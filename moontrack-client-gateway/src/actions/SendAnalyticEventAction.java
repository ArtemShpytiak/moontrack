package actions;

import com.moonmana.log.Log;

import core.action.Action;
import core.action.IRawByteArrayAction;
import core.action.exceptions.InvalidActionException;
import moontrack.analytics.communication.MoontrackEventsCluster;
import moontrack.analytics.communication.PackedEventsMessage;

public class SendAnalyticEventAction extends Action implements IRawByteArrayAction {

	@Override
	public void perform() throws InvalidActionException {
		byte[] eventsData = getRawData(false);
		
		if (MoontrackEventsCluster.instance == null) {
			Log.outError(" MoontrackEventsCluster.instance == null ");
			return;
		}
		PackedEventsMessage message = new PackedEventsMessage(eventsData);
		MoontrackEventsCluster.instance.track(message);
	}
}
