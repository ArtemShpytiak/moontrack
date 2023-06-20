package moontrack.analytics;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.moonmana.utils.MapBuilder;

public class MoontrackAppAliasManager {

	public static final String APPLICATION_ALIAS = "application_alias";
	
	private final Map<String, String> appAliases = new ConcurrentHashMap<>();

	public MoontrackAppAliasManager(Connection connection, String owner, Map<String, String> grantedPriveleges) throws SQLException {
		Statement statement = null;
		try {
			if (!SqlQueryBuilder.isTableExist(connection, APPLICATION_ALIAS)) {
				Map<String, Class<?>> typesMap = new MapBuilder<String, Class<?>>()
						.put("app_id", String.class)
						.put("app_inner_alias", String.class)					
						.build();
				String appsTableQuery = SqlQueryBuilder.createTableQuery(APPLICATION_ALIAS, typesMap, owner, grantedPriveleges);
				statement = connection.createStatement();
				
					statement.executeUpdate(appsTableQuery);
					connection.commit();
			} else {
				refresh(connection);
			}
			
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}

	public Statement refresh(Connection connection) throws SQLException {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet result = statement.executeQuery("SELECT app_id, app_inner_alias FROM " + APPLICATION_ALIAS + ";");
			while (result.next()) {
				appAliases.put(result.getString("app_id"), result.getString("app_inner_alias"));
			}
			result.close();
		return statement;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}

	public String getAppAlias(String appId, Connection connection) {
		return appAliases.computeIfAbsent(appId, (e) -> createAlias(e, connection));
	}

	private synchronized String createAlias(String appId, Connection connection) {
		String newName = "app" + (appAliases.size() + 1);
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String queryText = SqlQueryBuilder.createInsertSingleQuery(APPLICATION_ALIAS, new MapBuilder<String, String>() 
					.put("app_id", "'" + appId + "'")
					.put("app_inner_alias", "'" + newName + "'")
					.build());
			statement.executeUpdate(queryText);
			connection.commit();
		} catch (SQLException e) {
			throw new RuntimeException("Error inserting app id " + appId, e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new RuntimeException("WAT???", e);
				}
			}
		}
		
		return newName;
	}
	
}
