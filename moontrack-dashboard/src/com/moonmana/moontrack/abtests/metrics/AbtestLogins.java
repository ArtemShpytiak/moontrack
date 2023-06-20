package com.moonmana.moontrack.abtests.metrics;

import com.moonmana.moontrack.abtests.charts.AbtestChartLogins;
import com.moonmana.moontrack.abtests.metrics.groups.AbgroupLogins;
import com.moonmana.moontrack.abtests.metrics.groups.AbgroupMetric;
import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;

import hive.model.abtest.AbTest;
import hive.model.abtest.AbTestGroup;

public class AbtestLogins extends AbtestMetric {

	public AbtestLogins(AbTest abtest) {
		super(abtest);
		setType(MetricType.LOGINS);
	}

	@Override
	protected AbgroupMetric getSubquery(AbTestGroup group) {
		return new AbgroupLogins(group.getId(), abtest.getStartDate(), abtest.getFinishDate());
	}

	@Override
	protected Chart getChartInstance() {
		return new AbtestChartLogins(this, abtest);
	}
}
