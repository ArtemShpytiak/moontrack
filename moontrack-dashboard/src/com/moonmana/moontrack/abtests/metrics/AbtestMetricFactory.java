package com.moonmana.moontrack.abtests.metrics;

import com.moonmana.moontrack.metric.MetricType;

import hive.model.abtest.AbTest;

public class AbtestMetricFactory {

	public static AbtestMetric getAbtestMetric(byte metricType, AbTest abtest) {
		switch (metricType) {
		case MetricType.ARPDAU:
			return new AbtestARPDAU(abtest);
		case MetricType.REVENUE_DAILY:
			return new AbtestDailyRevenue(abtest);
		case MetricType.DAU:
			return new AbtestDAU(abtest);
		case MetricType.DEVICES:
			return new AbtestDevices(abtest);
		case MetricType.DPU:
			return new AbtestDPU(abtest);
		case MetricType.LOGINS:
			return new AbtestLogins(abtest);
		case MetricType.OSES:
			return new AbtestOses(abtest);
		case MetricType.PLATFORMS:
			return new AbtestPlatforms(abtest);
		case MetricType.USERS:
			return new AbtestUsers(abtest);

		default:
			return null;
		}
	}
}
