package com.moonmana.moontrack.abtests.charts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import com.moonmana.log.Log;
import com.moonmana.moontrack.abtests.metrics.AbtestMetric;
import com.moonmana.moontrack.abtests.metrics.groups.AbgroupMetric;
import com.moonmana.moontrack.chart.Chart;

import hive.model.abtest.AbTest;

public abstract class AbtestChart extends Chart {

	protected AbTest abtest;

	public AbtestChart(AbtestMetric metric, AbTest abtest) {
		super(metric);
		this.abtest = abtest;
	}

	@Override
	public void formJsonConfig() {
		config.put("element", getElement());
		config.put("resize", true);
		config.put("xkey", "y");

		JSONArray ykeys = new JSONArray();
		for (int i = 0; i < abtest.getGroups().size(); i++) {
			ykeys.put(String.valueOf(alphabet[i]));
		}
		config.put("ykeys", ykeys);

		JSONArray labels = new JSONArray();
		abtest.getGroups().forEach(g -> labels.put(g.getName()));
		config.put("labels", labels);
		config.put("lineColors", new JSONArray().put("#3c8dbc"));
		config.put("hideHover", "auto");
		config.put("smooth", false);
	}

	@Override
	public void formJsonData() {

		Map<String, Map<String, Object>> objects = new HashMap<String, Map<String, Object>>();

		List<AbgroupMetric> groups = ((AbtestMetric) metric).getGroups();
		for (int k = 0; k < groups.size(); k++) {

			AbgroupMetric abgroupMetric = groups.get(k);

			for (int i = 0; i < abgroupMetric.size(); i++) {
				if (abgroupMetric.isKeyNull(i)) {
					continue;
				}
				String key = abgroupMetric.getKey(i);
				Map<String, Object> prop = new HashMap<String, Object>();
				prop.put("y", key);

				objects.put(key, prop);
			}
		}
		for (Entry<String, Map<String, Object>> entry : objects.entrySet()) {
			for (int k = 0; k < groups.size(); k++) {
				entry.getValue().put(String.valueOf(alphabet[k]), 0);
			}
		}

		Log.out(objects.toString());

		for (int k = 0; k < groups.size(); k++) {
			AbgroupMetric abgroupMetric = groups.get(k);

			for (int i = 0; i < abgroupMetric.size(); i++) {
				if (abgroupMetric.isKeyNull(i)) {
					continue;
				}
				String key = abgroupMetric.getKey(i);

				if (objects.containsKey(key)) {
					Map<String, Object> prop = objects.get(key);
					prop.put(String.valueOf(alphabet[k]), abgroupMetric.getValue(i));
				} else {
					Map<String, Object> prop = new HashMap<String, Object>();
					prop.put("y", key);
					prop.put(String.valueOf(alphabet[k]), abgroupMetric.getValue(i));

					objects.put(key, prop);
				}
			}
		}

		List<String> keys = new ArrayList<String>(objects.keySet());

		keys.forEach(key -> {
			Map<String, Object> objs = objects.get(key);

			JSONObject prop = new JSONObject();

			for (Map.Entry<String, Object> e : objs.entrySet()) {
				prop.put(e.getKey(), e.getValue());
			}
			data.put(prop);
		});

		Log.out(data.toString());
	}

}
