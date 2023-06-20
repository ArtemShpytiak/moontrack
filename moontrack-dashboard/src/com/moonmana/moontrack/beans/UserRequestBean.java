package com.moonmana.moontrack.beans;

import org.hibernate.Session;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.moonmana.moontrack.dbAccess.DatabaseAccessControllerImpl;

@Component
@RequestScope
public class UserRequestBean implements DisposableBean {
	DatabaseAccessControllerImpl dbAccessController = new DatabaseAccessControllerImpl();

	public UserRequestBean() {
		super();
	}

	@Override
	public void destroy() throws Exception {
		dbAccessController.close();
		
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!UserRequestBean DESTROYED!!!!!!!!!!!!!!!!!!!!!");
	}

	public Session getDashboardSession() {
		return dbAccessController.getDashboardSession();
	}
	
	public void beginDashboardTransaction() {
		dbAccessController.beginDashboardTransaction();
	}
	
	public void commitDashboardTransaction() {
		dbAccessController.commitDashboardTransaction();
	}
	
	public void rollbackDashboardTransaction() {
		dbAccessController.rollbackDashboardTransaction();
	}

	public Session getHiveSession() {
		return dbAccessController.getHiveSession();
	}
}
