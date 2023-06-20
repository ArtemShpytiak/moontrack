package com.moonmana.moontrack.segments.charts;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.chart.ChartType;
import com.moonmana.moontrack.metric.Metric;

public class MonthlyRevenueChart extends Chart {

	public MonthlyRevenueChart(Metric metric) {
		super(metric);
	}

	@Override
	protected String getTitle() {
		return "Monthly Revenue";
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
