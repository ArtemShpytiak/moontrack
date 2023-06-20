package com.moonmana.moontrack.chart;

import org.json.JSONArray;
import org.json.JSONObject;

import com.moonmana.log.Log;
import com.moonmana.moontrack.metric.Metric;

public abstract class Chart {

	public static final char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

	protected JSONArray data = new JSONArray();
	protected JSONObject config = new JSONObject();
	protected JSONObject chart = new JSONObject();
	protected Metric metric;

	public Chart(Metric metric) {
		this.metric = metric;
	}

	public JSONObject toJson() {
		config.put("data", data);

		chart.put("metricName", getTitle());
		chart.put("chartType", getType());
		chart.put("chart", config);
		chart.put("widgetId", metric.getWidgetId());

		return chart;
	}

	public void formJsonConfig() {
		config.put("element", getElement());
		config.put("resize", true);
		config.put("xkey", "y");

		JSONArray ykeys = new JSONArray();
		ykeys.put("a");
		config.put("ykeys", ykeys);

		JSONArray labels = new JSONArray();
		labels.put(getTitle());
		config.put("labels", labels);
		config.put("lineColors", new JSONArray().put("#3c8dbc"));
		config.put("hideHover", "auto");
		config.put("smooth", false);
	}

	public void formJsonData() {
		for (int i = 0; i < metric.size(); i++) {
			JSONObject prop = new JSONObject();
			if (!metric.isKeyNull(i)) {
				prop.put("y", metric.getKey(i));
				prop.put("a", metric.getValue(i));
			}

			data.put(prop);
		}
		Log.out(data.toString());
	}

	protected abstract String getTitle();

	protected abstract String getType();

	protected abstract String getElement();

	public void setData(JSONArray data) {
		this.data = data;
	}

	public void setData(String data) {
		this.data = new JSONArray(data);
	}

	public JSONArray getData() {
		return data;
	}

}
