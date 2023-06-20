package com.moonmana.moontrack.segments.metrics;

import com.moonmana.moontrack.metric.MetricType;

import hive.model.segment.Segment;

public class SegmentMetricFactory {

	public static SegmentMetric getSegmentMetric(byte metricType, Segment segment) {
		switch (metricType) {
		case MetricType.ARPDAU:
			return new SegmentARPDAU(segment);
		case MetricType.REVENUE_DAILY:
			return new SegmentDailyRevenue(segment);
		case MetricType.DAU:
			return new SegmentDAU(segment);
		case MetricType.DEVICES:
			return new SegmentDevices(segment);
		case MetricType.DPU:
			return new SegmentDPU(segment);
		case MetricType.LOGINS:
			return new SegmentLogins(segment);
		case MetricType.OSES:
			return new SegmentOses(segment);
		case MetricType.PLATFORMS:
			return new SegmentPlatforms(segment);

		default:
			return null;
		}
	}
}
