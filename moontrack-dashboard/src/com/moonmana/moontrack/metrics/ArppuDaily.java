package com.moonmana.moontrack.metrics;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;
import com.moonmana.moontrack.metric.MetricWithFilters;
import com.moonmana.moontrack.metrics.charts.DailyArppuChart;

import hive.model.filter.FilterHub;

public class ArppuDaily extends MetricWithFilters {

	public ArppuDaily(FilterHub filter) {
		super(filter);
		setType(MetricType.ARPPU_DAILY);
	}

	@Override
	public String getSQL() {
		return
		// @formatter:off
				" SELECT date_trunc('day', iaps.date) AS day, SUM(iaps.price) / COUNT(DISTINCT iaps.r_user)"
				+ " FROM iaps, realm_users, d_user_registrations"
				+ getFromTables()
				+ " WHERE iaps.r_user = realm_users.id"
				+ " AND realm_users.d_user = d_user_registrations.id"
				+ getWhereClause()
				+ getGameWhereClause()
				+ getCountriesWhereClause()
				+ getOsesWhereClause()
				+ getPlatformsWhereClause()
				+ getRegistrationWhereClause()
				+ getPayingWhereClause()
				+ getDateRange("iaps.date")
				+ " GROUP BY day"
				+ " ORDER BY day DESC"
				+ " LIMIT " + getLimit();
		// @formatter:on
	}

	@Override
	protected String getFromTables() {
		if (!filter.getCountries().isEmpty()) {
			return ",a_users";
		}
		return "";
	}

	private String getWhereClause() {
		if (!filter.getCountries().isEmpty()) {
			return " AND d_user_registrations.a_user = a_users.id";
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
	public Object getValue(int index) {
		return String.format(Locale.US, "%.2f", (Double) rows.get(index)[1]);
	}

	@Override
	protected Chart getChartInstance() {
		return new DailyArppuChart(this);
	}
}
