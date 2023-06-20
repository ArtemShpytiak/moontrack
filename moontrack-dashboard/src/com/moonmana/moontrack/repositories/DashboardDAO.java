package com.moonmana.moontrack.repositories;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.moonmana.log.Log;
import com.moonmana.moontrack.abtests.cache.AbtestCache;
import com.moonmana.moontrack.controllers.UserController;
import com.moonmana.moontrack.dbAccess.DatabaseAccessControllerImpl;
import com.moonmana.moontrack.dbAccess.IDatabaseAccessController;
import com.moonmana.moontrack.metric.MetricType;
import com.moonmana.moontrack.model.Game;
import com.moonmana.moontrack.model.GameWidget;
import com.moonmana.moontrack.model.User;
import com.moonmana.moontrack.model.Widget;
import com.moonmana.moontrack.model.cache.DashboardMetricCache;
import com.moonmana.moontrack.model.cache.GameDashboardCache;
import com.moonmana.moontrack.segments.cache.SegmentCache;

import core.hibernate.Hibernate;
import hive.model.abtest.AbTest;
import hive.model.filter.FilterHub;
import hive.model.segment.Segment;

@Component
public class DashboardDAO {

	@Autowired
	private UserController userController;

	@Autowired
	private IDatabaseAccessController dbAccessController;

	public JSONObject getDashboardWidgetMetricCache() {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();

		// TODO: Remove hardcode
		int companyId = 1;
		if (userController != null) {
			User user = userController.getUser();
			if (user == null) {
				return null;
			}
			companyId = user.getCompany().getId();
		}

		DashboardMetricCache cache = getDashboardMetricCache(session, companyId);
		JSONObject result;

//		if (cache == null) {
//			cache = new DashboardMetricCache();
//		}
//		if (cache.getRefreshDate() == null) {
//			DashboardCacheDTO dto = new DashboardCacheDTO(new JSONObject());
//
//			cache.setCache(dto.getCache().toString());
//			cache.setRefreshDate(new Date());
//			cache.setCompanyId(companyId);
//
//			result = dto.getCache();
//
//			try {
//				getAvailableDatabaseAccessController().beginDashboardTransaction();
//				session.save(cache);
//				getAvailableDatabaseAccessController().commitDashboardTransaction();
//			} finally {
//				getAvailableDatabaseAccessController().rollbackDashboardTransactionIfActive();
//			}
//		} else {
//			result = new JSONObject(cache.getCache());
//		}
		result = new JSONObject(cache.getCache());
		return result;
	}

	public static DashboardMetricCache getDashboardMetricCache(Session session, int companyId) {
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<DashboardMetricCache> criteria = cb.createQuery(DashboardMetricCache.class);
		Root<DashboardMetricCache> root = criteria.from(DashboardMetricCache.class);
		criteria.select(root).where(cb.equal(root.get("companyId"), companyId));
		DashboardMetricCache cache = session.createQuery(criteria).uniqueResult();
		return cache;
	}

	public static GameDashboardCache getGameDashboardMetricCache(Session session, short gameId) {
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<GameDashboardCache> criteria = cb.createQuery(GameDashboardCache.class);
		Root<GameDashboardCache> root = criteria.from(GameDashboardCache.class);
		criteria.select(root).where(cb.equal(root.get("gameId"), gameId));
		GameDashboardCache cache = session.createQuery(criteria).uniqueResult();
		return cache;
	}

	private IDatabaseAccessController getAvailableDatabaseAccessController() {
		if (dbAccessController == null) {
			Log.out("No DatabaseAccessController  found, using DatabaseAccessControllerImpl");
			dbAccessController = new DatabaseAccessControllerImpl();
			dbAccessController.beginDashboardTransaction();
		}
		return dbAccessController;
	}

