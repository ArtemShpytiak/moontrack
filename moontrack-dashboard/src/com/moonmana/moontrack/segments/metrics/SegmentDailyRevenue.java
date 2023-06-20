package com.moonmana.moontrack.segments.metrics;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;
import com.moonmana.moontrack.segments.charts.DailyRevenueChart;

import hive.model.segment.Segment;

public class SegmentDailyRevenue extends SegmentMetric {

	public SegmentDailyRevenue(Segment segment) {
		super(segment);
		setType(MetricType.REVENUE_DAILY);
	}

	@Override
	public String getSQL() {
		return
		// @formatter:off
			" SELECT date_trunc('day', iaps.date) AS day, SUM(iaps.price)"
			+ " FROM iaps"
			+ " WHERE iaps.r_user"
			+ " IN (SELECT segment_users.realm_user FROM segment_users WHERE segment_users.segment_id = " + segment.getId() + ")"
			+ getDateRange("iaps.date")
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
	public Object getValue(int index) {
		return String.format(Locale.US, "%.2f", (Float) rows.get(index)[1]);
	}

	@Override
	protected Chart getChartInstance() {
		return new DailyRevenueChart(this);
	}

}
