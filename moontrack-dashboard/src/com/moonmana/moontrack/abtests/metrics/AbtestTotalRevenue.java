package com.moonmana.moontrack.abtests.metrics;

import com.moonmana.moontrack.abtests.charts.AbtestChartTotalRevenue;
import com.moonmana.moontrack.abtests.metrics.groups.AbgroupMetric;
import com.moonmana.moontrack.abtests.metrics.groups.AbgroupTotalRevenue;
import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;

import hive.model.abtest.AbTest;
import hive.model.abtest.AbTestGroup;

public class AbtestTotalRevenue extends AbtestMetric {

	public AbtestTotalRevenue(AbTest abtest) {
		super(abtest);
		setType(MetricType.REVENUE_TOTAL);
	}


	@Override
	protected AbgroupMetric getSubquery(AbTestGroup group) {
		return new AbgroupTotalRevenue(group.getId(), abtest.getStartDate(), abtest.getFinishDate());
	}

	@Override
	protected Chart getChartInstance() {
		return new AbtestChartTotalRevenue(this, abtest);
	}

}
