package com.moonmana.moontrack.segments.metrics;

import java.util.ArrayList;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import com.moonmana.moontrack.chart.Chart;
import com.moonmana.moontrack.metric.MetricType;

import hive.model.segment.Segment;
import hive.model.segment.SegmentUser;

public class SegmentUsers extends SegmentMetric {

	public SegmentUsers(Segment segment) {
		super(segment);
		setType(MetricType.USERS);
	}

	@Override
	public String getSQL() {
		return super.getSQL();
	}

	@Override
	protected Chart getChartInstance() {
		return null;
	}

	@Override
	public void performQuery(Session session) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<SegmentUser> from = cq.from(SegmentUser.class);
		cq.select(builder.count(from));
		cq.where(builder.equal(from.get("segment"), segment.getId()));
		Long size = session.createQuery(cq).getSingleResult();

		rows = new ArrayList<Object[]>(1);
		rows.add(new Object[] { segment.getId(), size.intValue() });
	}

}
