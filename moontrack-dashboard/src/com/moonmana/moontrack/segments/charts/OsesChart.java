package com.moonmana.moontrack.segments.charts;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.chart.ChartType;
import com.moonmana.moontrack.metric.Metric;

public class OsesChart extends Chart {

	public OsesChart(Metric metric) {
		super(metric);
	}

	@Override
	protected String getTitle() {
		return "OSes";
	}

	@Override
	protected String getType() {
		return ChartType.BAR;
	}

	@Override
	protected String getElement() {
		return "chart-oses";
	}

}
