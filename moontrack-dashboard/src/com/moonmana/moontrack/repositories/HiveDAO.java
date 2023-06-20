package com.moonmana.moontrack.repositories;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.moonmana.OS;
import com.moonmana.PlatformID;
import com.moonmana.log.Log;
import com.moonmana.moontrack.abtests.metrics.AbtestMetric;
import com.moonmana.moontrack.abtests.metrics.AbtestUsers;
import com.moonmana.moontrack.dto.DashboardCacheDTO;
import com.moonmana.moontrack.metric.Metric;
import com.moonmana.moontrack.metrics.Arpdau;
import com.moonmana.moontrack.metrics.Dau;
import com.moonmana.moontrack.metrics.InstallsTotal;
import com.moonmana.moontrack.metrics.Registrations;
import com.moonmana.moontrack.metrics.RevenueDaily;
import com.moonmana.moontrack.metrics.RevenueTotal;

import analytics.model.user.ARealmUser;
import core.hibernate.Hibernate;
import hive.model.abtest.AbTest;
import hive.model.abtest.AbTestGroup;
import hive.model.abtest.AbTestUser;
import hive.model.filter.FilterHub;
import hive.model.filter.FilterOs;
import hive.model.filter.FilterPlatform;

public class HiveDAO {


	public static void performQuery(Metric metric) {
		Session session = openSession();
		metric.performQuery(session);
		closeSession(session);
	}

	public static void performAbtestMetricsQuery(List<AbtestMetric> metrics) {
		Session session = openSession();

		metrics.forEach(m -> m.performQuery(session));

		closeSession(session);
	}

	public static Session openSession() {
		return Hibernate.getFactory("analytics").openSession();
	}

	public static void closeSession(Session session) {
		if (session != null) {
			session.clear();
			Hibernate.close(session);
		}
	}

	@SuppressWarnings("unchecked")
	public static List<Object[]> retriveRows(String sql) {
		Session session = openSession();

		if (sql == null) {
			throw new NullPointerException();
		}

		List<Object[]> result = session.createNativeQuery(sql).getResultList();

		closeSession(session);
		return result;
	}

	public static void addAndSaveNewAbtestUsers(AbTestGroup group, List<Integer> newUsers) {
		Session session = openSession();
		Transaction transaction = session.beginTransaction();

		for (Integer i : newUsers) {
			AbTestUser abUser = new AbTestUser();
			abUser.setGroup(group);
			abUser.setRealmUser(session.getReference(ARealmUser.class, newUsers.get(i)));
			session.save(abUser);
		}
		transaction.commit();
		closeSession(session);

		Log.out("Abtest users saved");
	}

	public static List<AbTest> getAbtests() {
		Session session = openSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<AbTest> criteria = cb.createQuery(AbTest.class);
		Root<AbTest> root = criteria.from(AbTest.class);
		criteria.select(root);
		List<AbTest> abtests = session.createQuery(criteria).getResultList();
		abtests.forEach(ab -> ab.getGroups().forEach(g -> g = Hibernate.unproxy(g)));

		closeSession(session);
		return abtests;
	}

	public static void updateAbtestGroupSize(AbTestGroup group) {
		Session session = openSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
		Root<AbtestUsers> root = query.from(AbtestUsers.class);
		query.select(cb.count(root)).where(cb.equal(root.get("groupId"), group.getId()));
		int groupSize = session.createQuery(query).getSingleResult().intValue();
		group.setGroupSize(groupSize);

		Transaction transaction = session.beginTransaction();
		session.merge(group);
		transaction.commit();

		closeSession(session);
	}

	public static List<Integer> getRealmUsersByGame(short gameId) {
		Session session = openSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
		Root<ARealmUser> root = query.from(ARealmUser.class);
		Join<Object, Object> duserJoin = root.join("dUser");
		duserJoin.on(cb.equal(duserJoin.get("game"), gameId));
		query.select(root.get("id"));
		List<Integer> gameUsers = session.createQuery(query).getResultList();

		closeSession(session);

		return gameUsers;
	}

	public static List<Integer> getGroupUsers(AbTestGroup group) {
		Session session = openSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
		Root<AbTestUser> root = query.from(AbTestUser.class);
		query.select(root.get("realm_user")).where(cb.equal(root.get("groupId"), group.getId()));
		List<Integer> users = session.createQuery(query).getResultList();

		closeSession(session);

		return users;
	}

}
