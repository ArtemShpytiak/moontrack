package moontrack.analytics.communication.abtest;

import com.moonmana.log.Log;

import core.action.exceptions.InvalidActionException;
import core.cluster.core.MessageProcessor;

public class AbtestEditedProcessor extends MessageProcessor<AbtestEditedMessage> {

	public AbtestEditedProcessor(AbtestEditedMessage message) {
		super(message);
	}

	@Override
	protected void perform() throws InvalidActionException {
		Log.out("AbtestEditedMessage received");

//		Abtest test = session.load(Abtest.class, message.getAbtest().getId());
//		test.setName(message.getAbtest().getName());
//		test.setStartDate(message.getAbtest().getStartDate());
//		test.setFinishDate(message.getAbtest().getFinishDate());
//
//		transaction = session.beginTransaction();
//		session.merge(test);
//		transaction.commit();
	}

}
