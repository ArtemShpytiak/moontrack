package com.moonmana.moontrack.metrics.charts;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.chart.ChartType;
import com.moonmana.moontrack.metric.Metric;

public class DevicesChart extends Chart {

	public DevicesChart(Metric metric) {
		super(metric);
	}

	@Override
	protected String getTitle() {
		return "Devices";
	}

	@Override
	protected String getType() {
		return ChartType.BAR;
	}

	@Override
	protected String getElement() {
		return "chart-" + metric.getWidgetId();
	}

}
