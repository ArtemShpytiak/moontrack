package com.moonmana.moontrack.clusters;

import java.util.Collection;
import java.util.HashSet;

import config.DashboardCache;
import config.DashboardEntityMapping;
import core.cluster.core.Cluster;
import core.cluster.core.manager.HibernateService;
import core.hibernate.DbInfo;

public class DashboardService extends HibernateService {

	public DashboardService() {
		super("dashboard", new DashboardEntityMapping(), new DashboardCache("dashboard"));
	}

	@Override
	public DbInfo getDefaultDbInfo() {
		DbInfo dbInfo = new DbInfo();
		dbInfo.username = "moontrack";
		dbInfo.database = "moontrack";
		return dbInfo;
	}

	@Override
	public Collection<Class<? extends Cluster>> getClustersInUse() {
		return new HashSet<Class<? extends Cluster>>();
	}

	@Override
	public String[] getGroupTypes() {
		return new String[] { "dashboard", "analytics" };
	}

}
