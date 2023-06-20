package com.moonmana.moontrack.dbAccess;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import core.hibernate.Hibernate;

public class DatabaseAccessControllerImpl implements IDatabaseAccessController {

	private Session dashboardSession = null; 
	private Transaction dashboardTransaction = null; 
	private Session hiveSession = null;
	private Set<Session> hiveSessions = new HashSet<>(); 
	
	@Override
	public Session getDashboardSession() {
		if (dashboardSession == null) {
			dashboardSession = Hibernate.getFactory("dashboard").openSession();
		}
		return dashboardSession;
	}

	@Override
	public void beginDashboardTransaction() {
		dashboardTransaction = getDashboardSession().beginTransaction(); 
	}

	@Override
	public void commitDashboardTransaction() {
		dashboardTransaction.commit();
		dashboardTransaction = null;
	}

	@Override
	public void rollbackDashboardTransaction() {
		if (dashboardTransaction != null) {
			dashboardTransaction.rollback();
			dashboardTransaction = null;
		}
	}

	@Override
	public Session getHiveSession() {
		if (hiveSession == null) {
			hiveSession = Hibernate.getFactory("analytics").openSession();
		}
		return hiveSession;
	}
	
	public Session createNewHiveSession() {
		Session session = Hibernate.getFactory("analytics").openSession();
		hiveSessions.add(session);
		return session;
	}

	@Override
	public void rollbackDashboardTransactionIfActive() {
		Transaction transaction = getDashboardSession().getTransaction();
		if (transaction != null && transaction.isActive()) {
			transaction.rollback();
			transaction = null;
		}
		
	}
	
	private void closeSession(Session session) {
		if (session != null) {
			session.clear();
			Hibernate.close(session);
		}
	}

	public void close() {
		if (dashboardTransaction != null) {
			dashboardTransaction.rollback();
			dashboardTransaction = null;
		}
		closeSession(getDashboardSession());
		dashboardSession = null;
		closeSession(getHiveSession());
		hiveSession = null;
		
		for (Session s : hiveSessions) {
			closeSession(s);
		}
	}
}
