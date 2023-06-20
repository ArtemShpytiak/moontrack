package com.moonmana.moontrack.abtests.metrics.groups;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;

public class AbgroupUsers extends AbgroupMetric {

	public AbgroupUsers(int group) {
		super(group, null, null);
		setType(MetricType.USERS);
	}

	@Override
	public String getSQL() {
		return " SELECT group_id, COUNT(*) FROM abtest_users WHERE group_id =" + group + " GROUP BY group_id";
	}

	@Override
	protected Chart getChartInstance() {
		return null;
	}

}
