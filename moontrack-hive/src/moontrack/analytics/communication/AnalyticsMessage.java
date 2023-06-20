package moontrack.analytics.communication;

import java.io.Serializable;
import java.util.Date;

import com.moonmana.utils.TimeUtils;

import core.cluster.core.CommunicationMessage;
import moontrack.analytics.AnalyticsCluster;

public abstract class AnalyticsMessage extends CommunicationMessage implements Serializable {
	private static final long serialVersionUID = -2891678100506660784L;

	private int realmUserId;
	private short realmId;
	private short gameId;
	private Date eventDate; 
	
	public AnalyticsMessage(int realmUserId, AnalyticsCluster cluster, Date eventDate) {
		this(realmUserId, cluster.getRealmId(), cluster.getGameId(), eventDate);
	}
	
	public Date getEventDate() {
		return eventDate;
	}
	
	public AnalyticsMessage(int realmUserId, short realmId, short gameId, Date eventDate) {
		super();
		this.realmUserId = realmUserId;
		this.realmId = realmId;
		this.gameId = gameId;
		this.eventDate = eventDate;
	}

	public int getRealmUserId() {
		return realmUserId;
	}
	
	public short getRealmId() {
		return realmId;
	}
	
	public short getGameId() {
		return gameId;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [realmUserId=" + realmUserId + ", realmId=" + realmId
				+ ", gameId=" + gameId 
				+ ", eventDate=" + TimeUtils.timestampFormat.format(eventDate) 
				+ ", " + subClassFields() +"]";
	}
	
	protected abstract String subClassFields();
}

