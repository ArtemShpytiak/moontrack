package com.moonmana.moontrack.controllers;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.moonmana.app.App;
import com.moonmana.log.Log;
import com.moonmana.moontrack.abtests.metrics.AbtestMetric;
import com.moonmana.moontrack.abtests.metrics.AbtestMetricFactory;
import com.moonmana.moontrack.common.utils.DateUtil;
import com.moonmana.moontrack.dto.DTOStorage;
import com.moonmana.moontrack.metric.MetricConfig;
import com.moonmana.moontrack.metric.MetricType;
import com.moonmana.moontrack.repositories.DashboardDAO;

import hive.model.abtest.AbTest;
import hive.model.abtest.AbTestGroupByFilter;
import hive.model.abtest.AbTestGroupByPercentage;
import hive.model.abtest.AbTestGroupBySegment;
import hive.model.filter.FilterCountry;
import hive.model.filter.FilterDevice;
import hive.model.filter.FilterHub;
import hive.model.filter.FilterOs;
import hive.model.filter.FilterPlatform;
import hive.model.filter.FilterRealm;
import hive.model.segment.Segment;
import moontrack.events.abtest.AbtestAddedEvent;
import moontrack.events.abtest.AbtestArchivedEvent;

@Controller
@RequestMapping("abtests")
public class AbtestsController {

	@Autowired
	private DashboardDAO dashboardDAO;

	@Autowired
	private GamesController gamesController;

	@PostMapping("")
	ModelAndView root() {
		ModelAndView mv = new ModelAndView("abtests/abtests");
		mv.addObject("games", gamesController.getAbtestGames());
		return mv;
	}

	@PostMapping("{gameId}/add")
	ModelAndView addView(@PathVariable("gameId") short gameId) {
		ModelAndView mv = new ModelAndView("abtests/add/abtests.add");
		mv.addObject("game", dashboardDAO.getGameById(gameId));
		mv.addObject("segments", dashboardDAO.getSegmentsByGame(gameId));
		mv.addObject("countries", DTOStorage.getCountries());
		mv.addObject("oses", DTOStorage.getOses());
		mv.addObject("platforms", DTOStorage.getPlatforms());
		// mv.addObject("devices", DTOStorage.getDevices());
		// mv.addObject("realms", DTOStorage.getRealms());
		return mv;
	}

	@PostMapping("{gameId}/add/submit")
	ResponseEntity<String> addSubmit(@PathVariable short gameId, HttpEntity<String> httpEntity) {

		JSONObject abtestJson = new JSONObject(httpEntity.getBody()).getJSONObject("abtest");
		AbTest abtest = new AbTest();
		abtest.setName(abtestJson.getString("name"));
		abtest.setGameId(gameId);
		abtest.setStartDate(DateUtil.getStartDate(abtestJson.getString("activeness")));
		abtest.setFinishDate(DateUtil.getFinishDate(abtestJson.getString("activeness")));

		parseGroupByPercentage(abtest, abtestJson.getJSONArray("groupsPercent"));
		parseGroupByFilter(abtest, abtestJson.getJSONArray("groupsFilter"));
		parseGroupBySegment(abtest, abtestJson.getJSONArray("groupsSegment"));

		dashboardDAO.addAbtest(abtest);
		dashboardDAO.saveAbtestCaches(abtest.getId());

		App.instance().eventProcessor().proccess(new AbtestAddedEvent(abtest));

		return new ResponseEntity<>("", HttpStatus.OK);
	}

	private void parseGroupBySegment(AbTest abtest, JSONArray jsonArray) {
		for (int i = 0; i < jsonArray.length(); i++) {
			int id = Integer.parseInt(jsonArray.getJSONObject(i).getString("id"));

			AbTestGroupBySegment groupBySegment = new AbTestGroupBySegment();
			Segment segment = dashboardDAO.getSegment(id, false);
			groupBySegment.setAbtest(abtest);
			groupBySegment.setSegment(segment);
			groupBySegment.setName(segment.getName());
			abtest.getGroups().add(groupBySegment);
		}
	}

