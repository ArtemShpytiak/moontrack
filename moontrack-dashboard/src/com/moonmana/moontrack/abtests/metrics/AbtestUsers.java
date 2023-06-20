package com.moonmana.moontrack.abtests.metrics;

import com.moonmana.moontrack.abtests.charts.AbtestChartUsers;
import com.moonmana.moontrack.abtests.metrics.groups.AbgroupMetric;
import com.moonmana.moontrack.abtests.metrics.groups.AbgroupUsers;
import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;

import hive.model.abtest.AbTest;
import hive.model.abtest.AbTestGroup;

public class AbtestUsers extends AbtestMetric {

	public AbtestUsers(AbTest abtest) {
		super(abtest);
		setType(MetricType.USERS);
	}

	@Override
	protected AbgroupMetric getSubquery(AbTestGroup group) {
		return new AbgroupUsers(group.getId());
	}

	@Override
	protected Chart getChartInstance() {
		return new AbtestChartUsers(this, abtest);
	}

}
