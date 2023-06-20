package com.moonmana.moontrack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import com.google.common.util.concurrent.Striped;
import com.moonmana.log.Log;
import com.moonmana.moontrack.dbAccess.DatabaseAccessControllerImpl;
import com.moonmana.moontrack.model.Company;
import com.moonmana.moontrack.model.Game;

import core.daemon.Daemon;

public class CacheUpdater extends Daemon {

	private static final byte PERIOD_MINUTES = 5;
	
	private static int NUM_SUBTHREADS = 6;
	private static int NUM_SUBTHREADS_IN_LOGIC = 4;
	private static final Striped<Lock> gameLocks = Striped.lock(Runtime.getRuntime().availableProcessors() * 4);
	private static final Striped<Lock> companyLocks = Striped.lock(Runtime.getRuntime().availableProcessors() * 4);

	// TODO: Remove hardcode
	@Override
	protected void perform() {
		long t1 = System.currentTimeMillis();
		ExecutorService executor = createExecutor();
		DatabaseAccessControllerImpl dbac = new DatabaseAccessControllerImpl();
		try {
			List<Company> companies = getCompaniesList(dbac);			
			for (Company company : companies) {
				updateCacheForCompany(company, executor, NUM_SUBTHREADS_IN_LOGIC);
				Set<Short> games = getGameIds(company);
				for (short gameId : games) {
					updateCacheForGame(gameId, executor, NUM_SUBTHREADS_IN_LOGIC);
				}
			}
		} finally {
			dbac.close();
		}
		waitUntilComplete(executor);
		long t2 = System.currentTimeMillis();
		Log.out("Cache update performed in " + (t2 - t1) + " ms.");
	}

	private ExecutorService createExecutor() {
		return (NUM_SUBTHREADS > 0) 
				? Executors.newFixedThreadPool(NUM_SUBTHREADS)
				: Executors.newCachedThreadPool();
	}

	private void updateCacheForCompany(Company company, ExecutorService executor, int numSubthreads) {
		int companyId = company.getId();
		Lock lock = companyLocks.get(companyId);
		executor.submit(() -> {
			CacheUpdateLogic сacheUpdateLogic = new CacheUpdateLogic(numSubthreads);
			try {
				lock.lock();
				сacheUpdateLogic.updateDashboardWidgetMetricCache(companyId);
			} catch (Throwable e) {
				Log.outError("Exception happened while updating cache for company " + companyId, e);
			} finally {
				lock.unlock();
				сacheUpdateLogic.destroy();
			}
		});
	}

	private void updateCacheForGame(short gameId, ExecutorService executor, int numSubthreads) {
		Lock lock = gameLocks.get(gameId);
		executor.submit(() -> {
			CacheUpdateLogic gameCacheUpdateLogic = new CacheUpdateLogic(numSubthreads);
			try {
				lock.lock();
				gameCacheUpdateLogic.updateGameDashboardWidgetMetricCache(gameId);
			} catch (Throwable e) {
				Log.outError("Exception happened while updating cache for game " + gameId, e);
			} finally {
				gameCacheUpdateLogic.destroy();
				lock.unlock();
			}
		});
	}

	private static Set<Short> getGameIds(Company company) {
		List<Game> games = company.getGames();
//		Set<Short> gameIds = new HashSet<>();
		Set<Short> gameIds = games.stream().map((game) -> game.getId())
				.collect(Collectors.toCollection(() -> new HashSet<>()));
//		if (company.getId() == 1) { // This is HARDCODE!!!!
//			gameIds.add((short) 1);
//			gameIds.add((short) 3);
//			gameIds.add((short) 5);
//		}
		return gameIds;
	}

	private List<Company> getCompaniesList(DatabaseAccessControllerImpl daci) {
		Session session = daci.getDashboardSession();
		
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Company> criteria = cb.createQuery(Company.class);
		Root<Company> root = criteria.from(Company.class);
		
		List<Company> list = session.createQuery(criteria).list();
		return list;
	}

	@Override
	protected boolean exitCondition() {
		return false;
	}

	@Override
	protected long getDelayMS() {
		return TimeUnit.MINUTES.toMillis(PERIOD_MINUTES);
	}

	public void updateCacheForGame(short gameId) {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		updateCacheForGame(gameId, executor, 6);
	//	waitUntilComplete(executor);
	}

	private void waitUntilComplete(ExecutorService executor) {
		try {
			executor.shutdown();
			while (!executor.awaitTermination(4, TimeUnit.SECONDS)) {
				
			}
		} catch (InterruptedException e) {
			Log.outError("!!!!!!!!!!!!!!!!!!", e);
		}
	}
}
