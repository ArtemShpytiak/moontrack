package com.moonmana.moontrack.segments.metrics;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;
import com.moonmana.moontrack.segments.charts.ArpdauChart;

import hive.model.segment.Segment;

public class SegmentARPDAU extends SegmentMetric {

	public SegmentARPDAU(Segment segment) {
		super(segment);
		setType(MetricType.ARPDAU);
	}

	@Override
	public String getSQL() {
		return
		// @formatter:off
			" SELECT activities.date, sumprice / COUNT(DISTINCT activities.d_user)"
			+ " FROM activities"
			+ " INNER JOIN ("
			+ " SELECT date_trunc('day', iaps.date) AS day, SUM(price) AS sumprice"
			+ " FROM iaps"
			+ " WHERE iaps.r_user"
			+ " IN (SELECT segment_users.realm_user FROM segment_users WHERE segment_users.segment_id = " + segment.getId() + ")"
			+ " AND iaps.sandbox = false"
			+ getDateRange("activities.date")
			+ " GROUP BY day"
			+ " ) AS iaps ON day = activities.date"
			+ " GROUP BY activities.date, sumprice"
			+ " ORDER BY activities.date DESC"
			+ " LIMIT " + getLimit();
		// @formatter:on
	}

	@Override
	public String getKey(int index) {
		Date date = (Date) rows.get(index)[0];
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	@Override
	public Object getValue(int index) {
		return String.format(Locale.US, "%.2f", (Double) rows.get(index)[1]);
	}

	@Override
	protected Chart getChartInstance() {
		return new ArpdauChart(this);
	}

}
