package com.moonmana.moontrack.abtests.metrics.groups;

import java.util.Date;

import com.moonmana.moontrack.metric.Metric;

public abstract class AbgroupMetric extends Metric {

	protected int group;
	protected Date from;
	protected Date to;

	public AbgroupMetric(int group, Date from, Date to) {
		this.group = group;
		this.from = from;
		this.to = to;
	}

}
