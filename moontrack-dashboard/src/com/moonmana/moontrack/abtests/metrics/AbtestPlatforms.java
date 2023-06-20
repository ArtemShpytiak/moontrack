package com.moonmana.moontrack.abtests.metrics;

import com.moonmana.moontrack.abtests.charts.AbtestChartPlatforms;
import com.moonmana.moontrack.abtests.metrics.groups.AbgroupMetric;
import com.moonmana.moontrack.abtests.metrics.groups.AbgroupPlatforms;
import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;

import hive.model.abtest.AbTest;
import hive.model.abtest.AbTestGroup;

public class AbtestPlatforms extends AbtestMetric {

	public AbtestPlatforms(AbTest abtest) {
		super(abtest);
		setType(MetricType.PLATFORMS);
	}

	@Override
	protected AbgroupMetric getSubquery(AbTestGroup group) {
		return new AbgroupPlatforms(group.getId());
	}

	@Override
	protected Chart getChartInstance() {
		return new AbtestChartPlatforms(this, abtest);
	}

}
