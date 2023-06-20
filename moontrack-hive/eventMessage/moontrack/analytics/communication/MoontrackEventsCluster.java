package moontrack.analytics.communication;

import com.moonmana.log.Log;

import core.cluster.IChannelProvider;
import core.cluster.core.Cluster;
import core.cluster.core.InMemoryStorageTrackResultHandler;
import core.cluster.core.manager.ServiceManager;

public class MoontrackEventsCluster extends Cluster {

	public static MoontrackEventsCluster instance = null;

	public MoontrackEventsCluster(ServiceManager serviceManager) {
		super(serviceManager, "moontrackevents", "moontrackevents");
		trackResultHandler = new InMemoryStorageTrackResultHandler("moontrackevents");
		if (instance == null) {
			instance = this;
		}
	}
	
	@Override
	public void init(IChannelProvider provider) {
		Log.out("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Inited MoontrackEvents cluster sender!!!!!!!!!!!!!!!!!!!!");
		super.init(provider);
	}

}
