package com.moonmana.moontrack.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.moonmana.moontrack.model.Game;
import com.moonmana.moontrack.ui.UiBean;

@Controller
@RequestMapping("/")
public class IndexController {

	@Autowired
	private GamesController gamesController;

	@Autowired
	private ApplicationContext applicationContext;

	@RequestMapping("")
	public ModelAndView index() {
		List<Game> games = gamesController.getGames();

		UiBean uiBean = applicationContext.getBean(UiBean.class);
		ModelAndView mv = new ModelAndView("index/index", "uiBean", uiBean);
		mv.addObject("games", games);
		return mv;
	}

}
