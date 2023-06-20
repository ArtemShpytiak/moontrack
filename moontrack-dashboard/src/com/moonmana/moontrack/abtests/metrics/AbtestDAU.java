package com.moonmana.moontrack.abtests.metrics;

import com.moonmana.moontrack.abtests.charts.AbtestChartDau;
import com.moonmana.moontrack.abtests.metrics.groups.AbgroupDAU;
import com.moonmana.moontrack.abtests.metrics.groups.AbgroupMetric;
import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;

import hive.model.abtest.AbTest;
import hive.model.abtest.AbTestGroup;

public class AbtestDAU extends AbtestMetric {

	public AbtestDAU(AbTest abtest) {
		super(abtest);
		setType(MetricType.DAU);
	}

	@Override
	protected AbgroupMetric getSubquery(AbTestGroup group) {
		return new AbgroupDAU(group.getId(), abtest.getStartDate(), abtest.getFinishDate());
	}

	@Override
	protected Chart getChartInstance() {
		return new AbtestChartDau(this, abtest);
	}

}
