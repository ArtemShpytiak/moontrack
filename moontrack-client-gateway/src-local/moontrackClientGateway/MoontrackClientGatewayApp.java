package moontrackClientGateway;

import java.util.HashMap;
import java.util.Map;

import com.moonmana.app.App;

import actions.MoontrackCGActionFactory;
import core.action.ActionFactory;
import core.cluster.core.ApplicationService;
import core.cluster.core.manager.IServiceApp;
import core.cluster.core.manager.ServiceFactory;
import core.tomcat.ITomcatApp;
import core.tomcat.TomcatModule;
import gateway.MoontrackGatewayService;

public class MoontrackClientGatewayApp extends App implements ITomcatApp, IServiceApp {
	
	private TomcatModule tomcatModule = new TomcatModule();

	public MoontrackClientGatewayApp() {
		super();
		tomcatModule.addListener(new MoontrackGatewayTomcatListener());
	}

	@Override
	public TomcatModule getTomcatModule() {
		return tomcatModule;
	}

	@Override
	public ActionFactory createActionFactory() {
		return new MoontrackCGActionFactory();
	}

	@Override
	public ServiceFactory getServiceFactory() {
		
		return new ServiceFactory() {
			@Override
			protected Map<String, Class<? extends ApplicationService>> getServiceClasses() {
				Map<String, Class<? extends ApplicationService>> result = new HashMap<>();
				result.put("moontrackGateway", MoontrackGatewayService.class);
				return result;
			}
		};
	}
}
