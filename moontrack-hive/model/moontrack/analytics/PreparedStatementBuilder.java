package moontrack.analytics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class PreparedStatementBuilder {
	public final Map<String, Integer> intValues = new LinkedHashMap<>();
	public final Map<String, Date> dateValues = new LinkedHashMap<>();
	public final Map<String, Boolean> boolValues = new LinkedHashMap<>();
	public final Map<String, Double> doubleValues = new LinkedHashMap<>();
	public final Map<String, String> stringValues = new LinkedHashMap<>();
	public boolean isDateFull = true;
	private final DateTimeOptimizationManager dateTimeOptimizer;
	private final String appId;
	
	public PreparedStatementBuilder(String appId, DateTimeOptimizationManager dateTimeOptimizer) {
		this.appId = appId;
		this.dateTimeOptimizer = dateTimeOptimizer;
	}

	public AbstractMap.SimpleEntry<PreparedStatement, String> createInsertPreparedStatement(Connection connection, String tableName) throws SQLException {
		StringBuilder sqlText = new StringBuilder(1 << 12);
		sqlText.append("INSERT INTO ");
		sqlText.append(tableName);
		sqlText.append(" (");  

		int columnsAdded = 0;
		columnsAdded += addCommaSeparated(sqlText, intValues.keySet(), false);
		columnsAdded += addCommaSeparated(sqlText, dateValues.keySet(), columnsAdded != 0);
		columnsAdded += addCommaSeparated(sqlText, boolValues.keySet(), columnsAdded != 0);
		columnsAdded += addCommaSeparated(sqlText, doubleValues.keySet(), columnsAdded != 0);
		columnsAdded += addCommaSeparated(sqlText, stringValues.keySet(), columnsAdded != 0);

		sqlText.append(") "); // 2
		int totalvals = getTotalValuesSize();
		if (isDateFull) {
			sqlText.append(" VALUES ");
			sqlText.append("(");
			
			for (int i = 0; i < totalvals; i++) {
				if (i == 0) {
					sqlText.append("?");
				} else {
					sqlText.append(",?");
				}
			}
			
			sqlText.append(")");	
		} else {
			
			sqlText.append("(SELECT ");
			
			for (int i = 0; i < totalvals; i++) {
				String argument = isDateArgument(i) ? DateTimeOptimizationManager.DATE + "+make_interval(secs := ?)" : "?";
				if (i != 0) {
					sqlText.append(",");
				}
				sqlText.append(argument);
			}
			
			sqlText.append(" FROM "); 
			sqlText.append(dateTimeOptimizer.getTableName(appId, connection)); 
			
			sqlText.append(" WHERE ")
			.append(DateTimeOptimizationManager.USER_ID)
			.append("=? AND ")
			.append(DateTimeOptimizationManager.REALM_ID)
			.append("=?)");
			
		}
		

		PreparedStatement preparedStatement = connection.prepareStatement(sqlText.toString());
		int counter = 1;
		
		counter = setValuesToStatement(counter, intValues.values(),
				(index, value) -> preparedStatement.setInt(index, value));
		counter = setValuesToStatement(counter, dateValues.values(),
				isDateFull
						? (index, value) -> preparedStatement.setTimestamp(index,
								new Timestamp(value.getTime()))
						: (index, value) -> preparedStatement.setDouble(index, ((double)value.getTime()) / 1000));
		counter = setValuesToStatement(counter, boolValues.values(),
				(index, value) -> preparedStatement.setBoolean(index, value));
		counter = setValuesToStatement(counter, doubleValues.values(),
				(index, value) -> preparedStatement.setDouble(index, value));
		counter = setValuesToStatement(counter, stringValues.values(),
				(index, value) -> preparedStatement.setString(index, value));

		if (!isDateFull) {
			preparedStatement.setInt(counter++, intValues.get(MoontrackEventsTableProvider.USER));
			preparedStatement.setInt(counter++, intValues.get(MoontrackEventsTableProvider.REALM));
		}

		
		
		return new AbstractMap.SimpleEntry<>(preparedStatement, sqlText.toString());
	}
	
	

	private boolean isDateArgument(int index) {
		index -= intValues.size();
		return index >= 0 && index < dateValues.size();
	}



	private static <T> int setValuesToStatement(int counter, 
			Collection<T> collection, SQLBiConsumer<Integer, T> settingFunk) throws SQLException {
		for (T v : collection) {
			settingFunk.accept(counter++, v);
		}
		return counter;
	}
	
	private int addCommaSeparated(StringBuilder result, Set<String> columns, boolean leadingComma) {
		boolean addComma = leadingComma;
		for (String columnName : columns) {
			if (addComma) {
				result.append(", "); 
			} else {
				addComma = true;
			}
			result.append(columnName);
		}
		return columns.size();
	}

	private int getTotalValuesSize() {
		return intValues.size() + dateValues.size() + boolValues.size() + doubleValues.size()
				+ stringValues.size();
	}

}
