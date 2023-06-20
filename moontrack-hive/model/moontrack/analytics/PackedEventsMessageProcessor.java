package moontrack.analytics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.hibernate.Session;

import com.google.common.collect.Iterables;
import com.moonmana.log.Log;
import com.moonmana.utils.CompressionUtil;
import com.moonmana.utils.TimeUtils;
import com.moonmana.pack.Pack;

import core.action.exceptions.InvalidActionException;
import core.cluster.core.MessageProcessor;
import core.cluster.core.manager.ServiceManager;
import core.cluster.core.manager.SqlDatabaseModule;
import moontrack.analytics.communication.PackedEventsMessage;

public class PackedEventsMessageProcessor extends MessageProcessor<PackedEventsMessage> {

	private Connection connection;
	private Date packFirstEventDate = null;
	private static final Pattern DB_NAMES_PATTERN = Pattern.compile("[a-z]+[a-z\\d_]*");
	
	public PackedEventsMessageProcessor(PackedEventsMessage message) {
		super(message);
	}
	
	@Override
	public void init() {
		super.init();
		ServiceManager serviceManager = ServiceManager.getInstance();
		String serviceName = serviceManager.getServiceNameForObject(message);
		connection = SqlDatabaseModule.getConnection(serviceName);
		System.out.println("We have connection!!!");
	}

	@Override
	protected void perform() throws InvalidActionException {
		byte[] decompressed = CompressionUtil.decompress(message.data);
		Pack inputPack = new Pack(decompressed);
		String appId = inputPack.getUTF();
		int userId = inputPack.getInt();
		int realmId = inputPack.getShort();
		
		@SuppressWarnings("unused")
		String userkey = inputPack.getUTF(); // we'll use later
		
		while (inputPack.getBytesAvailable() > 0) {
			MoontrackClientEvent event = readEvent(inputPack);
			trySaveEvent(appId, userId, realmId, event);
		}	}
	
	private void trySaveEvent(String appId, int userId, int realmId, MoontrackClientEvent event) {
		System.out.println("Trying to save event " + event.name);
		if (!areNamesMatchFormat(event)) {
			return;
		}

		if (event.dateStrategy == EventDateStrategy.SESSION_START_FULL_DATE) {
			refreshFullDateTime(appId, userId, realmId, event);
		}

		
		MoontrackEventsTableProvider.instance.checkTableAvailability(appId, event, connection, session);
		
		String statementText = null;
		
		try {
			SimpleEntry<PreparedStatement, String> pair = createEventSavingQuery(appId, userId, realmId, event);
			PreparedStatement preparedStatement = pair.getKey();
			statementText = pair.getValue();
			preparedStatement.executeUpdate();
			
			connection.commit();
		} catch (SQLException e) {
			Log.outError("Can't save event " + event.name + ". Problem with query :" + statementText, e);
		}
		
	}

	private void refreshFullDateTime(String appId, int userId, int realmId, MoontrackClientEvent event) {
		try {
			MoontrackEventsTableProvider.instance.dateTimeOptimizer.saveDateTime(appId, userId,
					realmId, event, connection);
		} catch (SQLException e) {
			Log.outError("Can't update datetime appId= " + appId + ", user,realm=(" + userId
					+ "," + realmId + ") datetime = "
					+ TimeUtils.timestampFormat.format(event.date), e);
		}
	}

	private SimpleEntry<PreparedStatement, String> createEventSavingQuery(String appId, int userId, int realmId, MoontrackClientEvent event) throws SQLException {
		String tableName = MoontrackEventsTableProvider.instance.getTableName(appId, event, connection);
		
		PreparedStatementBuilder psb = new PreparedStatementBuilder(appId, MoontrackEventsTableProvider.instance.dateTimeOptimizer);
		psb.intValues.put(MoontrackEventsTableProvider.USER, userId);
		psb.intValues.put(MoontrackEventsTableProvider.REALM, realmId);
		
		psb.dateValues.put(MoontrackEventsTableProvider.DATE, event.date);
		psb.isDateFull = event.dateStrategy != EventDateStrategy.INTERVAL_AFTER_SESSION_START;
		
		psb.intValues.putAll(event.intMap);
		psb.boolValues.putAll(event.boolMap);
		psb.doubleValues.putAll(event.doubleMap);
		psb.stringValues.putAll(event.stringMap);
		
		return psb.createInsertPreparedStatement(connection, tableName);
	}

	private boolean areNamesMatchFormat(MoontrackClientEvent event) {
		if (!DB_NAMES_PATTERN.matcher(event.name).matches()) {
			Log.outError("Event name '" + event.name + "' does not match format.");
			return false;
		}
		Iterable<String> allNames = Iterables.concat(
				event.boolMap.keySet(),
				event.intMap.keySet(),
				event.doubleMap.keySet(),
				event.stringMap.keySet());
		for (String fieldName : allNames) {
			if (!DB_NAMES_PATTERN.matcher(fieldName).matches()) {
				Log.outError("Event field name '" + fieldName + "' from event " + event.name + " does not match format.");
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void clean() {
		try {
			connection.close();
		} catch (SQLException e) {
			
		}
		// we do not call super.clean(); because we have no session
	}

	private MoontrackClientEvent readEvent(Pack inputPack) {
		String name = inputPack.getUTF();
		SimpleEntry<Date, Byte> datePair = getDate(inputPack);
		Date date = datePair.getKey();
		
		Map<String, Boolean> boolMap = readMap(inputPack, (pack)->pack.getBoolean());
		Map<String, Double> doubleMap = readMap(inputPack, (pack)->pack.getDouble());
		Map<String, Integer> intMap = readMap(inputPack, (pack)->pack.getInt());
		Map<String, String> stringMap = readMap(inputPack, (pack)->pack.getUTF());
		
		MoontrackClientEvent moontrackEvent = new MoontrackClientEvent(name, date, datePair.getValue(), boolMap, doubleMap, intMap, stringMap);
		return moontrackEvent;
	}

	private AbstractMap.SimpleEntry<Date, Byte> getDate(Pack inputPack) {
		byte datePackingType = packFirstEventDate == null ? inputPack.getByte() : EventDateStrategy.INTERVAL_AFTER_PACK_FIRST_DATE;
		Date resultDate = null;
		switch (datePackingType) {
		case EventDateStrategy.ALL_FULL_DATE:
		case EventDateStrategy.SESSION_START_FULL_DATE:
			resultDate = new Date(inputPack.getLong());
			break;
		case EventDateStrategy.INTERVAL_AFTER_SESSION_START:
			resultDate = new Date(inputPack.getInt());
			break;
		case EventDateStrategy.INTERVAL_AFTER_PACK_FIRST_DATE:
			resultDate = packFirstEventDate == null
					? packFirstEventDate = new Date(inputPack.getLong())
					: new Date(packFirstEventDate.getTime() + inputPack.getInt());
			break;
		
		}
		return new SimpleEntry<>(resultDate, datePackingType);
	}

	private <T>Map<String, T> readMap(Pack inputPack, Function<Pack, T> funk) {
		Map<String, T> map = new HashMap<>();
		short len = inputPack.getShort();
		for (short i = 0; i < len; i++) {
			String key = inputPack.getUTF();
			T value = funk.apply(inputPack);
			map.put(key, value);
		}
		return map;
	}

	@Override
	protected Session createSession() { // what do we say to the god of refactoring? Not today!
		return null;
	}
}
