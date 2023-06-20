package moontrack.events.abtest;

public class AbtestEditedEvent {

	public int id;

	public AbtestEditedEvent(AbtestEditedEvent event) {
		this.id = event.id;
	}
}