	public JSONObject getGameDashboardWidgetMetricCache(short gameId) {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();

		GameDashboardCache cache = getGameDashboardMetricCache(session, gameId);
		JSONObject result;

//		if (cache == null) {
//			cache = new GameDashboardCache();
//		}
//		cacheController.validate(cache);
//		if (cache.getRefreshDate() == null) {
//			DashboardCacheDTO dto = new DashboardCacheDTO(new JSONObject());
//
//			cache.setCache(dto.getCache().toString());
//			cache.setRefreshDate(new Date());
//			cache.setGameId(gameId);
//
//			result = dto.getCache();
//
//			try {
//				getAvailableDatabaseAccessController().beginDashboardTransaction();
//				session.save(cache);
//				getAvailableDatabaseAccessController().commitDashboardTransaction();
//			} finally {
//				getAvailableDatabaseAccessController().rollbackDashboardTransactionIfActive();
//			}
//		} else {
//			result = new JSONObject(cache.getCache());
//		}
//		
		result = new JSONObject(cache != null ? cache.getCache() : "{}");

		return result;
	}

	public Segment getLazySegmentById(int segmentId) {
		Segment result = userController.getUser().getCompany().getSegmentById(segmentId);

		return result;
	}

	public Segment archivateSegment(int segmentId) {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();
		Segment segment = userController.getUser().getCompany().getSegmentById(segmentId);

		Transaction transaction = session.beginTransaction();
		segment.setArchived(true);
		transaction.commit();

		return segment;
	}

	public void saveSegment(Segment segment) {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();

		User user = userController.getUser();

		Transaction transaction = session.beginTransaction();
		user.getCompany().getSegments().add(segment);
		segment.setCompanyId(user.getCompany().getId());
		session.save(segment.getFilter());
		session.save(segment);
		transaction.commit();
	}

	public Segment getSegment(int segmentId, boolean unproxy) {
		Segment segment = userController.getUser().getCompany().getSegmentById(segmentId);

		if (unproxy) {
			FilterHub filter = segment.getFilter();
			segment.setFilter(Hibernate.unproxy(filter));

			filter.getCountries().forEach(i -> i = Hibernate.unproxy(i));
			filter.getPlatforms().forEach(i -> i = Hibernate.unproxy(i));
			filter.getOses().forEach(i -> i = Hibernate.unproxy(i));
			filter.getDevices().forEach(i -> i = Hibernate.unproxy(i));
			filter.getRealms().forEach(i -> i = Hibernate.unproxy(i));
		}

		return segment;
	}

	public Game getGameById(short gameId) {
		Game game = userController.getUser().getCompany().getGameById(gameId);
		return game;
	}

	public List<Segment> getSegmentsByGame(short gameId) {
		User user = userController.getUser();
		List<Segment> segments = user.getCompany().getSegments().stream()
				.filter(s -> s.getFilter().getGameId() == gameId).collect(Collectors.toList());
		return segments;
	}

	public AbTest getAbtest(int abtestId, boolean unproxy) {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<AbTest> criteria = cb.createQuery(AbTest.class);
		Root<AbTest> root = criteria.from(AbTest.class);
		criteria.select(root).where(cb.equal(root.get("id"), abtestId));
		AbTest abtest = session.createQuery(criteria).uniqueResult();
		if (unproxy) {
			abtest.getGroups().forEach(g -> g = Hibernate.unproxy(g));
		}

		return abtest;
	}

	public List<AbTest> getAbtests() {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<AbTest> criteria = cb.createQuery(AbTest.class);
		Root<AbTest> root = criteria.from(AbTest.class);
		criteria.select(root);
		List<AbTest> abtests = session.createQuery(criteria).getResultList();
		abtests.forEach(ab -> ab.getGroups().forEach(g -> g = Hibernate.unproxy(g)));

		return abtests;
	}

	public void archivateAbtest(int abtestId) {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<AbTest> criteria = cb.createQuery(AbTest.class);
		Root<AbTest> root = criteria.from(AbTest.class);
		criteria.select(root).where(cb.equal(root.get("id"), abtestId));
		AbTest abtest = session.createQuery(criteria).uniqueResult();
		abtest.setArchived(true);

		Transaction transaction = session.beginTransaction();
		session.update(abtest);
		transaction.commit();

	}

	public void addAbtest(AbTest abtest) {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();

		User user = userController.getUser();

		Transaction transaction = session.beginTransaction();
		user.getCompany().getGameById(abtest.getGameId()).getAbtests().add(abtest);
		session.save(abtest);
		transaction.commit();

	}

