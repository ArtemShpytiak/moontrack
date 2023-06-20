package com.moonmana.moontrack.abtests.metrics.groups;

import com.moonmana.OS;
import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;

public class AbgroupOses extends AbgroupMetric {

	public AbgroupOses(int group) {
		super(group, null, null);
		setType(MetricType.OSES);
	}

	@Override
	public String getSQL() {
		return
		// @formatter:off
			" SELECT d_user_registrations.os_id, COUNT(d_user_registrations.os_id) as count"
			+ " FROM d_user_registrations"
			+ " WHERE d_user_registrations.id"
			+ " IN (SELECT realm_users.d_user FROM realm_users WHERE realm_users.id"
			+ " IN (SELECT abtest_users.realm_user FROM abtest_users WHERE abtest_users.group_id = " + group + "))"
			+ " GROUP BY d_user_registrations.os_id"
			+ " ORDER BY count DESC";
		// @formatter:on
	}

	@Override
	public String getKey(int index) {
		if (rows.get(index)[0] == null) {
			return super.getKey(index);
		}
		Short osId = (Short) rows.get(index)[0];
		return OS.getOsName(osId.byteValue());
	}

	@Override
	protected Chart getChartInstance() {
		return null;
	}

}
