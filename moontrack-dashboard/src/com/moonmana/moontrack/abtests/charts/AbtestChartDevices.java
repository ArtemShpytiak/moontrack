package com.moonmana.moontrack.abtests.charts;

import com.moonmana.moontrack.abtests.metrics.AbtestMetric;
import com.moonmana.moontrack.chart.ChartType;

import hive.model.abtest.AbTest;

public class AbtestChartDevices extends AbtestChart {

	public AbtestChartDevices(AbtestMetric metric, AbTest abtest) {
		super(metric, abtest);
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
		return "chart-devices";
	}
}
