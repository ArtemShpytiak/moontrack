package com.moonmana.moontrack;

import java.math.BigInteger;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.hibernate.Session;
import org.json.JSONObject;

import com.moonmana.OS;
import com.moonmana.PlatformID;
import com.moonmana.log.Log;
import com.moonmana.moontrack.dbAccess.DatabaseAccessControllerImpl;
import com.moonmana.moontrack.dto.DashboardCacheDTO;
import com.moonmana.moontrack.metric.Metric;
import com.moonmana.moontrack.metric.MetricType;
import com.moonmana.moontrack.metrics.Arpdau;
import com.moonmana.moontrack.metrics.Dau;
import com.moonmana.moontrack.metrics.InstallsTotal;
import com.moonmana.moontrack.metrics.Registrations;
import com.moonmana.moontrack.metrics.Retention;
import com.moonmana.moontrack.metrics.RevenueDaily;
import com.moonmana.moontrack.metrics.RevenueTotal;
import com.moonmana.moontrack.model.cache.DashboardMetricCache;
import com.moonmana.moontrack.model.cache.GameDashboardCache;
import com.moonmana.moontrack.repositories.DashboardDAO;

import hive.model.filter.FilterHub;
import hive.model.filter.FilterOs;
import hive.model.filter.FilterPlatform;

@SuppressWarnings("resource")
public class CacheUpdateLogic {
	DatabaseAccessControllerImpl dbAccessController = new DatabaseAccessControllerImpl(); 
	private static final Set<Entry<Byte, Byte>> filterKeys = generatePlatformAndOsKeys();
	private final int numSubthreads;
	
	public CacheUpdateLogic(int numSubthreads) {
		this.numSubthreads = numSubthreads;
	}

	public void updateDashboardWidgetMetricCache(int companyId) {
		Session session = dbAccessController.getDashboardSession();
		DashboardMetricCache cache = DashboardDAO.getDashboardMetricCache(session, companyId);
		if (cache == null) {
			cache = new DashboardMetricCache();
			cache.setCompanyId(companyId);
		}
		
		DashboardCacheDTO dto = new DashboardCacheDTO(new JSONObject());
		try {
			updateDashboardMetrics(dto, null);
		} catch (InterruptedException | ExecutionException e) {
			Log.outError("", e);
			return;
			
		}
		dbAccessController.beginDashboardTransaction();
		cache.setCache(dto.getCache().toString());
		session.saveOrUpdate(cache);
		dbAccessController.commitDashboardTransaction();

	}
	
	private ExecutorService createExecutor() {
		return (numSubthreads > 0) 
				? Executors.newFixedThreadPool(numSubthreads)
				: Executors.newCachedThreadPool();
	}

	public void updateGameDashboardWidgetMetricCache(short gameId) {
		Session session = dbAccessController.getDashboardSession();
		GameDashboardCache gdcache = DashboardDAO.getGameDashboardMetricCache(session, gameId);
		if (gdcache == null) {
			gdcache = new GameDashboardCache();
			gdcache.setGameId(gameId);
		}
		
		DashboardCacheDTO dto = new DashboardCacheDTO(new JSONObject());
		try {
			updateDashboardMetrics(dto, gameId);
		} catch (InterruptedException | ExecutionException e) {
			Log.outError("", e);
			return;
		}
		
		dbAccessController.beginDashboardTransaction();
		gdcache.setCache(dto.getCache().toString());
		session.saveOrUpdate(gdcache);
		dbAccessController.commitDashboardTransaction();
	}
	
	private static class StatsTracker {
		private long t0;
		private final ArrayList<Long> stats;
		ConcurrentHashMap<String, Long> measurements = new ConcurrentHashMap<>();
		ConcurrentHashMap<String, Long> measureStarts = new ConcurrentHashMap<>();

		public StatsTracker(int initialCap) {
			stats = new ArrayList<>(initialCap);
		}

		public void start() {
			t0 = System.currentTimeMillis();
		}

		public synchronized void mark() {
			stats.add(System.currentTimeMillis());
		}
		
