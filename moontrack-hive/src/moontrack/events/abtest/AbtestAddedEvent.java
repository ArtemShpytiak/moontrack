package moontrack.events.abtest;

import com.moonmana.events.IEvent;

import hive.model.abtest.AbTest;

public class AbtestAddedEvent implements IEvent {
	private AbTest abtest;

	public AbtestAddedEvent(AbTest abtest) {
		this.abtest = abtest;
	}

	public AbTest getAbtest() {
		return abtest;
	}

	public void setAbtest(AbTest abtest) {
		this.abtest = abtest;
	}
}
