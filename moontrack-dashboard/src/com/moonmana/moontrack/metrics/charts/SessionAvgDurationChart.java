package com.moonmana.moontrack.metrics.charts;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.chart.ChartType;
import com.moonmana.moontrack.metric.Metric;

public class SessionAvgDurationChart extends Chart {

	public SessionAvgDurationChart(Metric metric) {
		super(metric);
	}

	@Override
	public void formJsonConfig() {
		super.formJsonConfig();

		config.put("yLabelFormat", "(value) => { const sec = parseInt(value, 10);"
				+ "    let hours   = Math.floor(sec / 3600);"
				+ "    let minutes = Math.floor((sec - (hours * 3600)) / 60);"
				+ "    let seconds = sec - (hours * 3600) - (minutes * 60);"
				+ "    if (hours   < 10) {hours   = '0'+hours;}" + "    if (minutes < 10) {minutes = '0'+minutes;}"
				+ "    if (seconds < 10) {seconds = '0'+seconds;}" + "    return hours+':'+minutes+':'+seconds; }");
	}

	@Override
	protected String getTitle() {
		return "Average Session Duration";
	}

	@Override
	protected String getType() {
		return ChartType.LINE;
	}

	@Override
	protected String getElement() {
		return "chart-" + metric.getWidgetId();
	}

}
