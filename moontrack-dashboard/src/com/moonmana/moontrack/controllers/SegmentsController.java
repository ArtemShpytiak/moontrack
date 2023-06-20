package com.moonmana.moontrack.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.moonmana.app.App;
import com.moonmana.log.Log;
import com.moonmana.moontrack.common.utils.DateUtil;
import com.moonmana.moontrack.dto.CountryDTO;
import com.moonmana.moontrack.dto.DTOStorage;
import com.moonmana.moontrack.dto.OsDTO;
import com.moonmana.moontrack.dto.PlatformDTO;
import com.moonmana.moontrack.metric.MetricType;
import com.moonmana.moontrack.model.Game;
import com.moonmana.moontrack.repositories.DashboardDAO;
import com.moonmana.moontrack.repositories.HiveDAO;
import com.moonmana.moontrack.segments.config.WidgetConfig;
import com.moonmana.moontrack.segments.metrics.SegmentMetric;
import com.moonmana.moontrack.segments.metrics.SegmentMetricFactory;
import com.moonmana.moontrack.segments.metrics.SegmentUsers;

import hive.model.filter.FilterCountry;
import hive.model.filter.FilterHub;
import hive.model.filter.FilterOs;
import hive.model.filter.FilterPlatform;
import hive.model.segment.Segment;
import moontrack.events.segment.SegmentAddedEvent;
import moontrack.events.segment.SegmentArchivedEvent;
import moontrack.events.segment.SegmentEditedEvent;

@Controller
@RequestMapping("segments")
public class SegmentsController {

	@Autowired
	private GamesController gamesController;

	@Autowired
	private DashboardDAO dashboardDAO;

	@RequestMapping("")
	ModelAndView get() {
		ModelAndView mv = new ModelAndView("segments/segments");

		List<Segment> segments = dashboardDAO.getActiveSegments().stream()
				.sorted(Comparator.comparing(Segment::getCreated).reversed()).collect(Collectors.toList());

		mv.addObject("segments", segments);
		return mv;
	}

	@PostMapping("add")
	ModelAndView add() {
		ModelAndView mv = new ModelAndView("segments/add/segments.add");
		mv.addObject("games", gamesController.getGames());
		mv.addObject("countries", DTOStorage.getCountries());
		mv.addObject("oses", DTOStorage.getOses());
		mv.addObject("platforms", DTOStorage.getPlatforms());
//		mv.addObject("devices", DTOStorage.getDevices());
//		mv.addObject("realms", DTOStorage.getRealms());
		return mv;
	}

	@PostMapping("add/submit")
	ResponseEntity<String> submitAdd(@RequestParam("name") String name,
			@RequestParam(value = "game", required = true) Short gameId,
			@RequestParam(value = "country", required = false) String[] countries,
			@RequestParam(value = "os", required = false) Byte[] oses,
			@RequestParam(value = "platform", required = false) Byte[] platforms,
			@RequestParam(value = "moneyspentfrom", required = false) Float moneyspentfrom,
			@RequestParam(value = "moneyspentto", required = false) Float moneyspentto,
			@RequestParam(value = "registration", required = false) String registration,
			@RequestParam(value = "payinguser", required = false) Boolean payinguser) {

		Segment segment = new Segment();
		segment.setName(name);
		segment.setFilter(new FilterHub());
		segment.getFilter().setGameId(gameId);
		segment.getFilter().setSpentMoneyFrom(moneyspentfrom);
		segment.getFilter().setSpentMoneyTo(moneyspentto);

		if (registration != null && !registration.isEmpty()) {
			segment.getFilter().setRegistrationDateFrom(DateUtil.getStartDate(registration));
		}
		if (registration != null && !registration.isEmpty()) {
			segment.getFilter().setRegistrationDateTo(DateUtil.getFinishDate(registration));
		}
		segment.getFilter().setPaying(payinguser);

		if (platforms != null) {
			for (byte platform : platforms) {
				segment.getFilter().getPlatforms().add(new FilterPlatform(platform));
			}
		}
		if (countries != null) {
			for (String country : countries) {
				segment.getFilter().getCountries().add(new FilterCountry(country));
			}
		}
		if (oses != null) {
			for (byte os : oses) {
				segment.getFilter().getOses().add(new FilterOs(os));
			}
		}

		dashboardDAO.saveSegment(segment);
		dashboardDAO.saveSegmentCache(segment.getId());

		Log.out("segment added: " + segment);

		App.instance().eventProcessor().proccess(new SegmentAddedEvent(segment));

		return new ResponseEntity<>("", HttpStatus.OK);
	}

