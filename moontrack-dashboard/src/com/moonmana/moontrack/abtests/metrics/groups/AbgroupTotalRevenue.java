package com.moonmana.moontrack.abtests.metrics.groups;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;

public class AbgroupTotalRevenue extends AbgroupMetric {

	public AbgroupTotalRevenue(int group, Date from, Date to) {
		super(group, from, to);
		setType(MetricType.REVENUE_TOTAL);
	}

	@Override
	public String getSQL() {
		return
		// @formatter:off
			" SELECT MAX(iaps.date), SUM(iaps.price)"
			+ " FROM iaps"
			+ " WHERE iaps.sandbox = FALSE"
			+ " AND iaps.r_user"
			+ " IN (SELECT abtest_users.realm_user FROM abtest_users WHERE abtest_users.group_id = " + group + ")"
			+ " AND iaps.date BETWEEN '" + from + "' AND '" + to + "'";
		// @formatter:on
	}

	@Override
	public String getKey(int index) {
		if (rows.get(index)[0] == null) {
			return super.getKey(index);
		}
		Date date = (Date) rows.get(index)[0];
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	@Override
	public Object getValue(int index) {
		return String.format(Locale.US, "%.2f", (Float) rows.get(index)[1]);
	}

	@Override
	protected Chart getChartInstance() {
		return null;
	}

}
