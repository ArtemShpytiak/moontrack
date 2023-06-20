package moontrack.analytics.communication.abtest;

import core.action.exceptions.InvalidActionException;
import core.cluster.core.MessageProcessor;
import hive.model.abtest.AbTest;

public class AbtestArchivedProcessor extends MessageProcessor<AbtestArchivedMessage> {

	public AbtestArchivedProcessor(AbtestArchivedMessage message) {
		super(message);
	}

	@Override
	protected void perform() throws InvalidActionException {
		transaction = session.beginTransaction();

		AbTest abtest = session.get(AbTest.class, message.getId());
		abtest.setArchived(true);
		session.update(abtest);

		transaction.commit();
	}

}
