package moontrack.analytics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.moonmana.utils.MapBuilder;

public class DateTimeOptimizationManager {

	private static final String TABLE_NAME_PATTERN = "last_full_datetime";
	private final MoontrackAppAliasManager appAliasManager;
	private final Map<String, String> grantedPriveleges;
	private final String owner;
	
	public static final String USER_ID = "user_id";
	public static final String REALM_ID = "realm_id";
	public static final String DATE = "date";
	
	private static final Map<String, Class<?>> dateTimeTableFields = new MapBuilder<String, Class<?>>(
			new LinkedHashMap<>(), true)
			.put(USER_ID, Integer.class)
			.put(REALM_ID, Integer.class)
			.put(DATE, Date.class)
			.build(); 
	
	public static void main(String[] args) {
		String tableName = TABLE_NAME_PATTERN + "_sometest";
		String owner = "analytics";
		Map<String, String> grantedPriveleges = new LinkedHashMap<>();
		System.out.println(SqlQueryBuilder.createTableQuery(tableName, dateTimeTableFields, new String[]{USER_ID, REALM_ID}, owner, grantedPriveleges));
	}

	public DateTimeOptimizationManager(MoontrackAppAliasManager appAliasManager, String owner, Map<String, String> grantedPriveleges) {
		this.appAliasManager = appAliasManager;
		this.owner = owner;
		this.grantedPriveleges = grantedPriveleges;
	}

	public void saveDateTime(String appId, int userId, int realmId, MoontrackClientEvent event, Connection connection) throws SQLException {
		String tableName = getTableName(appId, connection);
		
		if (!SqlQueryBuilder.isTableExist(connection, tableName)) {
			String tableQuery = SqlQueryBuilder.createTableQuery(tableName, dateTimeTableFields, new String[]{USER_ID, REALM_ID}, owner, grantedPriveleges);
			Statement statement = connection.createStatement();
			statement.executeUpdate(tableQuery);
			connection.commit();
		}
		
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + tableName + " ("
				+ "user_id, realm_id, date"
				+ ") VALUES (?, ?, ?)" 
				+ "ON CONFLICT (user_id, realm_id) DO UPDATE SET "
				+ "date = EXCLUDED.date");
		preparedStatement.setInt(1, userId);
		preparedStatement.setInt(2, realmId);
		preparedStatement.setTimestamp(3, new Timestamp(event.date.getTime()));
		
		preparedStatement.execute();
		connection.commit();
		
	}

	public String getTableName(String appId, Connection connection) {
		String appAlias = appAliasManager.getAppAlias(appId, connection);
		
		String tableName = TABLE_NAME_PATTERN + "_" + appAlias;
		return tableName;
	}

}