	public List<Segment> getActiveSegments() {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();
		List<Segment> segments = userController.getUser().getCompany().getActiveSegments();

		return segments;
	}

	public void updateSegment(Segment segment) {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();

		Transaction transaction = session.beginTransaction();
		session.merge(segment);
		transaction.commit();

	}

	public void addDashboardWidget(Widget widget, FilterHub filter) {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();
		User user = userController.getUser();

		Transaction transaction = session.beginTransaction();
		user.getCompany().getWidgets().add(widget);
		widget.setCompany(user.getCompany());
		session.persist(filter);
		widget.setFilter(filter);
		session.persist(widget);
		transaction.commit();

	}

	public void addGameDashboardWidget(GameWidget widget, FilterHub filter, short gameId) {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();
		User user = userController.getUser();
		Game game = user.getCompany().getGameById(gameId);

		Transaction transaction = session.beginTransaction();
		game.getWidgets().add(widget);
		widget.setGame(game);
		session.save(filter);
		session.save(widget);
		transaction.commit();

	}

	public JSONArray getDashboardWidgetCharts() {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();

		List<Widget> widgets = getDashboardWidgets();
		JSONArray result = new JSONArray();

		for (Widget widget : widgets) {
			// TODO validate cache
			if (widget.getCache() == null || widget.getCache().isEmpty()) {
				widget.initMetric();
				Session hiveSession = getAvailableDatabaseAccessController().getHiveSession();

				widget.getMetric().performQuery(hiveSession);

				HiveDAO.closeSession(hiveSession);

				widget.getMetric().getChart().formJsonData();
				widget.getMetric().getChart().formJsonConfig();
				widget.setCache(widget.getMetric().getChart().getData().toString());

				result.put(widget.getMetric().getChart().toJson());

				Transaction transaction = session.beginTransaction();
				session.update(widget);
				transaction.commit();
			} else {
				// TODO
				widget.initMetric();
				widget.getMetric().getChart().setData(widget.getCache());
				widget.getMetric().getChart().formJsonConfig();
				widget.setCache(widget.getMetric().getChart().getData().toString());

				result.put(widget.getMetric().getChart().toJson());
			}
		}

		return result;
	}

	public JSONArray getGameDashboardWidgetCharts(short gameId) {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();

		List<GameWidget> widgets = getGameDashboardWidgets(gameId);
		JSONArray result = new JSONArray();

		for (GameWidget widget : widgets) {
			// TODO validate cache
			if (widget.getCache() == null || widget.getCache().isEmpty()) {
				widget.initMetric();
				Session hiveSession = getAvailableDatabaseAccessController().getHiveSession();

				widget.getMetric().performQuery(hiveSession);

				HiveDAO.closeSession(hiveSession);

				widget.getMetric().getChart().formJsonData();
				widget.getMetric().getChart().formJsonConfig();
				widget.setCache(widget.getMetric().getChart().getData().toString());

				result.put(widget.getMetric().getChart().toJson());

				Transaction transaction = session.beginTransaction();
				session.update(widget);
				transaction.commit();
			} else {
				// TODO
				widget.initMetric();
				widget.getMetric().getChart().setData(widget.getCache());
				widget.getMetric().getChart().formJsonConfig();
				widget.setCache(widget.getMetric().getChart().getData().toString());

				result.put(widget.getMetric().getChart().toJson());
			}
		}

		return result;
	}

	@SuppressWarnings({ "resource", "boxing" })
	public List<Widget> getDashboardWidgets() {
		Session session = dbAccessController.getDashboardSession();

		User user = userController.getUser();

		if (user == null) {
			return null;
		}
		user = session.get(User.class, user.getId());

		List<Widget> widgets = user.getCompany().getWidgets();
		widgets = widgets.stream().map(w -> w = Hibernate.unproxy(w)).collect(Collectors.toList());
		return widgets;

	}

	public void updateDashWidget(Widget widget) {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();

		Transaction transaction = session.beginTransaction();
		session.update(widget);
		transaction.commit();

	}

