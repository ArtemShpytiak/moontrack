package com.moonmana.moontrack.abtests.charts;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.chart.ChartType;
import com.moonmana.moontrack.metric.Metric;

public class EventChart extends Chart {

	public EventChart(Metric metric) {
		super(metric);
	}

	@Override
	protected String getTitle() {
		return metric.getFilter().getEventName();
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
