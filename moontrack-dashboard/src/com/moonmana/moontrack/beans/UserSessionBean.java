package com.moonmana.moontrack.beans;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.moonmana.moontrack.model.User;

@Component
@SessionScope
public class UserSessionBean {
	private int userId = -1;

	public UserSessionBean() {
		super();
	}

	public void setUser(User user) {
		userId = user.getId();
		
	}

	public int getUserId() {
		return userId;
	}
	

}
