package com.moonmana.moontrack.controllers;

import java.util.List;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.moonmana.log.Log;
import com.moonmana.moontrack.common.utils.DateUtil;
import com.moonmana.moontrack.dto.DTOStorage;
import com.moonmana.moontrack.dto.DashboardCacheDTO;
import com.moonmana.moontrack.metric.MetricType;
import com.moonmana.moontrack.model.Game;
import com.moonmana.moontrack.model.GameWidget;
import com.moonmana.moontrack.model.Widget;
import com.moonmana.moontrack.repositories.DashboardDAO;
import com.moonmana.moontrack.repositories.HiveDAO;

import hive.model.filter.FilterCountry;
import hive.model.filter.FilterDevice;
import hive.model.filter.FilterHub;
import hive.model.filter.FilterOs;
import hive.model.filter.FilterPlatform;
import hive.model.filter.FilterRealm;

@Controller
@RequestMapping("dashboard")
public class DashboardController {

	@Autowired
	private DashboardDAO dashboardDAO;

	@Autowired
	private GamesController gamesController;

	@Autowired
	private EventsController eventsController;

	@PostMapping("")
	public ModelAndView dashboard() {
		List<Game> games = gamesController.getActiveGames();

		ModelAndView mv = new ModelAndView("dash/dash");
		mv.addObject("metrics", DTOStorage.getMetrics());
		mv.addObject("countries", DTOStorage.getCountries());
		mv.addObject("games", games);
		mv.addObject("oses", DTOStorage.getOses());
		mv.addObject("platforms", DTOStorage.getPlatforms());
		// mv.addObject("devices", DTOStorage.getDevices());
		// mv.addObject("realms", DTOStorage.getRealms());
		JSONObject cache = dashboardDAO.getDashboardWidgetMetricCache();

		DashboardCacheDTO dto = new DashboardCacheDTO(cache);
		mv.addObject("dashcache", dto);
		mv.addObject("installs", dto);
		mv.addObject("online", dto);
		mv.addObject("revenue", dto);
		mv.addObject("widgets", dashboardDAO.getDashboardWidgets());

		mv.addObject("eventType", MetricType.EVENT);
		mv.addObject("events", eventsController.getEvents());
		return mv;
	}

	@PostMapping("games/{gameId}")
	public ModelAndView gameDashboard(@PathVariable short gameId) {

		Log.out("dashboard game id : " + gameId);

		Game game = dashboardDAO.getGameById(gameId);

		ModelAndView mv = new ModelAndView("gamedash/gamedash");
		JSONObject cache = dashboardDAO.getGameDashboardWidgetMetricCache(gameId);
		if (!cache.keySet().isEmpty()) {
		
			mv.addObject("metrics", DTOStorage.getMetrics());
			mv.addObject("countries", DTOStorage.getCountries());
			mv.addObject("game", game);
			mv.addObject("oses", DTOStorage.getOses());
			mv.addObject("platforms", DTOStorage.getPlatforms());
			// mv.addObject("devices", DTOStorage.getDevices());
			// mv.addObject("realms", DTOStorage.getRealms());
	
			DashboardCacheDTO dto = new DashboardCacheDTO(cache);
			mv.addObject("dashcache", dto);
			mv.addObject("installs", dto);
			mv.addObject("online", dto);
			mv.addObject("revenue", dto);
		}
		mv.addObject("widgets", dashboardDAO.getGameDashboardWidgets(gameId));
		mv.addObject("eventType", MetricType.EVENT);
		mv.addObject("events", eventsController.getEvents());
		
		return mv;
	}

	@PostMapping("widgets/games/{gameId}/get")
	@ResponseBody
	ResponseEntity<String> getGameCharts(@PathVariable short gameId) {
		JSONArray charts = dashboardDAO.getGameDashboardWidgetCharts(gameId);
		Log.out("Response: " + charts);
		return new ResponseEntity<>(charts.toString(), HttpStatus.OK);
	}

	@PostMapping("widgets/get")
	@ResponseBody
	ResponseEntity<String> getAllCharts() {
		JSONArray charts = dashboardDAO.getDashboardWidgetCharts();
		Log.out("Response: " + charts);
		return new ResponseEntity<>(charts.toString(), HttpStatus.OK);
	}