		public String getReport() {
			
			String s = "	Total marks = " + stats.size() + ":\n";
			for (int i = 0; i < stats.size(); i++) {
				s += "			" + (i + 1) + ". " + (stats.get(i) - t0) + ";\n";
			}
			
			for (Entry<String, Long> m : measurements.entrySet()) {
				s += "				" + m.getKey() + " : " + m.getValue() + ";\n";
			}
			
			return s;
		}

		public void start(String string) {
			measureStarts.put(string, System.currentTimeMillis());
			
		}

		public void stop(String string) {
			long tc = System.currentTimeMillis();
			measurements.put(string, measurements.getOrDefault(string, 0L) + (tc - measureStarts.getOrDefault(string, tc)));
		}
	}

	@SuppressWarnings("boxing")
	private void updateDashboardMetrics(DashboardCacheDTO cache, Short gameId) throws InterruptedException, ExecutionException {
		StatsTracker stats = new StatsTracker(50);
		stats.start();
		ExecutorService executor = createExecutor();
		FilterHub filter = new FilterHub(gameId);
		FilterHub todayFilter = new FilterHub(gameId);
		setDate(todayFilter, 0);
		
		Future<BigInteger> registrationsF = executor.submit(() -> {
			Session session = dbAccessController.createNewHiveSession();
			Registrations registrations = new Registrations(todayFilter);
			stats.mark();
			stats.start("regs");
			registrations.performQuery(session);
			stats.stop("regs");
			return registrations.getValueOr(0, BigInteger.valueOf(0));
		});
		
		FilterHub yesterdayFilter = new FilterHub(gameId);
		setDate(yesterdayFilter, -1);
		
		loadRetention(cache, yesterdayFilter, executor, stats);

		Future<Double> revenueDailyF = executor.submit(() -> {
			Session session = dbAccessController.createNewHiveSession();
			RevenueDaily revenue = new RevenueDaily(todayFilter);
			revenue.setLimit(1);
			stats.mark();
			stats.start("revt");
			revenue.performQuery(session);
			stats.stop("revt");
			return revenue.getValueOr(0, 0.0);
		});

		Future<Double> arpdauF = executor.submit(() -> {
			Session session = dbAccessController.createNewHiveSession();
			Arpdau arpdau = new Arpdau(filter);
			arpdau.setLimit(1);
			stats.mark();
			stats.start("arpdau");
			arpdau.performQuery(session);
			stats.stop("arpdau");
			return arpdau.getValueOr(0, 0.0);
		});

		Map<Entry<Byte, Byte>, Future<Integer>> installs = new HashMap<>();  
		Map<Entry<Byte, Byte>, Future<Double>> revenues = new HashMap<>();  
		Map<Entry<Byte, Byte>, Future<Integer>> daus = new HashMap<>();  
		for (Entry<Byte, Byte> filterKey : filterKeys) {
			FilterHub platformFilter = createPlatformFilter(gameId, filterKey);
			Future<Integer> installsF = executor.submit(() -> {
				Session session = dbAccessController.createNewHiveSession();
				Metric metric = new InstallsTotal(platformFilter);
				stats.mark();
				stats.start("instot");
				metric.performQuery(session);
				stats.stop("instot");
				return metric.getValueOr(0, 0);
			});
			installs.put(filterKey, installsF);
			
			Future<Double> revenueF = executor.submit(() -> {
				Session session = dbAccessController.createNewHiveSession();
				Metric metric = new RevenueTotal(platformFilter);
				stats.mark();
				stats.start("revenuetot");
				metric.performQuery(session);
				stats.stop("revenuetot");
				return metric.getValueOr(0, 0.0);
			});
			revenues.put(filterKey, revenueF);
			
			Future<Integer> dauF = executor.submit(() -> {
				Session session = dbAccessController.createNewHiveSession();
				Metric metric = new Dau(platformFilter);
				metric.setLimit(1);
				stats.mark();
				stats.start("dauwtf");
				metric.performQuery(session);
				stats.stop("dauwtf");				
				return metric.getValueOr(0, 0);
			});
			daus.put(filterKey, dauF);
		}
		
		for (Entry<Byte, Byte> filterKey : filterKeys) {
			cache.setInstallsByKey(filterKey, installs.get(filterKey).get().intValue());
			cache.setRevenueTotalByKey(filterKey, revenues.get(filterKey).get().doubleValue());
			cache.setDauTotalByKey(filterKey, daus.get(filterKey).get().intValue());
		}
		
		cache.setRevenueToday(revenueDailyF.get().doubleValue());
		cache.setRegisteredUsersToday(registrationsF.get().intValue());
		cache.setArpdau(arpdauF.get().doubleValue());
		stats.mark();
		
		Log.out(stats.getReport());
		executor.shutdown();
	}

