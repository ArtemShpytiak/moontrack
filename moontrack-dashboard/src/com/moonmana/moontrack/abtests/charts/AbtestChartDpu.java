package com.moonmana.moontrack.abtests.charts;

import com.moonmana.moontrack.abtests.metrics.AbtestMetric;
import com.moonmana.moontrack.chart.ChartType;

import hive.model.abtest.AbTest;

public class AbtestChartDpu extends AbtestChart {

	public AbtestChartDpu(AbtestMetric metric, AbTest abtest) {
		super(metric, abtest);
	}

	@Override
	protected String getTitle() {
		return "DPU";
	}

	@Override
	protected String getType() {
		return ChartType.LINE;
	}

	@Override
	protected String getElement() {
		return "chart-dpu";
	}

}
