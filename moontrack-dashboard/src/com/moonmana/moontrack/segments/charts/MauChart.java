package com.moonmana.moontrack.segments.charts;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.chart.ChartType;
import com.moonmana.moontrack.metric.Metric;

public class MauChart extends Chart {

	public MauChart(Metric metric) {
		super(metric);
	}

	@Override
	protected String getTitle() {
		return "MAU";
	}

	@Override
	protected String getType() {
		return ChartType.LINE;
	}

	@Override
	protected String getElement() {
		return "chart-mau";
	}

}
