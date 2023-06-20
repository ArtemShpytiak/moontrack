package com.moonmana.moontrack.metrics.charts;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.chart.ChartType;
import com.moonmana.moontrack.metric.Metric;

public class InstallsTotalChart extends Chart {

	public InstallsTotalChart(Metric metric) {
		super(metric);
	}

	@Override
	protected String getTitle() {
		return "Installs";
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
