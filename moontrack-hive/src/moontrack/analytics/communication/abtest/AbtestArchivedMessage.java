package moontrack.analytics.communication.abtest;

import java.io.Serializable;

import core.cluster.core.CommunicationMessage;
import core.cluster.core.MessageProcessor;
import moontrack.events.abtest.AbtestArchivedEvent;

public class AbtestArchivedMessage extends CommunicationMessage implements Serializable {

	private static final long serialVersionUID = 7003456263692108970L;

	private int id;

	public AbtestArchivedMessage(AbtestArchivedEvent event) {
		this.id = event.id;
	}

	@Override
	public MessageProcessor<?> getMessageProcessor() {
		return new AbtestArchivedProcessor(this);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
