package com.moonmana.moontrack.abtests.metrics;

import com.moonmana.moontrack.abtests.charts.AbtestChartDevices;
import com.moonmana.moontrack.abtests.metrics.groups.AbgroupDevices;
import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;

import hive.model.abtest.AbTest;
import hive.model.abtest.AbTestGroup;

public class AbtestDevices extends AbtestMetric {

	public AbtestDevices(AbTest abtest) {
		super(abtest);
		setType(MetricType.DEVICES);
	}

	@Override
	protected AbgroupDevices getSubquery(AbTestGroup group) {
		return new AbgroupDevices(group.getId());
	}

	@Override
	protected Chart getChartInstance() {
		return new AbtestChartDevices(this, abtest);
	}
}
