package com.moonmana.moontrack.segments.metrics;

import com.moonmana.OS;
import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;
import com.moonmana.moontrack.segments.charts.OsesChart;

import hive.model.segment.Segment;

public class SegmentOses extends SegmentMetric {

	public SegmentOses(Segment segment) {
		super(segment);
		setType(MetricType.OSES);
	}

	@Override
	public String getSQL() {
		return
		// @formatter:off
			" SELECT d_user_registrations.os_id, COUNT(d_user_registrations.os_id) as count"
			+ " FROM d_user_registrations"
			+ " WHERE d_user_registrations.id"
			+ " IN (SELECT realm_users.d_user FROM realm_users WHERE realm_users.id"
			+ " IN (SELECT segment_users.realm_user FROM segment_users WHERE segment_users.segment_id = " + segment.getId() + "))"
			+ " GROUP BY d_user_registrations.os_id"
			+ " ORDER BY count DESC";
		// @formatter:on
	}

	@Override
	public String getKey(int index) {
		Short osId = (Short) rows.get(index)[0];
		return OS.getOsName(osId.byteValue());
	}

	@Override
	protected Chart getChartInstance() {
		return new OsesChart(this);
	}

}
