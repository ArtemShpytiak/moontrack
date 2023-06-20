package com.moonmana.moontrack;

import java.util.Map;

import com.moonmana.app.App;
import com.moonmana.log.Log;
import com.moonmana.moontrack.clusters.DashboardService;
import com.moonmana.utils.MapBuilder;

import core.action.ActionFactory;
import core.cluster.core.ApplicationService;
import core.cluster.core.manager.IServiceApp;
import core.cluster.core.manager.ServiceFactory;
import core.cluster.core.manager.ServiceManager;
import core.hibernate.ISessionMonitorProvider;
import core.hibernate.SessionMonitor;
import core.tomcat.ITomcatApp;
import core.tomcat.TomcatModule;
import moontrack.analytics.AnalyticsCluster;
import moontrack.analytics.MoontrackEventListener;
import moontrack.analytics.MoontrackEventsService;
import moontrack.analytics.MoontrackService;

public class DashboardApp extends App implements ITomcatApp, ISessionMonitorProvider, IServiceApp {
	private TomcatModule tomcatModule = new MoontrackTomcatModule();

	public DashboardApp() {
		Log.out("Dashboard called!!");
		tomcatModule.addListener(new MoontrackDashboardConfigListener());
		MoontrackEventListener listener = new MoontrackEventListener(new AnalyticsCluster(ServiceManager.getInstance(), (short) -360));
		eventProcessor().addEventListener(listener);
	}

	@Override
	public TomcatModule getTomcatModule() {
		return tomcatModule;
	}

	@Override
	public ActionFactory createActionFactory() {
		return null;
	}

	@Override
	public SessionMonitor getSessionMonitor() {
		return tomcatModule.getSessionMonitor();
	}

	@Override
	public ServiceFactory getServiceFactory() {
		return new ServiceFactory() {
			private final Map<String, Class<? extends ApplicationService>> serviceClasses = new MapBuilder<String, Class<? extends ApplicationService>>()
					.put("moontrack", MoontrackService.class).put("dashboard", DashboardService.class).build();

			@Override
			public ApplicationService create(String serviceName) {
				Class<? extends ApplicationService> serviceClass = serviceClasses.get(serviceName);
				if (serviceClass == null) {
					Log.outError("No class for " + serviceName);
					return null;
				}
				try {
					ApplicationService result = serviceClass.newInstance();
					if (result instanceof MoontrackService) {
						MoontrackService moontrackService = (MoontrackService) result;
						moontrackService.setReadOnly(true);
					}
					return result;
				} catch (InstantiationException | IllegalAccessException e) {
					Log.outError("While creating instance of " + serviceClass.toString(), e);

				}
				return null;
			}

			@Override
			protected Map<String, Class<? extends ApplicationService>> getServiceClasses() {
				return new MapBuilder<String, Class<? extends ApplicationService>>()
						.put("analytics", MoontrackService.class)
						.put("moontrack", MoontrackService.class)
						.put("moontrackEvents", MoontrackEventsService.class)
						//.put("moontrackGateway", MoontrackGatewayService.class)
						.build();
			}
		};
	}

}
