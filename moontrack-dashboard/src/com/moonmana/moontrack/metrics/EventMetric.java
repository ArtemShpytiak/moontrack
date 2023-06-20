package com.moonmana.moontrack.metrics;

import com.moonmana.moontrack.abtests.charts.EventChart;
import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricWithFilters;

import hive.model.filter.FilterHub;

public class EventMetric extends MetricWithFilters {

	private String eventName;
	private short gameId;

	@Override
	public String getSQL() {
		String prefix = "";
		switch (gameId) {
		case 1:
			prefix = "poe";
			break;
		case 3:
			prefix = "ld";
			break;
		case 5:
			prefix = "up";
			break;
		default:
			break;
		}
		String table = prefix + "_" + eventName;
		return
		// @formatter:off
				"SELECT date_trunc('day', date) AS day, COUNT(*)"
				+ " FROM " + table
				+ " WHERE TRUE"
				+ getDateRange("date")
				+ " GROUP BY day"
				+ " ORDER BY day DESC"
				+ " LIMIT " + getLimit();
		// @formatter:on
	}

	public EventMetric() {
		super(new FilterHub());
	}

	public EventMetric(FilterHub filter) {
		super(filter);
		eventName = filter.getEventName();
		gameId = filter.getGameId();
		setType(type);
	}

	@Override
	protected Chart getChartInstance() {
		return new EventChart(this);
	}
}
