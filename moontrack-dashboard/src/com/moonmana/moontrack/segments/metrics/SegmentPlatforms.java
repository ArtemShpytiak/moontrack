package com.moonmana.moontrack.segments.metrics;

import com.moonmana.PlatformID;
import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;
import com.moonmana.moontrack.segments.charts.PlatformsChart;

import hive.model.segment.Segment;

public class SegmentPlatforms extends SegmentMetric {

	public SegmentPlatforms(Segment segment) {
		super(segment);
		setType(MetricType.PLATFORMS);
	}

	@Override
	public String getSQL() {
		return
		// @formatter:off
			" SELECT d_user_registrations.platform_id, COUNT(d_user_registrations.platform_id) as count"
			+ " FROM d_user_registrations"
			+ " WHERE d_user_registrations.id"
			+ " IN (SELECT realm_users.d_user FROM realm_users WHERE realm_users.id"
			+ " IN (SELECT segment_users.realm_user FROM segment_users WHERE segment_users.segment_id = " + segment.getId() + "))"
			+ " GROUP BY d_user_registrations.platform_id"
			+ " ORDER BY count DESC";
		// @formatter:on
	}

	@Override
	protected Chart getChartInstance() {
		return new PlatformsChart(this);
	}

	@Override
	public String getKey(int index) {
		if (rows.get(index)[0] == null) {
			return super.getKey(index);
		}
		Short osId = (Short) rows.get(index)[0];
		return PlatformID.getPlatformName(osId.byteValue());
	}

}
