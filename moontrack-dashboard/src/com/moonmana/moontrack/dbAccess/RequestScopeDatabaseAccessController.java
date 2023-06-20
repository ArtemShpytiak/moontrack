package com.moonmana.moontrack.dbAccess;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.moonmana.moontrack.beans.UserRequestBean;

@Component
public class RequestScopeDatabaseAccessController implements IDatabaseAccessController{
	@Autowired
	private WebApplicationContext webAppContext;
	
	@Override
	public Session getDashboardSession() {
		return getBean().getDashboardSession();
	}

	@Override
	public void beginDashboardTransaction() {
		getBean().beginDashboardTransaction();
	}

	@Override
	public void commitDashboardTransaction() {
		getBean().commitDashboardTransaction();
	}

	@Override
	public void rollbackDashboardTransaction() {
		getBean().rollbackDashboardTransaction();
	}

	@Override
	public Session getHiveSession() {
		return getBean().getHiveSession();
	}
	
	private UserRequestBean getBean() {
		return webAppContext.getBean(UserRequestBean.class);
	}

	@Override
	public void rollbackDashboardTransactionIfActive() {
		getBean().rollbackDashboardTransaction();
		
	}

}
