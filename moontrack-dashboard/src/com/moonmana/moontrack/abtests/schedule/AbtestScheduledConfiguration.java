package com.moonmana.moontrack.abtests.schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.moonmana.log.Log;
import com.moonmana.moontrack.abtests.cache.AbtestCache;
import com.moonmana.moontrack.abtests.metrics.AbtestMetric;
import com.moonmana.moontrack.abtests.metrics.AbtestMetricFactory;
import com.moonmana.moontrack.repositories.DashboardDAO;
import com.moonmana.moontrack.repositories.HiveDAO;

import hive.model.abtest.AbTest;
import hive.model.abtest.AbTestGroup;
import hive.model.abtest.AbTestGroupByFilter;
import hive.model.abtest.AbTestGroupByPercentage;
import hive.model.abtest.AbTestGroupBySegment;

@Configuration
@EnableScheduling
public class AbtestScheduledConfiguration {

//	@Scheduled(initialDelay = 5 * 60 * 1000, fixedRate = 24 * 60 * 60 * 1000)
//	@Scheduled(initialDelay = 17 * 1000, fixedDelay = 5 * 1000)
	public void updateAbtestCache(DashboardDAO dashboardDAO) {
		Log.out("Updating Abtest Cache");

		List<AbtestCache> allCache = dashboardDAO.getAllAbtestCache();

		for (AbtestCache cache : allCache) {
			if (cache.getRefreshed() == null) {
				continue;
			}
			Calendar currDate = Calendar.getInstance();
			currDate.setTime(new Date());

			Calendar nextRefresh = Calendar.getInstance();
			nextRefresh.setTime(cache.getRefreshed());
			nextRefresh.add(Calendar.DATE, 1);

			if (currDate.after(nextRefresh)) {
				int abtestId = cache.getAbtestId();
				AbTest abtest = dashboardDAO.getAbtest(abtestId, true);

				if (abtest.isArchived()) {
					continue;
				}

				AbtestMetric metric = AbtestMetricFactory.getAbtestMetric(cache.getMetricType(), abtest);
				metric.performQuery(dashboardDAO);

				metric.getChart().formJsonData();
				cache.setChartData(metric.getChart().getData().toString());
				cache.setRefreshed(new Date());
				dashboardDAO.updateAbtestCache(cache);
			}
		}
	}

//	@Scheduled(initialDelay = 5 * 60 * 1000, fixedRate = 8 * 60 * 60 * 1000)
//	@Scheduled(initialDelay = 17 * 1000, fixedDelay = 5 * 1000)
	public void updateAbtestUsers2() {
		Log.out("Start updating abtest group users...");

		List<AbTest> abtests = HiveDAO.getAbtests();

		Log.out("Count abtests: " + abtests.size());

		for (AbTest abtest : abtests) {

			Log.out("Processing abtest id: " + abtest.getId() + ", name: " + abtest.getName());

			List<Integer> gameUsers = HiveDAO.getRealmUsersByGame(abtest.getGameId());

			Log.out("Retrieved game users amount: " + gameUsers.size());

			List<AbTestGroupByFilter> groupsBybFilter = new ArrayList<AbTestGroupByFilter>();
			List<AbTestGroupBySegment> groupsBySegment = new ArrayList<AbTestGroupBySegment>();
			List<AbTestGroupByPercentage> groupsByPercentage = new ArrayList<AbTestGroupByPercentage>();

			for (AbTestGroup g : abtest.getGroups()) {
				if (g instanceof AbTestGroupByPercentage) {
					groupsByPercentage.add((AbTestGroupByPercentage) g);
				} else if (g instanceof AbTestGroupBySegment) {
					groupsBySegment.add((AbTestGroupBySegment) g);
				} else if (g instanceof AbTestGroupByFilter) {
					groupsBybFilter.add((AbTestGroupByFilter) g);
				}
			}

			if (!groupsBybFilter.isEmpty()) {

			} else if (!groupsBySegment.isEmpty()) {

			} else if (!groupsByPercentage.isEmpty()) {

				Log.out("Processing percentage groups");

				boolean requeresUpdate = false;
				for (AbTestGroupByPercentage percentageGroup : groupsByPercentage) {
					int newGroupSize = percentageGroup.calculateGroupSize(gameUsers.size());
					if (percentageGroup.getGroupSize() < newGroupSize) {
						requeresUpdate = true;
						break;
					}
				}

				if (requeresUpdate) {
					for (AbTestGroupByPercentage group : groupsByPercentage) {
						List<Integer> users = HiveDAO.getGroupUsers(group);

						Log.out("Retrieving users from heap: " + users.size() + ", group id: " + group.getId());

						gameUsers.removeAll(users);
					}

					for (AbTestGroupByPercentage group : groupsByPercentage) {
						int newGroupSize = group.calculateGroupSize(gameUsers.size());

						Log.out("Group id: " + group.getId() + ", new group size: " + newGroupSize
								+ ", current group size: " + group.getGroupSize());

						if (group.getGroupSize() < newGroupSize) {
							int diff = newGroupSize - group.getGroupSize();

							List<Integer> usersToAdd = gameUsers.subList(0, diff - 1);

							Log.out("Users to add to group: " + usersToAdd.size());
							Log.out("Game users size: " + gameUsers.size());

							gameUsers.removeAll(usersToAdd);

							HiveDAO.addAndSaveNewAbtestUsers(group, usersToAdd);
							HiveDAO.updateAbtestGroupSize(group);
						}
					}
				} else {
					Log.out("Abtest with id: " + abtest.getId() + " does not require updating group users");
				}
			} else {
				Log.outError("Unknown abtest groups");
			}
		}
	}

}