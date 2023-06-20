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
import com.moonmana.moontrack.metrics.charts.DailyRevenueChart;

import hive.model.filter.FilterHub;

public class RevenueDaily extends MetricWithFilters {

	public RevenueDaily(FilterHub filter) {
		super(filter);
		setType(MetricType.REVENUE_DAILY);
	}

	// unused
	@Override
	public String getSQL() {
		return
		// @formatter:off
			" SELECT date_trunc('day', date) AS day," +
				" CAST(SUM(price) AS DOUBLE PRECISION)" +
			" FROM iaps, realm_users, d_user_registrations" +
			getFromTables() +
			" WHERE iaps.r_user = realm_users.id" +
			" AND realm_users.d_user = d_user_registrations.id" +
				getGameWhereClause() +
				getCountriesWhereClause() +
				getOsesWhereClause() +
				getPlatformsWhereClause() +
				getRegistrationWhereClause() +
				getPayingWhereClause() +
				getDateRange("iaps.date") +
			" GROUP BY day" +
			" ORDER BY day DESC" +
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
		return new DailyRevenueChart(this);
	}
}
