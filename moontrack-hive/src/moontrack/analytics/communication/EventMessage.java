package moontrack.analytics.communication;

import java.io.Serializable;
import java.util.Date;

import analytics.model.AnalyticsEvent;
import analytics.model.user.ARealmUser;
import core.action.exceptions.InvalidActionException;
import core.cluster.core.MessageProcessor;
import moontrack.analytics.AnalyticsCluster;

public class EventMessage extends AnalyticsMessage implements Serializable {

	private static final long serialVersionUID = -2055428763143275925L;
	private int type;
	private int value;
	
	public EventMessage(int realmUserId, int type, int value, Date date, AnalyticsCluster cluster) {
		super(realmUserId, cluster, date);
		this.type = type;
		this.value = value;
	}

	@Override
	public MessageProcessor<?> getMessageProcessor() {
		return new AnalyticsMessageProccessor<EventMessage>(this) {
			@Override
			protected void perform() throws InvalidActionException {
				ARealmUser realmUser = getRealmUser();
				if (realmUser != null) {
					AnalyticsEvent event = new AnalyticsEvent(type, value, new Date(), realmUser);
					transaction = session.beginTransaction();
					session.save(event);
					transaction.commit();
				}
			}
			
			@Override
			protected ARealmUser performPartRegistration() {
				TrackRegistrationProcessor registrationProcessor = new TrackRegistrationProcessor(message);
				registrationProcessor.performStatic(session, currentDate, sender);
				return registrationProcessor.getRegisteredUser();
			}
		};
	}

	@Override
	protected String subClassFields() {
		return "type=" + type + ", value=" + value;
	}
	
}
