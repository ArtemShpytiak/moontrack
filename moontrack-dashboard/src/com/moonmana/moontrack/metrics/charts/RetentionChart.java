package com.moonmana.moontrack.metrics.charts;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.Metric;

public class RetentionChart extends Chart {

	public RetentionChart(Metric metric) {
		super(metric);
	}

	@Override
	protected String getTitle() {
		return "Retention";
	}

	@Override
	protected String getType() {
		return null;
	}

	@Override
	protected String getElement() {
		return "chart-" + metric.getWidgetId();
	}

}