	public void deleteDashboardWidget(int widgetId) {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();

		User user = userController.getUser();

		Transaction transaction = session.beginTransaction();
		Widget widget = session.get(Widget.class, widgetId);
		session.delete(widget);
		user.getCompany().getWidgets().remove(widget);
		transaction.commit();

	}

	public void deleteGameDashboardWidget(short gameId, int widgetId) {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();

		Game game = userController.getUser().getCompany().getGameById(gameId);
		GameWidget widget = game.getWidgetById(widgetId);

		Transaction transaction = session.beginTransaction();
		widget = session.get(GameWidget.class, widgetId);
		game.getWidgets().remove(widget);
		session.delete(widget);
		transaction.commit();

	}

	public List<GameWidget> getGameDashboardWidgets(short gameId) {
		Session session = dbAccessController.getDashboardSession();
		session = getAvailableDatabaseAccessController().getDashboardSession();

		User user = userController.getUser();

		if (user == null) {
			return null;
		}
		user = session.get(User.class, user.getId());

		List<GameWidget> widgets = user.getCompany().getGameById(gameId).getWidgets();
		widgets = widgets.stream().map(w -> w = Hibernate.unproxy(w)).collect(Collectors.toList());
		return widgets;
	}

	public AbtestCache getAbtestCache(AbTest abtest, byte metricType) {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<AbtestCache> criteria = cb.createQuery(AbtestCache.class);
		Root<AbtestCache> root = criteria.from(AbtestCache.class);
		Predicate equalByAbtest = cb.equal(root.get("abtestId"), abtest.getId());
		Predicate equalByMetric = cb.equal(root.get("metricType"), metricType);
		Predicate and = cb.and(equalByAbtest, equalByMetric);
		criteria.select(root).where(and);

		AbtestCache cache = session.createQuery(criteria).uniqueResult();

		return cache;
	}

	public List<AbtestCache> getAllAbtestCache() {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<AbtestCache> criteria = cb.createQuery(AbtestCache.class);
		Root<AbtestCache> root = criteria.from(AbtestCache.class);
		criteria.select(root);

		List<AbtestCache> cache = session.createQuery(criteria).getResultList();

		return cache;
	}

	public void saveAbtestCaches(int abtestId) {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();
		Transaction transaction = session.beginTransaction();

		MetricType.getAbtestMetricTypes().forEach(type -> {
			AbtestCache cache = new AbtestCache();
			cache.setAbtestId(abtestId);
			cache.setMetricType(type);
			cache.setRefreshed(new Date());
			session.save(cache);
		});

		transaction.commit();

	}

	public void updateAbtestCache(AbtestCache cache) {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();
		Transaction transaction = session.beginTransaction();

		session.update(cache);

		transaction.commit();

	}

	public SegmentCache getSegmentCache(Segment segment, byte metricType) {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<SegmentCache> criteria = cb.createQuery(SegmentCache.class);
		Root<SegmentCache> root = criteria.from(SegmentCache.class);
		Predicate equalByAbtest = cb.equal(root.get("segmentId"), segment.getId());
		Predicate equalByMetric = cb.equal(root.get("metricType"), metricType);
		Predicate and = cb.and(equalByAbtest, equalByMetric);
		criteria.select(root).where(and);

		SegmentCache cache = session.createQuery(criteria).uniqueResult();

		return cache;
	}

	public List<SegmentCache> getAllSegmentCache() {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<SegmentCache> criteria = cb.createQuery(SegmentCache.class);
		Root<SegmentCache> root = criteria.from(SegmentCache.class);
		criteria.select(root);

		List<SegmentCache> cache = session.createQuery(criteria).getResultList();

		return cache;
	}

	public void saveSegmentCache(int segmentId) {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();
		Transaction transaction = session.beginTransaction();

		MetricType.getSegmentMetricTypes().forEach(type -> {
			SegmentCache cache = new SegmentCache();
			cache.setSegmentId(segmentId);
			cache.setMetricType(type);
			session.save(cache);
		});

		transaction.commit();

	}

	public void updateSegmentCache(SegmentCache cache) {
		Session session = getAvailableDatabaseAccessController().getDashboardSession();
		Transaction transaction = session.beginTransaction();

		session.update(cache);

		transaction.commit();

	}
}