	private void parseGroupByFilter(AbTest abtest, JSONArray jsonArray) {
		for (int i = 0; i < jsonArray.length(); i++) {
			AbTestGroupByFilter groupByFilter = new AbTestGroupByFilter();
			groupByFilter.setAbtest(abtest);
			abtest.getGroups().add(groupByFilter);

			FilterHub filter = new FilterHub();
			groupByFilter.seFilter(filter);

			JSONObject filterJson = jsonArray.getJSONObject(i);
			groupByFilter.setName(filterJson.getString("name"));
			filter.setGameId(abtest.getGameId());
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

			JSONArray countries = filterJson.getJSONArray("countries");
			JSONArray oses = filterJson.getJSONArray("oses");
			JSONArray devices = filterJson.getJSONArray("devices");
			JSONArray realms = filterJson.getJSONArray("realms");
			JSONArray platforms = filterJson.getJSONArray("platforms");

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
		}
	}

	private void parseGroupByPercentage(AbTest abtest, JSONArray jsonArray) {
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			AbTestGroupByPercentage groupByPercentage = new AbTestGroupByPercentage();
			groupByPercentage.setName(jsonObject.getString("name"));
			groupByPercentage.setPercent(Float.parseFloat(jsonObject.getString("percent")));
			groupByPercentage.setAbtest(abtest);
			abtest.getGroups().add(groupByPercentage);
		}
	}

	@PostMapping("edit/{abtestId}")
	ModelAndView getEdit(@PathVariable int abtestId) {
		AbTest abtest = dashboardDAO.getAbtest(abtestId, false);

		ModelAndView mv = new ModelAndView("abtests/edit/abtests.edit");
		mv.addObject("game", dashboardDAO.getGameById(abtest.getGameId()));
		mv.addObject("abtest", abtest);
		// mv.addObject("segmentList", game.getSegments());
		// mv.addObject("selectedSegment", abtest.getSegment());
		return mv;
	}

	@PostMapping("edit/{abtestId}/submit")
	ResponseEntity<String> postEdit(@PathVariable int abtestId, @RequestParam("abtestname") String name,
			@RequestParam("percentage") String percentage,
			@RequestParam(value = "segment", required = false) String segment,
			@RequestParam(value = "activeness", required = false) String activeness) {

		// Game game = getUser().getCompany().getGameById(gameId);

		return new ResponseEntity<String>("", HttpStatus.OK);
	}

	@PostMapping("view/{abtestId}")
	ModelAndView view(@PathVariable int abtestId) {
		ModelAndView mv = new ModelAndView("abtests/view/abtests.view");
		mv.addObject("abtest", dashboardDAO.getAbtest(abtestId, true));
		mv.addObject("metrics", DTOStorage.getAbtestMetrics());
		mv.addObject("countries", DTOStorage.getCountries());
		mv.addObject("oses", DTOStorage.getOses());
		mv.addObject("platforms", DTOStorage.getPlatforms());
		// mv.addObject("devices", DTOStorage.getDevices());
		// mv.addObject("realms", DTOStorage.getRealms());
		mv.addObject("elementids", MetricConfig.getAbtestMetricMarkups().values());
		return mv;
	}

	@PostMapping("archive/{abtestId}")
	ModelAndView archive(@PathVariable int abtestId) {
		ModelAndView mv = new ModelAndView("abtests/archive/abtests.archive");
		mv.addObject("abtest", dashboardDAO.getAbtest(abtestId, false));
		return mv;
	}

	@PostMapping("archive/{abtestId}/submit")
	ResponseEntity<String> archivate(@PathVariable int abtestId) {

		dashboardDAO.archivateAbtest(abtestId);

		App.instance().eventProcessor().proccess(new AbtestArchivedEvent(abtestId));

		return new ResponseEntity<>("", HttpStatus.OK);
	}

	@PostMapping(value = "metrics/get/{abtestId}/{metricName}", consumes = { "text/plain",
			"application/*" }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	ResponseEntity<String> getChart(@PathVariable int abtestId, @PathVariable String metricName) {

		AbTest abtest = dashboardDAO.getAbtest(abtestId, true);
		byte metricType = MetricType.getTypeByName(metricName);

		Log.out(metricName);

		AbtestMetric metric = AbtestMetricFactory.getAbtestMetric(metricType, abtest);
		metric.performQuery(dashboardDAO);
		JSONObject jsoned = metric.getChart().toJson();

		Log.out(jsoned.toString());

		return new ResponseEntity<>(jsoned.toString(), HttpStatus.OK);
	}

	@PostMapping(value = "widget/add/{widgetId}")
	ModelAndView removeWidget(@PathVariable int widgetId) {
		ModelAndView mv = new ModelAndView();
		// TODO
		return mv;
	}

}
