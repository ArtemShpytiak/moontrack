package moontrack.events.abtest;

import com.moonmana.events.IEvent;

public class AbtestArchivedEvent implements IEvent {

	public int id;

	public AbtestArchivedEvent(int id) {
		this.id = id;
	}
}
