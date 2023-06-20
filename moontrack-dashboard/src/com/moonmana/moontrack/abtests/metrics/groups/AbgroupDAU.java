package com.moonmana.moontrack.abtests.metrics.groups;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;

public class AbgroupDAU extends AbgroupMetric {

	public AbgroupDAU(int group, Date from, Date to) {
		super(group, from, to);
		setType(MetricType.DAU);
	}

	@Override
	public String getSQL() {
		return
		// @formatter:off
			" SELECT activities.date, COUNT(activities.d_user)"
			+ " FROM activities"
			+ " WHERE activities.d_user"
			+ " IN (SELECT realm_users.d_user FROM realm_users WHERE realm_users.id"
			+ " IN (SELECT abtest_users.realm_user FROM abtest_users WHERE abtest_users.group_id = " + group + "))"
			+ " AND activities.date BETWEEN '" + from + "' AND '" + to + "'"
			+ " GROUP BY activities.date"
			+ " ORDER BY activities.date DESC"
			+ " LIMIT " + getLimit()
			+ " OFFSET 1";
		// @formatter:on
	}

	@Override
	public String getKey(int index) {
		Date date = (Date) rows.get(index)[0];
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	@Override
	protected Chart getChartInstance() {
		return null;
	}

}
