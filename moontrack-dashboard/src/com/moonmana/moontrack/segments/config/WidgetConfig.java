package com.moonmana.moontrack.segments.config;

import java.util.HashMap;
import java.util.Map;

import com.moonmana.moontrack.metric.MetricType;

public class WidgetConfig {
	public static Map<Byte, String[]> metricMarkups = new HashMap<Byte, String[]>();

	static {
		metricMarkups.put(MetricType.ARPDAU, new String[] { "arpdau-metric", "chart-arpdau", "arpdau" });
		metricMarkups.put(MetricType.REVENUE_DAILY,
				new String[] { "dailyrevenue-metric", "chart-dailyrevenue", "dailyrevenue" });
		metricMarkups.put(MetricType.DAU, new String[] { "dau-metric", "chart-dau", "dau" });
		metricMarkups.put(MetricType.DEVICES, new String[] { "devices-metric", "chart-devices", "devices" });
		metricMarkups.put(MetricType.DPU, new String[] { "dpu-metric", "chart-dpu", "dpu" });
		metricMarkups.put(MetricType.LOGINS, new String[] { "logins-metric", "chart-logins", "logins" });
		metricMarkups.put(MetricType.OSES, new String[] { "oses-metric", "chart-oses", "oses" });
		metricMarkups.put(MetricType.PLATFORMS, new String[] { "platforms-metric", "chart-platforms", "platforms" });
	}
}
