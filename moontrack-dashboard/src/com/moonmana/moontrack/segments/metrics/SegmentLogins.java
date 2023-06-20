package com.moonmana.moontrack.segments.metrics;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;
import com.moonmana.moontrack.segments.charts.LoginsChart;

import hive.model.segment.Segment;

public class SegmentLogins extends SegmentMetric {

	public SegmentLogins(Segment segment) {
		super(segment);
		setType(MetricType.LOGINS);
	}

	@Override
	public String getSQL() {
		return
		// @formatter:off
			" SELECT date_trunc('day', logins.login_date) AS day, COUNT(logins.r_user)"
			+ " FROM logins"
			+ " WHERE logins.r_user"
			+ " IN (SELECT segment_users.realm_user FROM segment_users WHERE segment_users.segment_id = " + segment.getId() + ")"
			+ getDateRange("logins.login_date")
			+ " GROUP BY day"
			+ " ORDER BY day DESC"
			+ " LIMIT " + getLimit();
		// @formatter:on
	}

	@Override
	public String getKey(int index) {
		Timestamp date = (Timestamp) rows.get(index)[0];
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	@Override
	protected Chart getChartInstance() {
		return new LoginsChart(this);
	}

}
