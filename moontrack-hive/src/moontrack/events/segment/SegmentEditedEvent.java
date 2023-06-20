package moontrack.events.segment;

import com.moonmana.events.IEvent;

import hive.model.segment.Segment;

public class SegmentEditedEvent implements IEvent {

	public Segment segment;

	public SegmentEditedEvent(Segment segment) {
		this.segment = segment;
	}
}
