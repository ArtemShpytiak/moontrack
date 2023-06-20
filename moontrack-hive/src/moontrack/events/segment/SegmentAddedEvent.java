package moontrack.events.segment;

import com.moonmana.events.IEvent;

import hive.model.segment.Segment;

public class SegmentAddedEvent implements IEvent {
	private Segment segment;

	public SegmentAddedEvent(Segment segment) {
		this.segment = segment;
	}

	public Segment getSegment() {
		return segment;
	}

	public void setSegment(Segment segment) {
		this.segment = segment;
	}
}
