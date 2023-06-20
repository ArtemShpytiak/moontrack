package com.moonmana.moontrack.abtests.charts;

import com.moonmana.moontrack.abtests.metrics.AbtestMetric;
import com.moonmana.moontrack.chart.ChartType;

import hive.model.abtest.AbTest;

public class AbtestChartOses extends AbtestChart {

	public AbtestChartOses(AbtestMetric metric, AbTest abtest) {
		super(metric, abtest);
	}

	@Override
	protected String getTitle() {
		return "Oses";
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
