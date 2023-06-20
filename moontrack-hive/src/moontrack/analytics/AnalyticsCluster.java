package moontrack.analytics;

import java.io.Serializable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.moonmana.log.Log;

import core.cluster.IChannelProvider;
import core.cluster.core.Cluster;
import core.cluster.core.InMemoryStorageTrackResultHandler;
import core.cluster.core.manager.ServiceManager;
import core.tomcat.ServerUndeployServicesDestroyedEvent;

public class AnalyticsCluster extends Cluster {

	Queue<Serializable> unsent = new ConcurrentLinkedQueue<>();
	private short realmId;
	private short gameId;

	public static AnalyticsCluster instance = null;

	public AnalyticsCluster(ServiceManager serviceManager, short gameId) {
		super(serviceManager, "analytics", "analytics");
		this.gameId = gameId;
		trackResultHandler = new InMemoryStorageTrackResultHandler("analytics");
		if (instance == null) {
			instance = this;
		}
	}

	public short getRealmId() {
		return realmId;
	}

	public short getGameId() {
		return gameId;
	}

	@Override
	public void init(IChannelProvider provider) {
		Log.out("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Inited analytics cluster sender!!!!!!!!!!!!!!!!!!!!");
		super.init(provider);
	}

	void destroy(ServerUndeployServicesDestroyedEvent event) {
		onShutdown();
	}

	public void setRealmId(short realmId) {
		this.realmId = realmId;
	}
}