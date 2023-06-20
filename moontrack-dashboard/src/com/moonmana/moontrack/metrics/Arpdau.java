package com.moonmana.moontrack.metrics;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;
import com.moonmana.moontrack.metric.MetricWithFilters;
import com.moonmana.moontrack.metrics.charts.ArpdauChart;

import hive.model.filter.FilterHub;

public class Arpdau extends MetricWithFilters {

	public Arpdau(FilterHub filter) {
		super(filter);
		setType(MetricType.ARPDAU);
	}

	@Override
	public String getSQL() {
		return
		// @formatter:off
			  " SELECT activities.date, sumprice / COUNT(DISTINCT activities.d_user)" +
			  " FROM " + getFromTablesOuter() +
			  " activities" +
					" INNER JOIN ("
						+ " SELECT date_trunc('day', iaps.date) AS day, SUM(price) AS sumprice"
						+ " FROM iaps, realm_users, d_user_registrations"
						+ getFromTables()
						+ " WHERE iaps.r_user = realm_users.id"
						+ " AND realm_users.d_user = d_user_registrations.id"
						+ " AND iaps.sandbox = false"
						+ getGameWhereClause()
						+ getRegistrationWhereClause()
						+ getPlatformsWhereClause()
						+ getOsesWhereClause()
						+ getCountriesWhereClause()
						+ getPayingWhereClause()
						+ getDateRange("iaps.date")
						+ " GROUP BY day"
				+ " ) AS iaps ON day = activities.date" +
				" WHERE TRUE" +
				getWhereClause() +
				getGameWhereClause() +
				getRegistrationWhereClause() +
				getPlatformsWhereClause() +
				getOsesWhereClause() +
				getPayingWhereClause() +
				getDateRange("activities.date")
			+ " GROUP BY activities.date, sumprice"
			+ " ORDER BY activities.date DESC"
			+ " LIMIT " + getLimit();
		// @formatter:on
	}

	private String getWhereClause() {
		String result = "";
		if (filter.getGameId() != null) {
			result += " AND activities.d_user = d_user_registrations.id";
		}
		if (filter.getPaying() != null) {
			result += " AND iaps.r_user = realm_users.id";
			result += " AND realm_users.d_user = d_user_registrations.id";
		}
		return result;
	}

	protected String getFromTablesOuter() {
		Set<String> tables = new HashSet<String>();

		if (filter.getGameId() != null) {
			tables.add("d_user_registrations");
		}
		if (filter.getPaying() != null) {
			tables.add("realm_users");
			tables.add("iaps");
		}

		if (!tables.isEmpty()) {
			List<String> list = new ArrayList<String>(tables);
			return String.join(", ", list) + ", ";
		}
		return "";
	}

	private String getPayingWhereClause() {
		if (filter.getPaying() != null) {
			if (filter.getPaying()) {
				// paying users
				Set<String> range = new HashSet<String>();
				String havingClause = "";
				if (filter.getSpentMoneyFrom() != null || filter.getSpentMoneyTo() != null) {
					havingClause = " GROUP BY iaps.r_user HAVING";
				}
				if (filter.getSpentMoneyFrom() != null) {
					range.add(" SUM(iaps.price) >= " + Float.valueOf(filter.getSpentMoneyFrom()));
				}
				if (filter.getSpentMoneyTo() != null) {
					range.add(" SUM(iaps.price) <= " + Float.valueOf(filter.getSpentMoneyTo()));
				}
				return " AND iaps.r_user IN (SELECT iaps.r_user FROM iaps WHERE iaps.sandbox = false " + havingClause
						+ String.join(" AND ", new ArrayList<String>(range)) + ")";
			} else {
				// none paying users
				return " AND iaps.r_user NOT IN (SELECT iaps.r_user FROM iaps)";
			}
		}
		return "";
	}

	@Override
	public String getKey(int index) {
		Date date = (Date) rows.get(index)[0];
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	@Override
	public Object getValue(int index) {
		String value = String.format(Locale.US, "%.2f", (Double) rows.get(index)[1]);
		return value;
	}

	@Override
	protected Chart getChartInstance() {
		return new ArpdauChart(this);
	}

}
