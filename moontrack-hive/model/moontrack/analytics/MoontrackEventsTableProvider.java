package moontrack.analytics;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.hibernate.Session;

import com.moonmana.utils.MapBuilder;

import core.dbUtils.SqlDataSourceUtil;
import core.utils.text.TextUtils;

public class MoontrackEventsTableProvider {
	public static final String USER = "user_id";
	public static final String REALM = "realm_id";
	public static final String DATE = "date";
	private static final String[] RESERVED_COLUMN_NAMES = new String[]{"id", USER, REALM, DATE};

	public static final String MOONTRACK_EVENT_TABLE_SCHEMAS = "moontrack_event_table_schemas";

	public static MoontrackEventsTableProvider instance = null;

	private final Map<String, EventTableSchema> schemas = new ConcurrentHashMap<>();
	
	private final MoontrackAppAliasManager appAliasManager;
	public final DateTimeOptimizationManager dateTimeOptimizer;

	private final String owner;

	private final Map<String, String> grantedPriveleges;
	
	public MoontrackEventsTableProvider(Connection connection, String owner, Map<String, String> grantedPriveleges) throws SQLException {
		this.owner = owner;
		this.grantedPriveleges = grantedPriveleges;
		appAliasManager = new MoontrackAppAliasManager(connection, owner, grantedPriveleges);
		dateTimeOptimizer = new DateTimeOptimizationManager(appAliasManager, owner, grantedPriveleges);
		fillEventSchemas(connection); 
	}

	public void checkTableAvailability(String appId, MoontrackClientEvent event,
			Connection connection, Session session) {
		String tableName = getTableName(appId, event, connection);
		EventTableSchema schema = schemas.get(tableName);
		if (schema == null) {
			schema = createSchemaIfAbsent(tableName, event, connection, session);
		}
		SchemeVerificationResult verification = schema.verify(event);
		if (!verification.isOK()) {
			schema.updateSchema(event, connection, session);
		}
	}

	public String getTableName(String appId, MoontrackClientEvent event, Connection connection) {
		return appAliasManager.getAppAlias(appId, connection) + "_" + event.name;
	}

	public void fillEventSchemas(Connection connection) {
		Statement statement = null;
		ResultSet result = null;
		try {
			statement = connection.createStatement();
			if (!SqlQueryBuilder.isTableExist(connection, MOONTRACK_EVENT_TABLE_SCHEMAS)) {
				String createTable = SqlQueryBuilder.createTableQuery(MOONTRACK_EVENT_TABLE_SCHEMAS,
						new MapBuilder<String, Class<?>>().put("table_name", String.class).build(), 
						owner, 
						grantedPriveleges);
				statement.executeUpdate(createTable);
				connection.commit();
			}
			result = statement.executeQuery(
					"SELECT table_name, column_name, data_type "
					+ "FROM information_schema.columns "
					+ "WHERE table_name IN (SELECT table_name FROM " + MOONTRACK_EVENT_TABLE_SCHEMAS + ")");
			Map<String, Map<String, String>> moontrackEventsTables = new HashMap<>();

			while (result.next()) {
				String tableName = result.getString(1);
				String columnName = result.getString(2);
				if (TextUtils.isIn(columnName, RESERVED_COLUMN_NAMES)) {
					continue;
				}
				String dataType = result.getString(3);
				moontrackEventsTables.computeIfAbsent(tableName, (k) -> new HashMap<>())
						.put(columnName, dataType);
			}
			
			Map<String, Class<?>> typesMap = MapBuilder.invert(SqlQueryBuilder.SQL_TYPES);
			moontrackEventsTables.forEach((tableName, columns) -> schemas.put(tableName,
					new EventTableSchema(columns.entrySet().stream().collect(Collectors
							.toMap((e) -> e.getKey(), (e) -> typesMap.get(e.getValue()))))));
			
		} catch (SQLException e) {
			throw new RuntimeException("Cannot  moontrack_event_table_schemas", e);
		} finally {
			try {
				if (result != null) {
					result.close();
				}
				if (statement != null && !statement.isClosed()) {
					statement.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException("No, really, wtf?", e);
			}
		}
	}

	private synchronized EventTableSchema createSchemaIfAbsent(String tableName, MoontrackClientEvent event,
			Connection connection, Session session) {
		return schemas.computeIfAbsent(tableName, (s) -> createSchema(tableName, event, connection));
	}

	
	private EventTableSchema createSchema(String tableName, MoontrackClientEvent event,
			Connection connection) {
		Map<String, Class<?>> typesMap = new LinkedHashMap<>(event.fieldsNumber() + 3);
		typesMap.put(USER, Integer.class);//extract
		typesMap.put(REALM, Integer.class);
		typesMap.put(DATE, Date.class);
		
		
		collectFields(event.boolMap, typesMap, Boolean.class);
		collectFields(event.stringMap, typesMap, String.class);
		collectFields(event.intMap, typesMap, Integer.class);
		collectFields(event.doubleMap, typesMap, Double.class);

		String query = SqlQueryBuilder.createTableQuery(tableName, typesMap, owner, grantedPriveleges);

		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			statement.executeUpdate(
					"INSERT INTO " + MOONTRACK_EVENT_TABLE_SCHEMAS + " (table_name) VALUES ('" + tableName
							+ "')");
			connection.commit();
		} catch (SQLException e) {
			SqlDataSourceUtil.closeStatementIfNotNull(statement);
			throw new RuntimeException("Shit happened", e);
		}

		EventTableSchema result = new EventTableSchema(typesMap);
		return result;
	}

	private <T> void collectFields(Map<String, T> fieldsMap, Map<String, Class<?>> typesMap,
			Class<T> fieldType) {
		fieldsMap.keySet().forEach((k) -> {
			typesMap.put(k, fieldType);
		});
	}

	public void refresh(Connection connection) throws SQLException {
		appAliasManager.refresh(connection);
		
	}
}
