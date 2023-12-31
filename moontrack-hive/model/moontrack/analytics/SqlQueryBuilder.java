package moontrack.analytics;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.moonmana.utils.MapBuilder;

public class SqlQueryBuilder {
	
	private static final String INSERT_INTO = "INSERT INTO ";
	private static final String VALUES = "VALUES ";
	
	public static final Map<Class<?>, String> SQL_TYPES = new MapBuilder<Class<?>, String>()
			.put(Boolean.class, "boolean")
			.put(Integer.class, "integer")
			.put(String.class, "text")
			.put(Double.class, "double precision")
			.put(Date.class, "timestamp without time zone")
			.build();

	public static String createInsertSingleQuery(String tableName, Map<String, String> values) {
		return createInsertQuery(tableName, values.entrySet().stream().collect(Collectors.toMap((e) -> e.getKey(), (e) -> new String[]{e.getValue()})));
	}
	
	public static String createInsertQuery(String tableName, Map<String, String[]> values) {
		int numvalues = values.values().iterator().next().length;
		int len = 16 + INSERT_INTO.length() + tableName.length() + 2 + 2 + VALUES.length() +   
				+ (2 + 2) * (values.size() - 1) + numvalues * 3 + getAllStringsLength(values);
		StringBuilder result = new StringBuilder(len);
		result.append(INSERT_INTO);
		result.append(tableName);
		result.append(" (");  //2
		
		ArrayList<String> columnsOrder = new ArrayList<>(values.keySet());
		
		boolean addComma = false;
		for (String columnName : columnsOrder) {
			if (addComma) {
				result.append(", "); //2
			} else {
				addComma = true;
			}
			result.append(columnName);
		}
		
		result.append(") "); // 2
		result.append(VALUES);
		
		for (int i = 0; i < numvalues; i++) {
			result.append("(");
			addComma = false;
			for (String columnName : columnsOrder) {
				if (addComma) {
					result.append(", "); //2
				} else {
					addComma = true;
				}
				result.append(values.get(columnName)[i]);
			}
			if (i + 1 == numvalues) {
				result.append(")");
			} else {
				result.append("),");
			}
		}
		if (result.length() > len) {
			System.out.println("INSERT BUILDER: Final len is bigger than expected on " + (len - result.length()));
		}
		
		return result.toString();
	}


	private static int getAllStringsLength(Map<String, String[]> values) {
		return values.entrySet().stream().collect(
				Collectors.summingInt((e) -> e.getKey().length() + Arrays.stream(e.getValue())
						.collect(Collectors.summingInt((arrE) -> arrE.length()))));
	}
	
	public static String createTableQuery(String tableName, Map<String, Class<?>> typesMap, String[] pk, String owner, Map<String, String> grantedPriveleges) {
		StringBuilder queryBuilder = new StringBuilder(1 << 12);
		queryBuilder.append("CREATE TABLE IF NOT EXISTS public.").append(tableName).append("(");
		boolean addComma = false;
		if (pk.length == 0) {
			queryBuilder.append("id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY");
			addComma = true;
		}

		for (Entry<String, Class<?>> entry : typesMap.entrySet()) {
			if (addComma) {
				queryBuilder.append(",");
			} else {
				addComma = true;
			}
			queryBuilder.append(entry.getKey()).append(" ")
					.append(getSQLType(entry.getValue()));
		}
		if (pk.length != 0) {
			queryBuilder.append(", PRIMARY KEY(")
			.append(String.join(",", pk))
			.append(")");// comma added below in cicle
		}
		queryBuilder.append(")WITH (OIDS = FALSE) TABLESPACE pg_default;");
		
		queryBuilder.append("ALTER TABLE public.").append(tableName)
		.append(" OWNER to \"").append(owner).append("\";");
		for (Entry<String, String> privelege : grantedPriveleges.entrySet()) {
			addGrant(queryBuilder, tableName, privelege.getValue(), privelege.getKey());
		}
		addGrant(queryBuilder, tableName, "ALL", owner);

		return queryBuilder.toString();
	}
	
	public static String createTableQuery(String tableName, Map<String, Class<?>> typesMap, String owner, Map<String, String> grantedPriveleges) {
//		StringBuilder queryBuilder = new StringBuilder(1 << 12);
//		queryBuilder.append("CREATE TABLE public.").append(tableName).append("(")
//				.append("id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY");// comma added below in cicle
//
//		for (Entry<String, Class<?>> entry : typesMap.entrySet()) {
//			queryBuilder.append(",").append(entry.getKey()).append(" ")
//					.append(getSQLType(entry.getValue()));
//		}
//		queryBuilder.append(")WITH (OIDS = FALSE) TABLESPACE pg_default;");
//		
//		queryBuilder.append("ALTER TABLE public.").append(tableName)
//		.append(" OWNER to ").append(owner).append(";");
//		for (Entry<String, String> privelege : grantedPriveleges.entrySet()) {
//			addGrant(queryBuilder, tableName, privelege.getValue(), privelege.getKey());
//		}
//		addGrant(queryBuilder, tableName, "ALL", owner);
//
//		return queryBuilder.toString();
		return createTableQuery(tableName, typesMap, new String[0], owner, grantedPriveleges);
	}
	
	private static void addGrant(StringBuilder queryBuilder, String tableName, String privilege,
			String role) {
		queryBuilder.append("GRANT ").append(privilege).append(" ON TABLE public.")
				.append(tableName).append(" TO \"").append(role).append("\";");
	}

	private static String getSQLType(Class<?> classType) {
		return SQL_TYPES.get(classType);
	}
	
	public static boolean isTableExist(Connection connection, String tableName) throws SQLException {
		Statement statement = connection.createStatement();
		ResultSet res = null;
		try {
			DatabaseMetaData meta = connection.getMetaData();
			res = meta.getTables(null, "public", tableName, null);
			return res.next();
		} finally {
			if (res != null) {
				res.close();
			}
			statement.close();
		}
	}

}
