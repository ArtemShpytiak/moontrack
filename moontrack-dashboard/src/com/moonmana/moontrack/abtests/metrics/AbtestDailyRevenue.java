package com.moonmana.moontrack.abtests.metrics;

import com.moonmana.moontrack.abtests.charts.AbtestChartDailyRevenue;
import com.moonmana.moontrack.abtests.metrics.groups.AbgroupDailyRevenue;
import com.moonmana.moontrack.abtests.metrics.groups.AbgroupMetric;
import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;

import hive.model.abtest.AbTest;
import hive.model.abtest.AbTestGroup;

public class AbtestDailyRevenue extends AbtestMetric {

	public AbtestDailyRevenue(AbTest abtest) {
		super(abtest);
		setType(MetricType.REVENUE_DAILY);
	}

	@Override
	protected AbgroupMetric getSubquery(AbTestGroup group) {
		return new AbgroupDailyRevenue(group.getId(), abtest.getStartDate(), abtest.getFinishDate());
	}

	@Override
	protected Chart getChartInstance() {
		return new AbtestChartDailyRevenue(this, abtest);
	}

}
