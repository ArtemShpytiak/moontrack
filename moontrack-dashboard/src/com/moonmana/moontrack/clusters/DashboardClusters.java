package com.moonmana.moontrack.clusters;

import core.cluster.Clusters;
import moontrack.analytics.AnalyticsCluster;

public class DashboardClusters extends Clusters {

	public AnalyticsCluster hive = null;
	
	public static DashboardClusters get() {
		return (DashboardClusters) Clusters.get();
	}
}