	@PostMapping(value = "edit/{segmentId}")
	ModelAndView edit(@PathVariable("segmentId") int segmentId) {

		Segment segment = dashboardDAO.getSegment(segmentId, true);

		List<CountryDTO> countries = new ArrayList<CountryDTO>(DTOStorage.getCountries());
		List<OsDTO> oses = new ArrayList<OsDTO>(DTOStorage.getOses());
		List<PlatformDTO> platforms = new ArrayList<PlatformDTO>(DTOStorage.getPlatforms());
		List<CountryDTO> countriesSelected = new ArrayList<CountryDTO>();
		List<OsDTO> osesSelected = new ArrayList<OsDTO>();
		List<PlatformDTO> platformsSelected = new ArrayList<PlatformDTO>();

		segment.getFilter().getCountries().forEach(c -> countriesSelected.add(DTOStorage.getCountryDTO(c.getCode())));
		segment.getFilter().getOses().forEach(o -> osesSelected.add(DTOStorage.getOsDTO(o.getCode())));
		segment.getFilter().getPlatforms().forEach(p -> platformsSelected.add(DTOStorage.getPlatformDTO(p.getCode())));

		countries.removeAll(countriesSelected);
		platforms.removeAll(platformsSelected);
		oses.removeAll(osesSelected);

		List<Game> games = new ArrayList<Game>(gamesController.getGames());
		Game gameSelected = games.stream().filter(g -> g.getId() == segment.getFilter().getGameId()).findFirst()
				.orElse(null);
		games.remove(gameSelected);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("segments/edit/segments.edit");
		mv.addObject("segment", segment);
		mv.addObject("countries", countries);
		mv.addObject("oses", oses);
		mv.addObject("platforms", platforms);
		mv.addObject("games", games);
		mv.addObject("countries_selected", countriesSelected);
		mv.addObject("oses_selected", osesSelected);
		mv.addObject("platforms_selected", platformsSelected);
		mv.addObject("game_selected", gameSelected);
		return mv;
	}

	@PostMapping("edit/{segmentId}/submit")
	ResponseEntity<String> postEdit(@PathVariable("segmentId") int segmentId, @RequestParam("name") String name,
			@RequestParam(value = "game", required = true) Short gameId,
			@RequestParam(value = "country", required = false) String[] countries,
			@RequestParam(value = "os", required = false) Byte[] oses,
			@RequestParam(value = "platform", required = false) Byte[] platforms,
			@RequestParam(value = "moneyspentfrom", required = false) Float moneyspentfrom,
			@RequestParam(value = "moneyspentto", required = false) Float moneyspentto,
			@RequestParam(value = "registration", required = false) String registration,
			@RequestParam(value = "payinguser", defaultValue = "false") Boolean payinguser) {

		Segment segment = dashboardDAO.getSegment(segmentId, true);

		segment.getFilter().getCountries().clear();
		segment.getFilter().getPlatforms().clear();
		segment.getFilter().getOses().clear();
		segment.getFilter().getDevices().clear();
		segment.getFilter().getRealms().clear();
		segment.setName(name);
		segment.getFilter().setGameId(gameId);
		segment.getFilter().setSpentMoneyFrom(moneyspentfrom);
		segment.getFilter().setSpentMoneyTo(moneyspentto);

		if (registration != null && !registration.isEmpty()) {
			segment.getFilter().setRegistrationDateFrom(DateUtil.getStartDate(registration));
		}
		if (registration != null && !registration.isEmpty()) {
			segment.getFilter().setRegistrationDateTo(DateUtil.getFinishDate(registration));
		}
		segment.getFilter().setPaying(payinguser);

		if (platforms != null) {
			for (byte platform : platforms) {
				FilterPlatform e = new FilterPlatform(platform);
				segment.getFilter().getPlatforms().add(e);
			}
		}
		if (countries != null) {
			for (String country : countries) {
				FilterCountry e = new FilterCountry(country);
				segment.getFilter().getCountries().add(e);
			}
		}
		if (oses != null) {
			for (byte os : oses) {
				FilterOs e = new FilterOs(os);
				segment.getFilter().getOses().add(e);
			}
		}

		dashboardDAO.updateSegment(segment);

		Log.out("segment edited : " + segment);

		App.instance().eventProcessor().proccess(new SegmentEditedEvent(segment));

		return new ResponseEntity<>("", HttpStatus.OK);
	}

