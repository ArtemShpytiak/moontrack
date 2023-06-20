package com.moonmana.moontrack.metrics;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;
import com.moonmana.moontrack.metric.MetricWithFilters;
import com.moonmana.moontrack.metrics.charts.LoginsChart;

import hive.model.filter.FilterHub;

public class Logins extends MetricWithFilters {

	public Logins(FilterHub filter) {
		super(filter);
		setType(MetricType.LOGINS);
	}

	@Override
	public String getSQL() {
		return
		// @formatter:off
				" SELECT date_trunc('day', logins.login_date) AS day, COUNT(realm_users.d_user)" +
				" FROM logins, realm_users, d_user_registrations" +
				getFromTables() +
				" WHERE logins.r_user = realm_users.id" +
				" AND realm_users.d_user = d_user_registrations.id" +
					getGameWhereClause() +
					getCountriesWhereClause() +
					getOsesWhereClause() +
					getPlatformsWhereClause() +
					getRegistrationWhereClause() +
					getPayingWhereClause() +
					getCountriesWhereClause() +
					getDateRange("logins.login_date") +
				" GROUP BY day" +
				" ORDER BY day DESC" +
				" LIMIT " + getLimit();
		// @formatter:on
	}

	@Override
	public String getKey(int index) {
		Timestamp date = (Timestamp) rows.get(index)[0];
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
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
				return " AND realm_users.id IN (SELECT iaps.r_user FROM iaps WHERE iaps.sandbox = false " + havingClause
						+ String.join(" AND ", new ArrayList<String>(range)) + ")";
			} else {
				// none paying users
				return " AND iaps.r_user IS NULL";
			}
		}
		return "";
	}

	@Override
	protected String getFromTables() {
		if (!filter.getCountries().isEmpty()) {
			return ", a_users";
		}
		return "";
	}

	protected Chart getChartInstance() {
		return new LoginsChart(this);
	}

}