package com.moonmana.moontrack.metrics;

import java.util.Locale;

import org.hibernate.Session;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.Metric;
import com.moonmana.moontrack.metric.MetricType;
import com.moonmana.moontrack.metrics.charts.ConversionChart;

import hive.model.filter.FilterHub;

public class Conversion extends Metric {

	private Dau dau;
	private Dpu dpu;

	public Conversion(FilterHub filter) {
		setFilter(filter);
		setType(MetricType.CONVERSION);
	}

	public void performQuery(Session session) {
		dau = new Dau(getFilter());
		dpu = new Dpu(getFilter());

		addSubmetric(dau);
		addSubmetric(dpu);

		performSubqueries(session);
	}

	@Override
	public int size() {
		return Math.min(dau.size(), dpu.size());
	}

	@Override
	public boolean isKeyNull(int index) {
		return dau.isKeyNull(index) || dpu.isKeyNull(index);
	}

	@Override
	public String getKey(int index) {
		return dau.getKey(index);
	}

	@Override
	public Object getValue(int index) {
		Integer dpuValue = (Integer) dpu.getValue(index);
		Integer dauValue = (Integer) dau.getValue(index);

		if (dauValue.intValue() == 0) {
			return 0.0;
		}
		float conversion = dpuValue.floatValue() / dauValue.floatValue();
		return String.format(Locale.US, "%.2f", conversion);
	}

	@Override
	protected Chart getChartInstance() {
		return new ConversionChart(this);
	}

}