	private void setDate(FilterHub todayFilter, int dayOffset) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		c.add(Calendar.DAY_OF_MONTH, dayOffset);
		
		todayFilter.setDateFrom(c.getTime());
		c.add(Calendar.DAY_OF_MONTH, 1);
		todayFilter.setDateTill(c.getTime());
	}

	private static FilterHub createPlatformFilter(Short gameId, Entry<Byte, Byte> filterKey) {
		FilterHub filter = new FilterHub(gameId);
		if (filterKey.getKey() != null) {
			filter.getPlatforms().add(new FilterPlatform(filterKey.getKey().byteValue()));
		}
		if (filterKey.getValue() != null) {
			filter.getOses().add(new FilterOs(filterKey.getValue().byteValue()));
		}
		return filter;
	}

	@SuppressWarnings("boxing")
	private static Set<Entry<Byte, Byte>> generatePlatformAndOsKeys() {
		Set<Entry<Byte, Byte>> result = new HashSet<>();
		result.add(new AbstractMap.SimpleEntry<>(null, null));
		result.add(new AbstractMap.SimpleEntry<>(PlatformID.APPLE, OS.IOS));
		result.add(new AbstractMap.SimpleEntry<>(PlatformID.APPLE, OS.MAC_OS));
		
		result.add(new AbstractMap.SimpleEntry<>(PlatformID.GOOGLE, null));
		result.add(new AbstractMap.SimpleEntry<>(PlatformID.FACEBOOK, null));
		result.add(new AbstractMap.SimpleEntry<>(PlatformID.WINDOWS_STORE, null));
		result.add(new AbstractMap.SimpleEntry<>(PlatformID.VK, null));
		return result;
	}

	private void loadRetention(DashboardCacheDTO cache, FilterHub filter, ExecutorService executor, StatsTracker stats) throws InterruptedException, ExecutionException {
//		Short gameID = filter.getGameId();
		// TODO Remove this crutch
//		if (gameID == null) {
//			cache.setRetention1d(26);
//			cache.setRetention7d(10);
//			cache.setRetention30d(4);
//		} else if (gameID == 1) {
//			cache.setRetention1d(27);
//			cache.setRetention7d(10);
//			cache.setRetention30d(5);
//		} else if (gameID == 3) {
//			cache.setRetention1d(21);
//			cache.setRetention7d(9);
//			cache.setRetention30d(4);
//		} else if (gameID == 5) {
//			cache.setRetention1d(22);
//			cache.setRetention7d(7);
//			cache.setRetention30d(1);
//		} else {
//			cache.setRetention1d(0);
//			cache.setRetention7d(0);
//			cache.setRetention30d(0);
//		}

		Set<Byte> retentionTypes = new HashSet<>();
		retentionTypes.add(MetricType.RETENTION_1_DAY);
		retentionTypes.add(MetricType.RETENTION_7_DAYS);
		retentionTypes.add(MetricType.RETENTION_30_DAYS);
		
		Map<Byte, Future<Integer>> retentions = new HashMap<>(); 
		
		for (Byte rType : retentionTypes) {
			Session session = dbAccessController.createNewHiveSession();
			Future<Integer> f = executor.submit(() -> {
				Retention metric = new Retention(filter, rType);
				metric.setLimit(1);
				stats.start("reten");
				metric.performQuery(session);
				stats.stop("reten");
				Double doubleVal = metric.getValueOr(0, 0.0);
				return doubleVal.intValue();
			});
			retentions.put(rType, f);
			
		}
		
		for (Entry<Byte, Future<Integer>> entry : retentions.entrySet()) {
			cache.setRetention(entry.getKey(), entry.getValue().get());
		}
	}

	public void destroy() {
		dbAccessController.close();
	}

}