	@PostMapping(value = "widgets/add", consumes = { "text/plain",
			"application/*" }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	ResponseEntity<String> addWidget(HttpEntity<String> httpEntity) {

		Log.out("Request: " + httpEntity.getBody());

		JSONObject filterJson = new JSONObject(httpEntity.getBody()).getJSONObject("filter");
		Byte metricType = Byte.valueOf(filterJson.getNullableString("selectedMetricType"));
		long tempMetricId = filterJson.getLong("tempMetricId");
		String gameParam = filterJson.getString("game");
		Short gameId = gameParam.isEmpty() || gameParam.equals("-") ? null : Short.valueOf(gameParam);

		FilterHub filter = new FilterHub();
		filter.setGameId(gameId);

		if (!filterJson.optString("event").isEmpty()) {
			short eventId = filterJson.getShort("event");
			filter.setEventId(eventId);
			filter.setEventName(eventsController.getEvents().get(eventId).getName());
		}

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
		if (!filterJson.optString("daterange").isEmpty()) {
			filter.setDateFrom(DateUtil.getStartDate(filterJson.getString("daterange")));
		}
		if (!filterJson.optString("daterange").isEmpty()) {
			filter.setDateTill(DateUtil.getFinishDate(filterJson.getString("daterange")));
		}
		if (!filterJson.optString("countries").isEmpty()) {
			JSONArray countries = filterJson.getJSONArray("countries");
			for (int k = 0; k < countries.length(); k++) {
				String country = countries.getString(k);
				filter.getCountries().add(new FilterCountry(country));
			}
		}
		if (!filterJson.optString("oses").isEmpty()) {
			JSONArray oses = filterJson.getJSONArray("oses");
			for (int k = 0; k < oses.length(); k++) {
				filter.getOses().add(new FilterOs(Byte.parseByte(oses.getString(k))));
			}
		}
		if (!filterJson.optString("devices").isEmpty()) {
			JSONArray devices = filterJson.getJSONArray("devices");
			for (int k = 0; k < devices.length(); k++) {
				filter.getDevices().add(new FilterDevice(devices.getString(k)));
			}
		}
		if (!filterJson.optString("realms").isEmpty()) {
			JSONArray realms = filterJson.getJSONArray("realms");
			for (int k = 0; k < realms.length(); k++) {
				filter.getRealms().add(new FilterRealm(Integer.parseInt(realms.getString(k))));
			}
		}
		if (!filterJson.optString("platforms").isEmpty()) {
			JSONArray platforms = filterJson.getJSONArray("platforms");
			for (int k = 0; k < platforms.length(); k++) {
				filter.getPlatforms().add(new FilterPlatform(Byte.parseByte(platforms.getString(k))));
			}
		}

		Widget widget = new Widget();
		widget.setMetricType(metricType);
		widget.setFilter(filter);
		widget.initMetric();

		HiveDAO.performQuery(widget.getMetric());

		widget.getMetric().getChart().formJsonData();

		String cache = widget.getMetric().getChart().getData().toString();
		widget.setCache(cache);
		Log.out("cache: " + cache);

		dashboardDAO.addDashboardWidget(widget, filter);

		widget.getMetric().setWidgetId(widget.getId());
		widget.getMetric().getChart().formJsonConfig();

		JSONObject json = widget.getMetric().getChart().toJson();
		json.putOnce("tempMetricId", tempMetricId);

		return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
	}

	@PostMapping(value = "widgets/games/{gameId}/add", consumes = { "text/plain",
			"application/*" }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	ResponseEntity<String> addGameWidget(@PathVariable short gameId, HttpEntity<String> httpEntity) {

		Log.out("Request: " + httpEntity.getBody());

		JSONObject filterJson = new JSONObject(httpEntity.getBody()).getJSONObject("filter");
		Byte metricType = Byte.valueOf(filterJson.getNullableString("selectedMetricType"));
		long tempMetricId = filterJson.getLong("tempMetricId");
		JSONArray countries = filterJson.getJSONArray("countries");
		JSONArray oses = filterJson.getJSONArray("oses");
		JSONArray devices = filterJson.getJSONArray("devices");
		JSONArray realms = filterJson.getJSONArray("realms");
		JSONArray platforms = filterJson.getJSONArray("platforms");

		FilterHub filter = new FilterHub();
		filter.setGameId(gameId);

		if (!filterJson.optString("event").isEmpty()) {
			short eventId = filterJson.getShort("event");
			filter.setEventId(eventId);
			filter.setEventName(eventsController.getEvents().get(eventId).getName());
		}

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
		if (!filterJson.optString("daterange").isEmpty()) {
			filter.setDateFrom(DateUtil.getStartDate(filterJson.getString("daterange")));
		}
		if (!filterJson.optString("daterange").isEmpty()) {
			filter.setDateTill(DateUtil.getFinishDate(filterJson.getString("daterange")));
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

		GameWidget widget = new GameWidget();
		widget.setMetricType(metricType);
		widget.setFilter(filter);
		widget.initMetric();

		HiveDAO.performQuery(widget.getMetric());

		widget.getMetric().getChart().formJsonData();

		String cache = widget.getMetric().getChart().getData().toString();
		widget.setCache(cache);
		Log.out("cache: " + cache);

		dashboardDAO.addGameDashboardWidget(widget, filter, gameId);

		widget.getMetric().setWidgetId(widget.getId());
		widget.getMetric().getChart().formJsonConfig();

		JSONObject json = widget.getMetric().getChart().toJson();
		json.putOnce("tempMetricId", tempMetricId);
		Log.out("Response: " + json);

		return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
	}

	@PostMapping("widgets/remove/{widgetId}")
	@ResponseBody
	ResponseEntity<String> removeWidget(@PathVariable int widgetId) {
		dashboardDAO.deleteDashboardWidget(widgetId);
		return new ResponseEntity<>("", HttpStatus.OK);
	}

	@PostMapping("widgets/games/{gameId}/remove/{widgetId}")
	@ResponseBody
	ResponseEntity<String> removeGameWidget(@PathVariable short gameId, @PathVariable int widgetId) {
		dashboardDAO.deleteGameDashboardWidget(gameId, widgetId);
		return new ResponseEntity<>("", HttpStatus.OK);
	}

}
