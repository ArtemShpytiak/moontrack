package com.moonmana.moontrack.metrics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;
import com.moonmana.moontrack.metric.MetricWithFilters;
import com.moonmana.moontrack.metrics.charts.MauChart;

import hive.model.filter.FilterHub;

public class Mau extends MetricWithFilters {

	public Mau(FilterHub filter) {
		super(filter);
		setType(MetricType.MAU);
	}

	@Override
	public String getSQL() {
		return
		// @formatter:off
				" SELECT date_trunc('month', activities.date) AS month, COUNT(activities.d_user)" +
				" FROM activities, d_user_registrations" +
				getFromTables() +
				" WHERE activities.d_user = d_user_registrations.id" +
					getGameWhereClause() +
					getOsesWhereClause() +
					getPlatformsWhereClause() +
					getRegistrationWhereClause() +
					getCountriesWhereClause() +
					getPayingWhereClause() +
					getDateRange("activities.date") +
				" GROUP BY month" +
				" ORDER BY month DESC" +
				" LIMIT " + getLimit();
		// @formatter:on
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
	protected String getFromTables() {
		String result = "";
		if (!filter.getCountries().isEmpty()) {
			result += ", a_users";
		}
		if (filter.getPaying() != null) {
			result += ", realm_users";
		}

		return result;
	}

	@Override
	protected Chart getChartInstance() {
		return new MauChart(this);
	}

}