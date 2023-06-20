package gateway;

import java.util.ArrayList;
import java.util.Collection;

import com.moonmana.app.App;

import actions.MoontrackCGActionFactory;
import core.cluster.core.Cluster;
import core.cluster.core.manager.DatabaseService;
import core.cluster.services.config.ServiceEntry;
import core.hibernate.DbInfo;
import core.tomcat.ITomcatApp;
import moontrack.analytics.communication.MoontrackEventsCluster;

public class MoontrackGatewayService extends DatabaseService {

	public MoontrackGatewayService() {
		super("moontrackgateway");
	}

	@Override
	public DbInfo getDefaultDbInfo() {
		return new DbInfo("moontrack_gateway", "moontrack_gateway");
	}

	@Override
	protected void innerInit(ServiceEntry entry) {
		super.innerInit(entry);
		App app = App.instance();
		if(app instanceof ITomcatApp) {
		//	app.get
		}
		new MoontrackCGActionFactory();
	}

	@Override
	public Collection<Class<? extends Cluster>> getClustersInUse() {
		Collection<Class<? extends Cluster>> result = new ArrayList<>(1);
		result.add(MoontrackEventsCluster.class);
		return result;
	}

	@Override
	public String[] getGroupTypes() {
		return new String[] {"gateway"};
	}

}
