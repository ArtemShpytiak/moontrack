package com.moonmana.moontrack.abtests.metrics;

import com.moonmana.moontrack.abtests.charts.AbtestChartArpdau;
import com.moonmana.moontrack.abtests.metrics.groups.AbgroupARPDAU;
import com.moonmana.moontrack.abtests.metrics.groups.AbgroupMetric;
import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;

import hive.model.abtest.AbTest;
import hive.model.abtest.AbTestGroup;

public class AbtestARPDAU extends AbtestMetric {

	public AbtestARPDAU(AbTest abtest) {
		super(abtest);
		setType(MetricType.ARPDAU);
	}

	@Override
	protected AbgroupMetric getSubquery(AbTestGroup group) {
		return new AbgroupARPDAU(group.getId());
	}

	@Override
	protected Chart getChartInstance() {
		return new AbtestChartArpdau(this, abtest);
	}

}
