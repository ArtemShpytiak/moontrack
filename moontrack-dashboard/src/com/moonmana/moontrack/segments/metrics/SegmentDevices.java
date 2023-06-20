package com.moonmana.moontrack.segments.metrics;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;
import com.moonmana.moontrack.segments.charts.DevicesChart;

import hive.model.segment.Segment;

public class SegmentDevices extends SegmentMetric {

	public SegmentDevices(Segment segment) {
		super(segment);
		setType(MetricType.DEVICES);
	}

	@Override
	public String getSQL() {
		return
		// @formatter:off
			" SELECT d_user_registrations.device, COUNT(d_user_registrations.device) as counts"
			+ " FROM d_user_registrations"
			+ " WHERE d_user_registrations.id"
			+ " IN (SELECT realm_users.d_user FROM realm_users WHERE realm_users.id"
			+ " IN (SELECT segment_users.realm_user FROM segment_users WHERE segment_users.segment_id = " + segment.getId() + "))"
			+ " GROUP BY d_user_registrations.device"
			+ " ORDER BY counts DESC"
			+ " LIMIT " + getLimit();
		// @formatter:on
	}

	@Override
	protected Chart getChartInstance() {
		return new DevicesChart(this);
	}

}
