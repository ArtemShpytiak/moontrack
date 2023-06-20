package com.moonmana.moontrack.abtests.metrics;

import com.moonmana.moontrack.abtests.charts.AbtestChartOses;
import com.moonmana.moontrack.abtests.metrics.groups.AbgroupMetric;
import com.moonmana.moontrack.abtests.metrics.groups.AbgroupOses;
import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;

import hive.model.abtest.AbTest;
import hive.model.abtest.AbTestGroup;

public class AbtestOses extends AbtestMetric {

	public AbtestOses(AbTest abtest) {
		super(abtest);
		setType(MetricType.OSES);
	}

	@Override
	protected AbgroupMetric getSubquery(AbTestGroup group) {
		return new AbgroupOses(group.getId());
	}

	@Override
	protected Chart getChartInstance() {
		return new AbtestChartOses(this, abtest);
	}

}
