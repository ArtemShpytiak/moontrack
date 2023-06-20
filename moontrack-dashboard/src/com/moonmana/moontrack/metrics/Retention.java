package com.moonmana.moontrack.metrics;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;
import com.moonmana.moontrack.metric.MetricWithFilters;
import com.moonmana.moontrack.metrics.charts.RetentionChart;

import hive.model.filter.FilterHub;

public class Retention extends MetricWithFilters {

	private byte interval;

	public Retention(FilterHub filter, byte type) {
		super(filter);
		setType(type);
		setInterval(type);
	}

	private void setInterval(byte type) {
		switch (type) {
		case MetricType.RETENTION_1_DAY:
			interval = 1;
			break;
		case MetricType.RETENTION_7_DAYS:
			interval = 7;
			break;
		case MetricType.RETENTION_30_DAYS:
			interval = 30;
			break;
		default:
			interval = 0;
			break;
		}
	}

	@Override
	public String getSQL() {
		return
		// @formatter:off
				"WITH  retentioned_users(date, cou) AS ("
				+ "SELECT activities.date, count(*) FROM d_user_registrations, activities "
				+ "WHERE d_user_registrations.id = d_user	"
				+ getDateRange("activities.date")
				+ "AND date(registration_date) + interval '"+ interval +" day' = activities.date "
				+ getGameWhereClause()
				+ " GROUP BY activities.date "
				+ "ORDER BY activities.date DESC "
				+ "LIMIT 1 OFFSET 1)  "
				+ "SELECT retentioned_users.date, retentioned_users.cou\\:\\:double precision / count(*) * 100 FROM "
				+ "d_user_registrations, retentioned_users "
				+ "WHERE true "
				+ getGameWhereClause()				
				+ " AND date(registration_date) + interval '"+ interval +" day' = retentioned_users.date "
				+ " GROUP by retentioned_users.date, retentioned_users.cou ";
		// @formatter:on
	}

	@Override
	public String getKey(int index) {
		Date date = (Date) rows.get(index)[0];
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	@Override
	protected Chart getChartInstance() {
		return new RetentionChart(this);
	}
}
