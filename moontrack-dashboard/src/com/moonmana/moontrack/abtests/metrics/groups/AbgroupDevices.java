package com.moonmana.moontrack.abtests.metrics.groups;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;

public class AbgroupDevices extends AbgroupMetric {

	public AbgroupDevices(int group) {
		super(group, null, null);
		setType(MetricType.DEVICES);
	}

	@Override
	public String getSQL() {
		return
		// @formatter:off
			" SELECT d_user_registrations.device, COUNT(d_user_registrations.device) as count"
			+ " FROM d_user_registrations"
			+ " WHERE d_user_registrations.id"
			+ " IN (SELECT realm_users.d_user FROM realm_users WHERE realm_users.id"
			+ " IN (SELECT abtest_users.realm_user FROM abtest_users WHERE abtest_users.group_id = " + group + "))"
			+ " GROUP BY d_user_registrations.device"
			+ " ORDER BY count DESC"
			+ " LIMIT " + getLimit();
		// @formatter:on
	}

	@Override
	protected Chart getChartInstance() {
		return null;
	}
}
