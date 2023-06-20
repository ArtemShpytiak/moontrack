package com.moonmana.moontrack.security;

import java.util.ArrayList;

import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.moonmana.log.Log;
import com.moonmana.moontrack.DashboardApp;
import com.moonmana.moontrack.beans.UserSessionBean;
import com.moonmana.moontrack.controllers.UserController;
import com.moonmana.moontrack.dbAccess.DatabaseAccessControllerImpl;
import com.moonmana.moontrack.dbAccess.IDatabaseAccessController;
import com.moonmana.moontrack.model.User;
import com.moonmana.moontrack.repositories.DashboardDAO;
import com.moonmana.moontrack.ui.UiBean;

@Component
public class DashboardAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private WebApplicationContext sessionContext;
	@Autowired
	private IDatabaseAccessController dbAccesController;
	@Autowired
	private UserController userController;
	

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		String passwordMd5 = DigestUtils.md5Hex(password);
		String dbPasswordMd5;
		Session moontrackSession = dbAccesController.getDashboardSession();
		Query<?> query = moontrackSession.createQuery("from " + User.class.getName(), User.class);
		if (query.getResultList().size() == 0) {
			Log.out("Userlist is empty. Admin user will be created.");
			userController.registerDefaultAdmin();
		}

		query = moontrackSession.createQuery("from " + User.class.getName() + " where login = :login", User.class)
				.setParameter("login", name);
		User dbUser = (User) query.uniqueResult();

		if (dbUser != null) {
			dbPasswordMd5 = dbUser.getPassword();
		} else
			return null;

		if (passwordMd5.equals(dbPasswordMd5)) {
			UiBean uiBean = applicationContext.getBean(UiBean.class);
			uiBean.setUser(dbUser);
			UserSessionBean usBean = sessionContext.getBean(UserSessionBean.class);
			usBean.setUser(dbUser);

			return new UsernamePasswordAuthenticationToken(name, password, new ArrayList<>());
		}

		else
			return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
