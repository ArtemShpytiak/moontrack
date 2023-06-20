package com.moonmana.moontrack.abtests.metrics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.moonmana.log.Log;
import com.moonmana.moontrack.abtests.cache.AbtestCache;
import com.moonmana.moontrack.abtests.metrics.groups.AbgroupMetric;
import com.moonmana.moontrack.metric.Metric;
import com.moonmana.moontrack.repositories.DashboardDAO;
import com.moonmana.moontrack.repositories.HiveDAO;

import hive.model.abtest.AbTest;
import hive.model.abtest.AbTestGroup;

public abstract class AbtestMetric extends Metric {

	protected List<AbgroupMetric> groups;
	protected AbTest abtest;

	public AbtestMetric(AbTest abtest) {
		this.abtest = abtest;
	}

	@Override
	public void performQuery(DashboardDAO dashboardDAO) {
		AbtestCache cache = dashboardDAO.getAbtestCache(abtest, type);

		if (cache.isValid()) {
			Log.out(cache.getChartData());
			getChart().setData(cache.getChartData());
			getChart().formJsonConfig();
		} else {
			initSubqueries();
			performSubqueries();

			getChart().formJsonData();
			getChart().formJsonConfig();

			cache.setChartData(getChart().getData().toString());
			cache.setRefreshed(new Date());
			dashboardDAO.updateAbtestCache(cache);
		}
	}

	private void performSubqueries() {
		groups.forEach(group -> {
			group.setRows(HiveDAO.retriveRows(group.getSQL()));
		});
	}

	private void initSubqueries() {
		groups = new ArrayList<AbgroupMetric>();
		abtest.getGroups().forEach(group -> addGroup(getSubquery(group)));
	}

	protected AbgroupMetric getSubquery(AbTestGroup group) {
		return null;
	}

	protected void addGroup(AbgroupMetric abgroup) {
		groups.add(abgroup);
		addSubmetric(abgroup);
	}

	public List<AbgroupMetric> getGroups() {
		return groups;
	}

}
