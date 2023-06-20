package com.moonmana.moontrack.metrics;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricWithFilters;

import hive.model.filter.FilterHub;

public class Arpu extends MetricWithFilters {

	public Arpu(FilterHub filter) {
		super(filter);
		
	}

	@Override
	protected Chart getChartInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSQL() {
		// TODO Auto-generated method stub
		return super.getSQL();
	}

}
