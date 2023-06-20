package analytics;

import java.util.Map;

import com.moonmana.app.App;
import com.moonmana.log.Log;
import com.moonmana.utils.MapBuilder;

import core.action.ActionFactory;
import core.cluster.core.ApplicationService;
import core.cluster.core.manager.IServiceApp;
import core.cluster.core.manager.ServiceFactory;
import core.tomcat.ITomcatApp;
import core.tomcat.TomcatModule;
import moontrack.analytics.MoontrackEventsService;
import moontrack.analytics.MoontrackService;

public class AnalyticsApp extends App implements ITomcatApp, IServiceApp {
	
	private TomcatModule tomcatModule = new TomcatModule();

	public AnalyticsApp() {
		tomcatModule.addListener(new MoontrackTomcatListener());
	}
	
	@Override
	public TomcatModule getTomcatModule() {
		return tomcatModule;
	}

	@Override
	public ActionFactory createActionFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceFactory getServiceFactory() {
		return new ServiceFactory() {
			@Override
			protected Map<String, Class<? extends ApplicationService>> getServiceClasses() {
				Map<String, Class<? extends ApplicationService>> serviceClasses = new MapBuilder<String, Class<? extends ApplicationService>>()
						.put("moontrack", MoontrackService.class)
						.put("analytics", MoontrackService.class)
						.put("moontrackEvents", MoontrackEventsService.class)
						.build();
				return serviceClasses;
			}
		};
	}
}
