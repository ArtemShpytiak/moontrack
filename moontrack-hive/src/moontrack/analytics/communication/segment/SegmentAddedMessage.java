package moontrack.analytics.communication.segment;

import java.io.Serializable;

import core.cluster.core.CommunicationMessage;
import core.cluster.core.MessageProcessor;
import hive.model.segment.Segment;
import moontrack.events.segment.SegmentAddedEvent;

public class SegmentAddedMessage extends CommunicationMessage implements Serializable {

	private static final long serialVersionUID = 5474933502045116986L;

	private Segment segment;

	public SegmentAddedMessage(SegmentAddedEvent event) {
		this.segment = event.getSegment();
	}

	@Override
	public MessageProcessor<?> getMessageProcessor() {
		return new SegmentAddedProcessor(this);
	}

	public Segment getSegment() {
		return segment;
	}

	public void setSegment(Segment segment) {
		this.segment = segment;
	}

}
