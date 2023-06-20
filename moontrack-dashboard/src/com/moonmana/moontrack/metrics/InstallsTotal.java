package com.moonmana.moontrack.metrics;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;
import com.moonmana.moontrack.metric.MetricWithFilters;
import com.moonmana.moontrack.metrics.charts.InstallsTotalChart;

import hive.model.filter.FilterHub;

public class InstallsTotal extends MetricWithFilters {

	public InstallsTotal(FilterHub filter) {
		super(filter);
		setType(MetricType.INSTALLS_TOTAL);
	}

	@Override
	public String getSQL() {
		return
		// @formatter:off
				" SELECT MAX(registration_date), CAST(COUNT(*) AS INTEGER)" +
				" FROM d_user_registrations" +
				" WHERE TRUE" +
					getGameWhereClause() +
					getOsesWhereClause() +
					getPlatformsWhereClause();
		// @formatter:on
	}

	@Override
	public Integer getValue(int index) {
		if (isValueNull(index)) {
			return 0;
		}
		return (Integer) rows.get(index)[1];
	}

	@Override
	protected Chart getChartInstance() {
		return new InstallsTotalChart(this);
	}
}
