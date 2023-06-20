package moontrack.analytics.communication.segment;

import java.io.Serializable;

import core.cluster.core.CommunicationMessage;
import core.cluster.core.MessageProcessor;
import hive.model.segment.Segment;
import moontrack.events.segment.SegmentEditedEvent;

public class SegmentEditedMessage extends CommunicationMessage implements Serializable {

	private static final long serialVersionUID = -2939608802260375473L;

	private Segment segment;

	public SegmentEditedMessage(SegmentEditedEvent event) {
		this.segment = event.segment;
	}

	@Override
	public MessageProcessor<?> getMessageProcessor() {
		return new SegmentEditedProcessor(this);
	}

	public Segment getSegment() {
		return segment;
	}

	public void setSegment(Segment segment) {
		this.segment = segment;
	}

}
