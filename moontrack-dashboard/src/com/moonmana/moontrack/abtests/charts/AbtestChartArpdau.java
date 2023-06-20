package com.moonmana.moontrack.abtests.charts;

import com.moonmana.moontrack.abtests.metrics.AbtestMetric;
import com.moonmana.moontrack.chart.ChartType;

import hive.model.abtest.AbTest;

public class AbtestChartArpdau extends AbtestChart {

	public AbtestChartArpdau(AbtestMetric metric, AbTest abtest) {
		super(metric, abtest);
	}

	@Override
	protected String getTitle() {
		return "ARPDAU";
	}

	@Override
	protected String getType() {
		return ChartType.LINE;
	}

	@Override
	protected String getElement() {
		return "chart-arpdau";
	}

}
