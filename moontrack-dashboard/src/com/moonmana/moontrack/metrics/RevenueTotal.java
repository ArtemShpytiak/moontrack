package com.moonmana.moontrack.metrics;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;
import com.moonmana.moontrack.metric.MetricWithFilters;

import hive.model.filter.FilterHub;

public class RevenueTotal extends MetricWithFilters {

	public RevenueTotal(FilterHub filter) {
		super(filter);
		setType(MetricType.REVENUE_TOTAL);
	}

	@Override
	public String getSQL() {
		return
		// @formatter:off
				" SELECT MAX(iaps.date), CAST(SUM(iaps.price) AS DOUBLE PRECISION)" +
				" FROM iaps" +
				getFromTables() +
				" WHERE sandbox = FALSE" +
				getWhereClause() +
				getGameWhereClause() +
				getPlatformsWhereClause() +
				getOsesWhereClause();
		// @formatter:on
	}

	private String getWhereClause() {
		if (filter.getGameId() != null || !filter.getPlatforms().isEmpty() || !filter.getOses().isEmpty()) {
			return " AND iaps.r_user = realm_users.id AND realm_users.d_user = d_user_registrations.id";
		}
		return "";
	}

	@Override
	protected String getFromTables() {
		if (filter.getGameId() != null || !filter.getPlatforms().isEmpty() || !filter.getOses().isEmpty()) {
			return ", d_user_registrations, realm_users";
		}
		return "";
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

	@Override
	public Double getValue(int index) {
		if (isValueNull(index)) {
			return 0.0;
		}
		return (Double) super.getValue(index);
	}
}
