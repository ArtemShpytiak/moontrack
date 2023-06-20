package com.moonmana.moontrack.metric;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MetricConfig {
	private static List<Byte> widgetMetrics = new ArrayList<Byte>();
	private static Map<Byte, String[]> metricMarkups = new LinkedHashMap<Byte, String[]>();

	static {
		metricMarkups.put(MetricType.USERS, new String[] { "users-metric", "chart-users", "users" });
		metricMarkups.put(MetricType.LOGINS, new String[] { "logins-metric", "chart-logins", "logins" });
		metricMarkups.put(MetricType.ARPDAU, new String[] { "arpdau-metric", "chart-arpdau", "arpdau" });
//		metricMarkups.put(MetricType.TOTAL_REVENUE,
//				new String[] { "totalrevenue-metric", "totalrevenue", "chart-totalrevenue" });
		metricMarkups.put(MetricType.REVENUE_DAILY,
				new String[] { "dailyrevenue-metric", "chart-dailyrevenue", "dailyrevenue" });
		metricMarkups.put(MetricType.DAU, new String[] { "dau-metric", "chart-dau", "dau" });
		metricMarkups.put(MetricType.DPU, new String[] { "dpu-metric", "chart-dpu", "dpu" });
		metricMarkups.put(MetricType.DEVICES, new String[] { "devices-metric", "chart-devices", "devices" });
		metricMarkups.put(MetricType.OSES, new String[] { "oses-metric", "chart-oses", "oses" });
		metricMarkups.put(MetricType.PLATFORMS, new String[] { "platforms-metric", "chart-platforms", "platforms" });

		widgetMetrics.add(MetricType.REVENUE_DAILY);
		widgetMetrics.add(MetricType.DAU);
		widgetMetrics.add(MetricType.MAU);
		widgetMetrics.add(MetricType.REVENUE_MONTHLY);
		widgetMetrics.add(MetricType.DPU);
		widgetMetrics.add(MetricType.MPU);
		widgetMetrics.add(MetricType.ARPPU_DAILY);
		widgetMetrics.add(MetricType.ARPPU_MONTHLY);
		widgetMetrics.add(MetricType.ARPDAU);
		widgetMetrics.add(MetricType.ARPMAU);
		widgetMetrics.add(MetricType.LOGINS);
		widgetMetrics.add(MetricType.REGISTRATIONS);
		widgetMetrics.add(MetricType.OSES);
		widgetMetrics.add(MetricType.DEVICES);
		widgetMetrics.add(MetricType.PLATFORMS);

	}

	public static Map<Byte, String[]> getAbtestMetricMarkups() {
		return metricMarkups;
	}

	public static List<Byte> getWidgetMetrics() {
		return widgetMetrics;
	}

	public static void setWidgetMetrics(List<Byte> widgetMetrics) {
		MetricConfig.widgetMetrics = widgetMetrics;
	}

}
