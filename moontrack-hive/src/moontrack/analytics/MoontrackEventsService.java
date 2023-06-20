package moontrack.analytics;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.moonmana.log.Log;
import com.moonmana.utils.MapBuilder;

import core.cluster.core.Cluster;
import core.cluster.core.MapBasedMessageProcessorProvider;
import core.cluster.core.MessageProcessorProvider;
import core.cluster.core.manager.DatabaseService;
import core.cluster.core.manager.SqlDatabaseModule;
import core.cluster.services.config.ServiceEntry;
import core.dbUtils.SqlDataSourceUtil;
import core.hibernate.DbInfo;
import moontrack.analytics.communication.PackedEventsMessage;

public class MoontrackEventsService extends DatabaseService {
	public MoontrackEventsService() {
		super("moontrackevents");
	}

	@Override
	public DbInfo getDefaultDbInfo() {
		return new DbInfo("moontrack", "moontrack");
	}

	@Override
	public Collection<Class<? extends Cluster>> getClustersInUse() {
		Set<Class<? extends Cluster>> result = new HashSet<>();
		return result;
	}

	@Override
	protected void innerInit(ServiceEntry entry) {
		super.innerInit(entry);
		
		DbInfo dbInfo = createDbInfo(entry);
		Connection connection = null;
		initMessageProcessorProvider();
		
		try {
			connection = SqlDatabaseModule.getConnection(serviceName);
			Set<String> availableRoles = getExistingRoles(connection);
			
			Map<String, String> grants = new HashMap<>();
			if (availableRoles.contains("dashboard")) {
					grants.put("dashboard", "SELECT");
			}
			if (availableRoles.contains("weak_user")) {
				grants.put("weak_user", "SELECT");
			}
			
			try {
				MoontrackEventsTableProvider.instance = new MoontrackEventsTableProvider(connection, dbInfo.username, grants);
				fillAppIdsTable(connection);
			} catch (SQLException e) {
				throw new RuntimeException("Some error in init", e);
			}
		} finally {
			SqlDataSourceUtil.closeConnectionIfNotNull(connection);
		}
		
	}

	private Set<String> getExistingRoles(Connection connection) {
		Statement statement = null;
		ResultSet sqlQueryResult = null;
		Set<String> result = new HashSet<>();
		try {
			statement = connection.createStatement();
			sqlQueryResult = statement.executeQuery("SELECT rolname FROM pg_roles;");
			
			while (sqlQueryResult.next()) {
				String rolname = sqlQueryResult.getString(1);
				result.add(rolname);
			}
		} catch (SQLException e) {
			Log.outError("Cannot access pg_roles");
		} finally {
			try {
				if (sqlQueryResult != null) {
					sqlQueryResult.close();
				}
				if (statement != null && !statement.isClosed()) {
					statement.close();
				}
			} catch (SQLException e) {
				Log.outError("Some additional exception?", e);
			}
		}
		return result;
	}

	private void initMessageProcessorProvider() {
		MapBasedMessageProcessorProvider processorProvider = ((MapBasedMessageProcessorProvider) MessageProcessorProvider.instance);
		processorProvider.put(PackedEventsMessage.class, PackedEventsMessageProcessor.class);
	}

	private void fillAppIdsTable(Connection connection) throws SQLException {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			
			String selectQuery = "SELECT count(*) FROM " + MoontrackAppAliasManager.APPLICATION_ALIAS 
					+ " WHERE app_id IN ('pirates', 'bloodeville', 'dwarfs', 'defendersio', 'ultimatepirates');";
			ResultSet result = statement.executeQuery(selectQuery);
			result.next();
			int count = result.getInt(1);
			if (count == 0) {
				String query = SqlQueryBuilder.createInsertQuery(MoontrackAppAliasManager.APPLICATION_ALIAS, 
						new MapBuilder<String, String[]>()
						.put("app_id", new String[] {"'pirates'", "'bloodeville'", "'dwarfs'", "'defendersio'", "'ultimatepirates'"})
						.put("app_inner_alias", new String[] {"'poe'", "'bv'", "'ld'", "'dio'", "'up'"})
						.build());
				statement.executeUpdate(query);
				connection.commit();
				MoontrackEventsTableProvider.instance.refresh(connection);
			}
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}

	@Override
	public String[] getGroupTypes() {
		return new String[]{"moontrackevents"};
	}

}
