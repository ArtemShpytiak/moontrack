package com.moonmana.moontrack.abtests.metrics.groups;

import com.moonmana.PlatformID;
import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;

public class AbgroupPlatforms extends AbgroupMetric {

	public AbgroupPlatforms(int group) {
		super(group, null, null);
		setType(MetricType.PLATFORMS);
	}

	@Override
	public String getSQL() {
		return
		// @formatter:off
			" SELECT d_user_registrations.platform_id, COUNT(d_user_registrations.platform_id) as count"
			+ " FROM d_user_registrations"
			+ " WHERE d_user_registrations.id"
			+ " IN (SELECT realm_users.d_user FROM realm_users WHERE realm_users.id"
			+ " IN (SELECT abtest_users.realm_user FROM abtest_users WHERE abtest_users.group_id = " + group + "))"
			+ " GROUP BY d_user_registrations.platform_id"
			+ " ORDER BY count DESC";
		// @formatter:on
	}

	@Override
	public String getKey(int index) {
		if (rows.get(index)[0] == null) {
			return super.getKey(index);
		}
		Short osId = (Short) rows.get(index)[0];
		return PlatformID.getPlatformName(osId.byteValue());
	}

	@Override
	protected Chart getChartInstance() {
		return null;
	}

}
