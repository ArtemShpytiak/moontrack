package moontrack.analytics.communication;

import java.util.Date;

import core.action.exceptions.InvalidActionException;
import core.cluster.core.MessageProcessor;
import moontrack.analytics.AnalyticsCluster;

public class SessionCloseMessage extends AnalyticsMessage {
	private static final long serialVersionUID = 8181116339542529734L;
	private Date endDate;
	private int numUpdates;

	public SessionCloseMessage(int realmUserId, AnalyticsCluster cluster, Date lastUpdateActionDate,
			int numUpdates) {
		super(realmUserId, cluster, lastUpdateActionDate);
		this.endDate = lastUpdateActionDate;
		this.numUpdates = numUpdates;
	}

	@Override
	public MessageProcessor<?> getMessageProcessor() {
		return new AnalyticsMessageProccessor<SessionCloseMessage>(this) {
			@Override
			protected void perform() throws InvalidActionException {
				trackSessionClosed(endDate, numUpdates);
			}
	};
	}

	@Override
	protected String subClassFields() {
		
		return "endDate=" + endDate + ", numUpdates=" + numUpdates;
	}
}
