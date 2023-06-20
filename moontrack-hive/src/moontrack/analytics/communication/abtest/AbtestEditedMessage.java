package moontrack.analytics.communication.abtest;

import java.io.Serializable;

import core.cluster.core.CommunicationMessage;
import core.cluster.core.MessageProcessor;
import moontrack.events.abtest.AbtestEditedEvent;

public class AbtestEditedMessage extends CommunicationMessage implements Serializable {

	private static final long serialVersionUID = 7003456263692108970L;
	private int id;

	public AbtestEditedMessage(AbtestEditedEvent event) {
		this.id  = event.id;
	}

	@Override
	public MessageProcessor<?> getMessageProcessor() {
		return new AbtestEditedProcessor(this);
	}

	public int getId() {
		return id;
	}

}
