package moontrack.analytics.communication.abtest;

import java.io.Serializable;

import core.cluster.core.CommunicationMessage;
import core.cluster.core.MessageProcessor;
import hive.model.abtest.AbTest;
import moontrack.events.abtest.AbtestAddedEvent;

public class AbtestAddedMessage extends CommunicationMessage implements Serializable {

	private static final long serialVersionUID = -7982686126717880937L;

	private AbTest abtest;

	public AbtestAddedMessage(AbtestAddedEvent event) {
		this.abtest = event.getAbtest();
	}

	@Override
	public MessageProcessor<?> getMessageProcessor() {
		return new AbtestAddedProcessor(this);
	}

	public AbTest getAbtest() {
		return abtest;
	}

}
