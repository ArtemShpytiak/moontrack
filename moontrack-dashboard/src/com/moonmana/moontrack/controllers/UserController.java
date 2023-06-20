package com.moonmana.moontrack.controllers;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.moonmana.moontrack.beans.UserSessionBean;
import com.moonmana.moontrack.dbAccess.RequestScopeDatabaseAccessController;
import com.moonmana.moontrack.model.User;
import com.moonmana.moontrack.repositories.DashboardDAO;
import com.moonmana.moontrack.ui.UiBean;

@Controller
public class UserController {

	@Autowired
	private RequestScopeDatabaseAccessController dbAccessController;
	
	@Autowired
	private WebApplicationContext webAppContext;

	// TODO: Remove hardcode
	public User getUser() {
		UserSessionBean userBean = webAppContext.getBean(UserSessionBean.class);
		Session session = dbAccessController.getDashboardSession();
		User user = session.get(User.class, userBean.getUserId());
		return user;
	}

	public void registerDefaultAdmin() {
		Session session = dbAccessController.getDashboardSession();
		dbAccessController.beginDashboardTransaction();
		User user = new User();
		user.setLogin("admin");
		user.setUsername("Default admin");
		user.setRegistratoinDate(new Date());
		user.setPassword(DigestUtils.md5Hex("moontrack"));
		user.setAdmin(true);
		session.save(user);
		dbAccessController.commitDashboardTransaction();
	}

//	@GetMapping("register")
	ModelAndView registrationForm() {
		return new ModelAndView("auth/register");
	}

//	@PostMapping("register")
	RedirectView registerNewUser(@ModelAttribute("user") User user) {
		Session session = dbAccessController.getDashboardSession();
		session.beginTransaction();
		user.setPassword(DigestUtils.md5Hex(user.getPassword()));
		session.save(user);
		session.getTransaction().commit();
		session.close();
		return new RedirectView("index/index");
	}
}
