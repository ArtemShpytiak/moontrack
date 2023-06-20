package com.moonmana.moontrack;

import javax.servlet.ServletContextEvent;

import core.tomcat.TomcatModule;

public class MoontrackTomcatModule extends TomcatModule {
	private CacheUpdater cacheUpdater;

	public MoontrackTomcatModule() {
		super();
		cacheUpdater = new CacheUpdater();
	}

	@Override
	public void init(ServletContextEvent event) {
		super.init(event);
		cacheUpdater.forcePerform();
		cacheUpdater.start();
	}

	@Override
	public void destroy() {
		super.destroy();
		if (cacheUpdater != null) {
			cacheUpdater.stop();
		}
	}

	public CacheUpdater getCacheUpdater() {
		return cacheUpdater;
	}

}
