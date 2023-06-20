package moontrack.analytics.communication.segment;

import com.moonmana.log.Log;

import core.action.exceptions.InvalidActionException;
import core.cluster.core.MessageProcessor;
import hive.model.segment.Segment;

public class SegmentEditedProcessor extends MessageProcessor<SegmentEditedMessage> {

	public SegmentEditedProcessor(SegmentEditedMessage message) {
		super(message);
	}

	@Override
	protected void perform() throws InvalidActionException {
		Log.out("SegmentEdited message received");
		Log.out(message.getSegment().toString());

		Segment segment = session.load(Segment.class, message.getSegment().getId());
		segment.getFilter().getPlatforms().clear();
		segment.getFilter().getCountries().clear();
		segment.getFilter().getRealms().clear();
		segment.getFilter().getOses().clear();
		segment.getFilter().getDevices().clear();

		transaction = session.beginTransaction();
		session.merge(message.getSegment().getFilter());
		session.merge(message.getSegment());
		transaction.commit();
	}
}
