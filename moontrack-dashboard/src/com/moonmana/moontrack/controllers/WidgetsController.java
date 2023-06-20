package com.moonmana.moontrack.controllers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.moonmana.log.Log;
import com.moonmana.moontrack.common.utils.DateUtil;
import com.moonmana.moontrack.model.Widget;
import com.moonmana.moontrack.repositories.DashboardDAO;
import com.moonmana.moontrack.repositories.HiveDAO;

import hive.model.filter.FilterCountry;
import hive.model.filter.FilterDevice;
import hive.model.filter.FilterHub;
import hive.model.filter.FilterOs;
import hive.model.filter.FilterPlatform;
import hive.model.filter.FilterRealm;

@RestController
@RequestMapping("widgets")
public class WidgetsController {

	@Autowired
	private DashboardDAO dashboardDAO;

	@PostMapping("dashboard/get")
	@ResponseBody
	ResponseEntity<String> getAllCharts() {
		JSONArray charts = dashboardDAO.getDashboardWidgetCharts();

		return new ResponseEntity<>(charts.toString(), HttpStatus.OK);
	}

	@PostMapping(value = "dashboard/add", consumes = { "text/plain",
			"application/*" }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	ResponseEntity<String> addWidget(HttpEntity<String> httpEntity) {

		Log.out("JSON: " + httpEntity.getBody());

		JSONObject filterJson = new JSONObject(httpEntity.getBody()).getJSONObject("filter");
		Byte metricType = Byte.valueOf(filterJson.getNullableString("selectedMetricType"));
		long tempMetricId = filterJson.getLong("tempMetricId");
		Short gameId = Short.valueOf(filterJson.getNullableString("game"));
		JSONArray countries = filterJson.getJSONArray("countries");
		JSONArray oses = filterJson.getJSONArray("oses");
		JSONArray devices = filterJson.getJSONArray("devices");
		JSONArray realms = filterJson.getJSONArray("realms");
		JSONArray platforms = filterJson.getJSONArray("platforms");

		FilterHub filter = new FilterHub();
		filter.setGameId(gameId);

		if (!filterJson.isNull("paying")) {
			boolean paying = filterJson.getBoolean("paying");
			filter.setPaying(paying);

			if (paying) {
				if (!filterJson.optString("moneyfrom").isEmpty()) {
					filter.setSpentMoneyFrom(Float.parseFloat(filterJson.getString("moneyfrom")));
				}
				if (!filterJson.optString("moneyto").isEmpty()) {
					filter.setSpentMoneyTo(Float.parseFloat(filterJson.getString("moneyto")));
				}
			}
		}
		if (!filterJson.optString("registration").isEmpty()) {
			filter.setRegistrationDateFrom(DateUtil.getStartDate(filterJson.getString("registration")));
		}
		if (!filterJson.optString("registration").isEmpty()) {
			filter.setRegistrationDateTo(DateUtil.getFinishDate(filterJson.getString("registration")));
		}

		for (int k = 0; k < countries.length(); k++) {
			String country = countries.getString(k);
			filter.getCountries().add(new FilterCountry(country));
		}
		for (int k = 0; k < oses.length(); k++) {
			filter.getOses().add(new FilterOs(Byte.parseByte(oses.getString(k))));
		}
		for (int k = 0; k < devices.length(); k++) {
			filter.getDevices().add(new FilterDevice(devices.getString(k)));
		}
		for (int k = 0; k < realms.length(); k++) {
			filter.getRealms().add(new FilterRealm(Integer.parseInt(realms.getString(k))));
		}
		for (int k = 0; k < platforms.length(); k++) {
			filter.getPlatforms().add(new FilterPlatform(Byte.parseByte(platforms.getString(k))));
		}

		Widget widget = new Widget();
		widget.setMetricType(metricType);
		widget.setFilter(filter);
		widget.initMetric();

		dashboardDAO.addDashboardWidget(widget, filter);
		HiveDAO.performQuery(widget.getMetric());

		JSONObject json = widget.getMetric().getChart().toJson();
		json.putOnce("widgetId", widget.getId());

		widget.setCache(json.toString());
		dashboardDAO.updateDashWidget(widget);

		json.putOnce("tempMetricId", tempMetricId);

		Log.out("Response: " + json);

		return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
	}

	@PostMapping("dashboard/remove/{widgetId}")
	@ResponseBody
	ResponseEntity<String> getAll(HttpEntity<String> httpEntity, @PathVariable int widgetId) {
		dashboardDAO.deleteDashboardWidget(widgetId);
		return new ResponseEntity<>("", HttpStatus.OK);
	}
}
