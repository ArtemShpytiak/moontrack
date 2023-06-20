package com.moonmana.moontrack.metrics;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;
import com.moonmana.moontrack.metric.MetricWithFilters;
import com.moonmana.moontrack.metrics.charts.RegistrationsChart;

import hive.model.filter.FilterHub;

public class Registrations extends MetricWithFilters {

	public Registrations(FilterHub filter) {
		super(filter);
		setType(MetricType.REGISTRATIONS);
	}

	@Override
	public String getSQL() {
		return
		// @formatter:off
				" SELECT date_trunc('day', d_user_registrations.registration_date) AS day, COUNT(d_user_registrations.id)" +
				" FROM d_user_registrations" +
				getFromTables() +
				" WHERE registration_date IS NOT NULL" +
					getGameWhereClause() +
					getCountriesWhereClause() +
					getOsesWhereClause() +
					getPlatformsWhereClause() +
					getRegistrationWhereClause() +
					getPayingWhereClause() +
					getDateRange("d_user_registrations.registration_date") +
				" GROUP BY day" +
				" ORDER BY day DESC" +
				" LIMIT " + getLimit();
		// @formatter:on
	}

	@Override
	protected String getFromTables() {
		Set<String> tables = new HashSet<String>();

		if (!filter.getCountries().isEmpty()) {
			tables.add("a_users");
		}
		if (filter.getPaying() != null) {
			tables.add("realm_users");
		}

		if (!tables.isEmpty()) {
			List<String> list = new ArrayList<String>(tables);
			return ", " + String.join(", ", list);
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
				return " AND d_user_registrations.id = realm_users.d_user"
						+ " AND realm_users.id IN (SELECT iaps.r_user FROM iaps WHERE iaps.sandbox = false "
						+ havingClause + String.join(" AND ", new ArrayList<String>(range)) + ")";
			} else {
				// none paying users
				return " AND iaps.r_user IS NULL";
			}
		}
		return "";
	}

	@Override
	public String getKey(int index) {
		Timestamp date = (Timestamp) rows.get(index)[0];
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	@Override
	protected Chart getChartInstance() {
		return new RegistrationsChart(this);
	}

}