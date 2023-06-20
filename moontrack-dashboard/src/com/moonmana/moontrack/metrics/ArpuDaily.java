package com.moonmana.moontrack.metrics;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;
import com.moonmana.moontrack.metric.MetricWithFilters;
import com.moonmana.moontrack.metrics.charts.ArpuDailyChart;

import hive.model.filter.FilterHub;

public class ArpuDaily extends MetricWithFilters {

	public ArpuDaily(FilterHub filter) {
		super(filter);
		setType(MetricType.ARPU_DAILY);
	}

	// TODO filters
	@Override
	public String getSQL() {
		return
		// @formatter:off
			" SELECT date_trunc('day', d_user_registrations.registration_date) AS install_day," +
		       " sumprice / count(*)" +
			" FROM d_user_registrations" +
				" INNER JOIN" +
				" (SELECT date_trunc('day', iaps.date) AS pay_day," +
				" SUM(price) AS sumprice" +
				" FROM iaps," +
				" realm_users," +
				" d_user_registrations" +
				" WHERE iaps.r_user = realm_users.id" +
				" AND realm_users.d_user = d_user_registrations.id" +
				" GROUP BY pay_day" +
				" ORDER BY pay_day DESC" +
				" LIMIT 1000) AS revenue ON pay_day = date_trunc('day', d_user_registrations.registration_date)" +
			" GROUP BY install_day," +
			" sumprice" +
			" ORDER BY install_day DESC" +
			" LIMIT " + getLimit();
		// @formatter:on
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
		return new ArpuDailyChart(this);
	}

}
