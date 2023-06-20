package com.moonmana.moontrack.ui;

import org.springframework.stereotype.Component;

import com.moonmana.moontrack.model.User;

@Component
public class UiBean {

	private User user;

	private boolean isActual = false;

	public UiBean() {
		super();
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isActual() {
		return isActual;
	}

	public void setActual(boolean isActual) {
		this.isActual = isActual;
	}

}
