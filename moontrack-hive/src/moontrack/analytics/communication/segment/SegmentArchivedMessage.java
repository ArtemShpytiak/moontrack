package moontrack.analytics.communication.segment;

import java.io.Serializable;

import core.cluster.core.CommunicationMessage;
import core.cluster.core.MessageProcessor;
import moontrack.events.segment.SegmentArchivedEvent;

public class SegmentArchivedMessage extends CommunicationMessage implements Serializable {

	private static final long serialVersionUID = -6314715495436980226L;
	private int id;

	public SegmentArchivedMessage(SegmentArchivedEvent event) {
		this.id = event.id;
	}

	@Override
	public MessageProcessor<?> getMessageProcessor() {
		return new SegmentArchivedProcessor(this);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}