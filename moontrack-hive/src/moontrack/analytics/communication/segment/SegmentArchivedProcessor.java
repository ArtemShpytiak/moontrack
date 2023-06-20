package moontrack.analytics.communication.segment;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;

import com.moonmana.log.Log;

import core.action.exceptions.InvalidActionException;
import core.cluster.core.MessageProcessor;
import hive.model.segment.Segment;
import hive.model.segment.SegmentUser;

public class SegmentArchivedProcessor extends MessageProcessor<SegmentArchivedMessage> {

	public SegmentArchivedProcessor(SegmentArchivedMessage message) {
		super(message);
	}

	@Override
	protected void perform() throws InvalidActionException {
		// Be careful: persistence context is not synchronized with the result and there
		// is no optimistic locking of the involved entities.
		transaction = session.beginTransaction();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaDelete<SegmentUser> delete = cb.createCriteriaDelete(SegmentUser.class);
		Root<SegmentUser> root = delete.from(SegmentUser.class);
		delete.where(cb.equal(root.get("segment"), message.getId()));
		int deleted = session.createQuery(delete).executeUpdate();
		Segment segment = session.get(Segment.class, message.getId());
		segment.setArchived(true);
		session.merge(segment);
		transaction.commit();

		Log.out("Deleted Segment Users: " + deleted);
	}

}
