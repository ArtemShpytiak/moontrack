package com.moonmana.moontrack.clusters;

import core.cluster.core.Cluster;
import core.cluster.core.manager.ServiceManager;

public class DashboardCluster extends Cluster {

	public DashboardCluster(ServiceManager serviceManager) {
		super(serviceManager, "dashboard", "dashboard");
	}

}
