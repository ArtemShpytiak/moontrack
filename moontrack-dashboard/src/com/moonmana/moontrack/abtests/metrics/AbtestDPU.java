package com.moonmana.moontrack.abtests.metrics;

import com.moonmana.moontrack.abtests.charts.AbtestChartDpu;
import com.moonmana.moontrack.abtests.metrics.groups.AbgroupDPU;
import com.moonmana.moontrack.abtests.metrics.groups.AbgroupMetric;
import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;

import hive.model.abtest.AbTest;
import hive.model.abtest.AbTestGroup;

public class AbtestDPU extends AbtestMetric {

	public AbtestDPU(AbTest abtest) {
		super(abtest);
		setType(MetricType.DPU);
	}

	@Override
	protected AbgroupMetric getSubquery(AbTestGroup group) {
		return new AbgroupDPU(group.getId(), abtest.getStartDate(), abtest.getFinishDate());
	}

	@Override
	protected Chart getChartInstance() {
		return new AbtestChartDpu(this, abtest);
	}

}
