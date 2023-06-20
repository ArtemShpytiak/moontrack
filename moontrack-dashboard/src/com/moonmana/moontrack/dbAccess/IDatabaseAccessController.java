package com.moonmana.moontrack.dbAccess;

import org.hibernate.Session;

public interface IDatabaseAccessController {

	Session getDashboardSession();

	void beginDashboardTransaction();

	void commitDashboardTransaction();

	void rollbackDashboardTransaction();

	Session getHiveSession();

	void rollbackDashboardTransactionIfActive();

}
