package com.moonmana.moontrack.metrics.charts;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.chart.ChartType;
import com.moonmana.moontrack.metric.Metric;

public class LoginsChart extends Chart {

	public LoginsChart(Metric metric) {
		super(metric);
	}

	@Override
	protected String getTitle() {
		return "Logins";
	}

	@Override
	protected String getType() {
		return ChartType.LINE;
	}

	@Override
	protected String getElement() {
		return "chart-" + metric.getWidgetId();
	}

}
