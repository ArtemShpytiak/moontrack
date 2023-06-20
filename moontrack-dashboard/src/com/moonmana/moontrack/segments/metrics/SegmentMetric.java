package com.moonmana.moontrack.segments.metrics;

import java.util.Date;

import org.hibernate.Session;

import com.moonmana.log.Log;
import com.moonmana.moontrack.metric.Metric;
import com.moonmana.moontrack.repositories.DashboardDAO;
import com.moonmana.moontrack.repositories.HiveDAO;
import com.moonmana.moontrack.segments.cache.SegmentCache;

import hive.model.segment.Segment;

public abstract class SegmentMetric extends Metric {
	protected Segment segment;

	public SegmentMetric(Segment segment) {
		this.segment = segment;
	}

	@Override
	public void performQuery(DashboardDAO dashboardDAO) {
		SegmentCache cache = dashboardDAO.getSegmentCache(segment, type);

		if (cache.isValid()) {
			Log.out(cache.getChartData());
			getChart().setData(cache.getChartData());
			getChart().formJsonConfig();
		} else {
			Session session = HiveDAO.openSession();
			performQuery(session);
			HiveDAO.closeSession(session);

			getChart().formJsonData();
			getChart().formJsonConfig();

			cache.setChartData(getChart().getData().toString());
			cache.setRefreshed(new Date());
			dashboardDAO.updateSegmentCache(cache);
		}
	}

	protected String getDateRange(String dateColumn) {
		String result = "";
		if (segment.getFilter().getDateFrom() != null) {
			result += " AND " + dateColumn + " >='" + formatDateToDaily(segment.getFilter().getDateFrom()) + "'";
		}
		if (segment.getFilter().getDateTill() != null) {
			result += " AND " + dateColumn + " <= '" + formatDateToDaily(segment.getFilter().getDateTill())
					+ "'";
		}
		return result;
	}

}
