package com.moonmana.moontrack.abtests.metrics.groups;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;

public class AbgroupDPU extends AbgroupMetric {

	public AbgroupDPU(int group, Date from, Date to) {
		super(group, from, to);
		setType(MetricType.DPU);
	}

	@Override
	public String getSQL() {
		return
		// @formatter:off
			" SELECT date_trunc('day', iaps.date) AS day, COUNT(DISTINCT iaps.r_user)"
			+ " FROM iaps"
			+ " WHERE iaps.r_user"
			+ " IN (SELECT abtest_users.realm_user FROM abtest_users WHERE abtest_users.group_id = " + group + ")"
			+ " AND iaps.sandbox = false"
			+ " AND iaps.date BETWEEN '" + from + "' AND '" + to + "'"
			+ " GROUP BY day"
			+ " ORDER BY day DESC"
			+ " LIMIT " + getLimit();
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
