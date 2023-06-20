package moontrack.events.segment;

import com.moonmana.events.IEvent;

public class SegmentArchivedEvent implements IEvent {

	public int id;

	public SegmentArchivedEvent(int id) {
		this.id = id;
	}
}
