package com.moonmana.moontrack.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.moonmana.app.App;
import com.moonmana.moontrack.MoontrackTomcatModule;
import com.moonmana.moontrack.dbAccess.RequestScopeDatabaseAccessController;
import com.moonmana.moontrack.dto.DTOStorage;
import com.moonmana.moontrack.model.Game;
import com.moonmana.moontrack.model.User;
import com.moonmana.moontrack.repositories.DashboardDAO;

import core.hibernate.Hibernate;
import core.tomcat.ITomcatApp;

@Controller
@RequestMapping("games")
public class GamesController {

	
	
	public GamesController() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private UserController userController;

	@Autowired
	private DashboardDAO dashboardDAO;

	@Autowired
	private RequestScopeDatabaseAccessController dbAccessController;

	@PostMapping("")
	ModelAndView get() {
		ModelAndView mv = new ModelAndView("games/games");
		mv.addObject("games", getActiveGames());
		return mv;
	}

	@PostMapping("add")
	ModelAndView add() {
		return new ModelAndView("games/add/games.add");
	}

	@PostMapping("add/submit")
	ResponseEntity<String> post(@RequestParam String gamename) {
		Game game = new Game();
		game.setName(gamename);

		saveGame(game);

		return new ResponseEntity<>("", HttpStatus.OK);
	}

	@PostMapping("edit/{gameId}")
	ModelAndView edit(@PathVariable short gameId) {
		ModelAndView mv = new ModelAndView("games/edit/games.edit");
		mv.addObject("game", dashboardDAO.getGameById(gameId));
		return mv;
	}

	@PostMapping("edit/{gameId}/submit")
	ResponseEntity<String> editSubmit(@PathVariable short gameId, @RequestParam String gamename) {
		Game game = dashboardDAO.getGameById(gameId);
		game.setName(gamename);

		updateGame(game);

		return new ResponseEntity<>("", HttpStatus.OK);
	}

	@PostMapping("view/{gameId}")
	ModelAndView view(@PathVariable short gameId) {

		// TODO load metrics !!!!!

		ModelAndView mv = new ModelAndView("games/view/games.view");
		mv.addObject("metrics", DTOStorage.getMetrics());
		mv.addObject("countries", DTOStorage.getCountries());
		mv.addObject("oses", DTOStorage.getOses());
		mv.addObject("platforms", DTOStorage.getPlatforms());
		// mv.addObject("devices", DTOStorage.getDevices());
		// mv.addObject("realms", DTOStorage.getRealms());
		mv.addObject("game", dashboardDAO.getGameById(gameId));
		return mv;
	}

	@PostMapping("archive/{gameId}")
	ModelAndView archive(@PathVariable short gameId) {
		ModelAndView mv = new ModelAndView("games/archive/games.archive");
		mv.addObject("game", dashboardDAO.getGameById(gameId));
		return mv;
	}

	@PostMapping("archive/{gameId}/submit")
	ResponseEntity<String> archivate(@PathVariable short gameId) {
		Game game = dashboardDAO.getGameById(gameId);
		game.setArchived(true);

		updateGame(game);

		// TODO send message to hive !!! wtf what message?

		return new ResponseEntity<>("", HttpStatus.OK);
	}

	public List<Game> getGames() {
		Session session = dbAccessController.getDashboardSession();
		User user = userController.getUser();
		if (user == null) {

			return null;
		}
		session.refresh(user);
		List<Game> result = new ArrayList<Game>(user.getCompany().getGames());
		result.forEach(Hibernate::unproxy);
		return result;
	}

	public List<Game> getActiveGames() {
		Session session = dbAccessController.getDashboardSession();

		User user = userController.getUser();
		if (user == null) {
			return null;
		}
		List<Game> games = user.getCompany().getGames();
		games.forEach(game -> game = Hibernate.unproxy(game));

		return games.stream().filter(obj -> !obj.isArchived()).collect(Collectors.toCollection(ArrayList::new));
	}

	public List<Game> getAbtestGames() {
		Session session = dbAccessController.getDashboardSession();
		List<Game> games = userController.getUser().getCompany().getGames();
		games.forEach(game -> {
			game = Hibernate.unproxy(game);
			game.getAbtests().forEach(ab -> ab = Hibernate.unproxy(ab));
		});
		return games;
	}

	public void saveGame(Game game) {
		Session session = dbAccessController.getDashboardSession();
		Transaction beginTransaction = session.beginTransaction();
	
		User user = userController.getUser();
		session.refresh(user);
		user.getCompany().addGame(game);
		session.save(game);	
		beginTransaction.commit();
		updateDashboardCache(game);
	}

	private void updateDashboardCache(Game game) {
		((MoontrackTomcatModule)((ITomcatApp) App.instance()).getTomcatModule()).getCacheUpdater()
				.updateCacheForGame(game.getId());
	}

	public void updateGame(Game game) {
		Session session = dbAccessController.getDashboardSession();
		
		dbAccessController.beginDashboardTransaction();
		session.update(game);
		dbAccessController.commitDashboardTransaction();
	}

}