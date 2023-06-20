package moontrackClientGateway;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.C3P0Registry;
import com.mchange.v2.c3p0.PooledDataSource;
import com.moonmana.log.Log;

import ch.qos.logback.classic.LoggerContext;
import core.cluster.core.manager.ServiceManager;
import core.log.LoggerUtils;
import core.tomcat.ITomcatListener;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.Configuration;

public class MoontrackGatewayTomcatListener implements ITomcatListener {
	private ServiceManager serviceManager = null;
	
	@Override
	public void onTomcatStart() {
		LoggerUtils.loadLoggingConfig();

		try {
			Log.out("");
			Log.out("=============    SERVER START    ================");
			initCache();
			initServices();
		} catch (Throwable e) {
			Log.outError("", e);
			if (e.getCause() != null) {
				Log.outError("", e.getCause());
			}
		}
	}
	
	private void initServices() {
		serviceManager = ServiceManager.getInstance();
		serviceManager.initServices();
	}

	private void initCache() {
		Configuration config = new Configuration();
		CacheManager.create(config);
	}

	@Override
	public void onTomcatStop() {
		serviceManager.destroyServices();

		releaseC3P0();
		releaseCache();
		releaseJDBC();
		releaseLogging();
	}
	
	private void releaseLogging() {
		Log.out("-> stopping logger");
		((LoggerContext) LoggerFactory.getILoggerFactory()).stop();
	}

	private void releaseCache() {
		Log.out("-> shutting down cache manager");
		CacheManager.getInstance().shutdown();
	}

	private void releaseJDBC() {
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			Driver driver = drivers.nextElement();
			try {
				Log.out(String.format("-> deregistering jdbc driver: %s", driver));
				DriverManager.deregisterDriver(driver);
			} catch (SQLException e) {
				Log.outError(String.format("   FAILED deregistering driver %s", driver), e);
			}
		}
	}

	private void releaseC3P0() {
		for (Object o : C3P0Registry.getPooledDataSources()) {
			PooledDataSource dataSource = (PooledDataSource) o;
			try {
				Log.out(String.format("-> deregistering C3P0 pool: size %s", dataSource.getThreadPoolSize()));
				dataSource.close();
				Thread.sleep(500);
			} catch (SQLException e) {
				Log.outError(String.format("   FAILED deregistering C3P0 pool: %s", dataSource.getDataSourceName()), e);
			} catch (InterruptedException e) {
			}
		}
	}

}
