package com.moonmana.moontrack.segments.shedule;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.moonmana.log.Log;
import com.moonmana.moontrack.repositories.DashboardDAO;
import com.moonmana.moontrack.repositories.HiveDAO;
import com.moonmana.moontrack.segments.cache.SegmentCache;
import com.moonmana.moontrack.segments.metrics.SegmentMetric;
import com.moonmana.moontrack.segments.metrics.SegmentMetricFactory;

import hive.model.segment.Segment;

@Configuration
@EnableScheduling
public class SegmentScheduledConfiguration {

	@Autowired
	private DashboardDAO dashboardDAO;

//	@Scheduled(initialDelay = 3 * 60 * 1000, fixedRate = 24 * 60 * 60 * 1000)
//	@Scheduled(initialDelay = 17 * 1000, fixedDelay = 5 * 1000)
	public void updateSegmentCache() {
		Log.out("Updating Segment Cache");

		List<SegmentCache> allCache = dashboardDAO.getAllSegmentCache();

		for (SegmentCache cache : allCache) {
			if (cache.getRefreshed() == null) {
				continue;
			}
			Calendar currDate = Calendar.getInstance();
			currDate.setTime(new Date());

			Calendar nextRefresh = Calendar.getInstance();
			nextRefresh.setTime(cache.getRefreshed());
			nextRefresh.add(Calendar.DATE, 1);

			if (currDate.after(nextRefresh)) {
				int segmentId = cache.getSegmentId();
				Segment segment = dashboardDAO.getSegment(segmentId, true);

				if (segment.isArchived()) {
					continue;
				}

				SegmentMetric metric = SegmentMetricFactory.getSegmentMetric(cache.getMetricType(), segment);

				Session session = HiveDAO.openSession();
				metric.performQuery(session);
				HiveDAO.closeSession(session);

				metric.getChart().formJsonData();
				cache.setChartData(metric.getChart().getData().toString());
				cache.setRefreshed(new Date());
				dashboardDAO.updateSegmentCache(cache);

			}
		}

	}

}
