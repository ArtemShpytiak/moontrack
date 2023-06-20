package com.moonmana.moontrack.abtests.metrics.groups;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;

public class AbgroupARPDAU extends AbgroupMetric {

	public AbgroupARPDAU(int group) {
		super(group, null, null);
		setType(MetricType.ARPDAU);
	}

	@Override
	public String getSQL() {
		return
		// @formatter:off
			" SELECT activities.date, sumprice / COUNT(DISTINCT activities.d_user)"
			+ " FROM activities"
			+ " INNER JOIN ("
			+ " SELECT date_trunc('day', iaps.date) AS day, SUM(price) AS sumprice"
			+ " FROM iaps"
			+ " WHERE iaps.r_user"
			+ " IN (SELECT abtest_users.realm_user FROM abtest_users WHERE abtest_users.group_id = " + group + ")"
			+ " AND iaps.sandbox = false"
			+ " GROUP BY day"
			+ " ) AS iaps ON day = activities.date"
			+ " GROUP BY activities.date, sumprice"
			+ " ORDER BY activities.date DESC"
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
	public Object getValue(int index) {
		return String.format(Locale.US, "%.2f", (Double) rows.get(index)[1]);
	}

	@Override
	protected Chart getChartInstance() {
		return null;
	}

}