	@PostMapping("view/{segmentId}")
	ModelAndView getView(@PathVariable int segmentId) {
		Segment segment = dashboardDAO.getSegment(segmentId, true);
		Game game = dashboardDAO.getGameById(segment.getFilter().getGameId());

		SegmentUsers segmentUsers = new SegmentUsers(segment);
		HiveDAO.performQuery(segmentUsers);

		List<CountryDTO> countries = new ArrayList<CountryDTO>();
		segment.getFilter().getCountries().forEach(c -> countries.add(DTOStorage.getCountryDTO(c.getCode())));

		List<OsDTO> oses = new ArrayList<OsDTO>();
		segment.getFilter().getOses().forEach(c -> oses.add(DTOStorage.getOsDTO(c.getCode())));

		List<PlatformDTO> platforms = new ArrayList<PlatformDTO>();
		segment.getFilter().getPlatforms().forEach(c -> platforms.add(DTOStorage.getPlatformDTO(c.getCode())));

		ModelAndView mv = new ModelAndView("segments/view/segments.view");
		mv.addObject("segment", segment);
		mv.addObject("game", game);
		mv.addObject("countries", countries);
		mv.addObject("oses", oses);
		mv.addObject("platforms", platforms);
		mv.addObject("paying", segment.getFilter().isPaying());
		mv.addObject("moneyfrom", segment.getFilter().getSpentMoneyFrom());
		mv.addObject("moneyto", segment.getFilter().getSpentMoneyTo());
		mv.addObject("regfrom", segment.getFilter().getRegistrationDateFrom());
		mv.addObject("regtill", segment.getFilter().getRegistrationDateTo());
		mv.addObject("totalusers", segmentUsers.getValue(0));
		mv.addObject("elementids", WidgetConfig.metricMarkups.values());
		return mv;
	}

	@PostMapping("archive/{segmentId}")
	ModelAndView archive(@PathVariable("segmentId") int segmentId) {
		ModelAndView mv = new ModelAndView("segments/archive/segments.archive");
		mv.addObject("segment", dashboardDAO.getLazySegmentById(segmentId));
		return mv;
	}

	@PostMapping("archive/{segmentId}/submit")
	ResponseEntity<String> archivate(@PathVariable("segmentId") int segmentId, ModelMap model) {
		Segment segment = dashboardDAO.archivateSegment(segmentId);

		App.instance().eventProcessor().proccess(new SegmentArchivedEvent(segment.getId()));
		return new ResponseEntity<>("", HttpStatus.OK);
	}

	@PostMapping(value = "metrics/get/{segmentId}/{metricName}", consumes = { "text/plain",
			"application/*" }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	ResponseEntity<String> getChart(@PathVariable int segmentId, @PathVariable String metricName) {

		Segment segment = dashboardDAO.getSegment(segmentId, true);
		byte metricType = MetricType.getTypeByName(metricName);

		Log.out(metricName);

		SegmentMetric metric = SegmentMetricFactory.getSegmentMetric(metricType, segment);
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
