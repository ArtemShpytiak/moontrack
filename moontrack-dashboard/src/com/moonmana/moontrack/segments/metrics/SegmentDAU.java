package com.moonmana.moontrack.segments.metrics;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;
import com.moonmana.moontrack.segments.charts.DauChart;

import hive.model.segment.Segment;

public class SegmentDAU extends SegmentMetric {

	public SegmentDAU(Segment segment) {
		super(segment);
		setType(MetricType.DAU);
	}

	@Override
	public String getSQL() {
		return
		// @formatter:off
			" SELECT activities.date, COUNT(activities.d_user)"
			+ " FROM activities"
			+ " WHERE activities.d_user"
			+ " IN (SELECT realm_users.d_user FROM realm_users WHERE realm_users.id"
			+ " IN (SELECT segment_users.realm_user FROM segment_users WHERE segment_users.segment_id = " + segment.getId() + "))"
			+ getDateRange("activities.date")
			+ " GROUP BY activities.date"
			+ " ORDER BY activities.date DESC"
			+ " LIMIT " + getLimit()
			+ " OFFSET 1";
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
		return new DauChart(this);
	}

}
