package moontrack.analytics.communication.segment;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.hibernate.query.Query;

import com.moonmana.log.Log;

import analytics.model.IAPPurchase;
import analytics.model.user.ARealmUser;
import core.action.exceptions.InvalidActionException;
import core.cluster.core.MessageProcessor;
import hive.model.filter.FilterHub;
import hive.model.segment.Segment;
import hive.model.segment.SegmentUser;

public class SegmentAddedProcessor extends MessageProcessor<SegmentAddedMessage> {

	public SegmentAddedProcessor(SegmentAddedMessage message) {
		super(message);
	}

	@Override
	protected void perform() throws InvalidActionException {
		Log.out("SegmentAdded message received");
		Log.out(message.getSegment().toString());

		transaction = session.beginTransaction();
		session.merge(message.getSegment().getFilter());
		session.merge(message.getSegment());

		createGroupByFilter(message.getSegment(), message.getSegment().getFilter());

		transaction.commit();
	}

	private void createGroupByFilter(Segment segment, FilterHub filter) {
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
		Root<ARealmUser> root = query.from(ARealmUser.class);
		Join<Object, Object> dur = root.join("dUser");
		Join<Object, Object> auserJoin = dur.join("analyticsUser");
		Join<Object, Object> loginJoin = dur.join("loginInfo");

		dur.on(cb.equal(dur.get("game"), filter.getGameId()));

		List<Predicate> conditions = new ArrayList<>();
		List<Predicate> orConditions = new ArrayList<>();

		filter.getOses().forEach(os -> orConditions.add(cb.equal(loginJoin.get("osId"), os.getCode())));
		if (!orConditions.isEmpty()) {
			conditions.add(cb.or(orConditions.toArray(new Predicate[orConditions.size()])));
		}
		orConditions.clear();
		filter.getPlatforms()
				.forEach(platform -> orConditions.add(cb.equal(loginJoin.get("platformID"), platform.getCode())));
		if (!orConditions.isEmpty()) {
			conditions.add(cb.or(orConditions.toArray(new Predicate[orConditions.size()])));
		}
		orConditions.clear();
		filter.getCountries()
				.forEach(country -> orConditions.add(cb.equal(auserJoin.get("country"), country.getCode())));
		if (!orConditions.isEmpty()) {
			conditions.add(cb.or(orConditions.toArray(new Predicate[orConditions.size()])));
		}
		orConditions.clear();
		filter.getRealms().forEach(realm -> orConditions.add(cb.equal(root.get("realmId"), realm.getRealm())));
		if (!orConditions.isEmpty()) {
			conditions.add(cb.or(orConditions.toArray(new Predicate[orConditions.size()])));
		}
		orConditions.clear();
		// abtestGroup.getGroupByFilter().getDevices().forEach(device ->
		// orConditions.add((builder.equal()));
		if (filter.getRegistrationDateFrom() != null) {
			conditions.add(cb.greaterThanOrEqualTo(dur.get("registrationDate"), filter.getRegistrationDateFrom()));
		}
		if (filter.getRegistrationDateTo() != null) {
			conditions.add(cb.lessThanOrEqualTo(dur.get("registrationDate"), filter.getRegistrationDateTo()));
		}

		if (filter.isPaying() != null) {
			boolean paying = filter.isPaying().booleanValue();

			Root<ARealmUser> realmUser = query.from(ARealmUser.class);
			Root<IAPPurchase> iaps = query.from(IAPPurchase.class);

			conditions.add(cb.equal(dur.get("id"), realmUser.get("dUser")));
			conditions.add(cb.equal(realmUser.get("id"), iaps.get("realmUser")));

			Subquery<Integer> iapSubquery = query.subquery(Integer.class);
			Root<IAPPurchase> iap = iapSubquery.from(IAPPurchase.class);
			iapSubquery.groupBy(iap.get("realmUser"));
			iapSubquery.select(iap.get("realmUser")).where(cb.isFalse(iap.get("isSandBox")));

			boolean ranged = filter.getSpentMoneyFrom() != null || filter.getSpentMoneyTo() != null;
			if (ranged) {
				List<Predicate> range = new ArrayList<>();
				if (filter.getSpentMoneyFrom() != null) {
					range.add(cb.greaterThanOrEqualTo(cb.sum(iap.get("price")), filter.getSpentMoneyFrom()));
				}
				if (filter.getSpentMoneyTo() != null) {
					range.add(cb.lessThanOrEqualTo(cb.sum(iap.get("price")), filter.getSpentMoneyTo()));
				}
				iapSubquery.having(cb.and(range.toArray(new Predicate[range.size()])));
			}
			if (paying) {
				conditions.add(iaps.get("realmUser").in(iapSubquery));
			} else {
				conditions.add(iaps.get("realmUser").in(iapSubquery).not());
			}
		}

		query.select(root.get("id").as(Integer.class));

		if (!conditions.isEmpty()) {
			query.where(cb.and(conditions.toArray(new Predicate[conditions.size()])));
		}
		Query<Integer> createQuery = session.createQuery(query);
		List<Integer> selected = createQuery.getResultList();
		Log.out(createQuery.unwrap(org.hibernate.query.Query.class).getQueryString());

		for (Integer s : selected) {
			SegmentUser user = new SegmentUser();
			user.setSegment(segment);
			user.setRealmUser(session.getReference(ARealmUser.class, s));
			session.save(user);
		}

		Log.out("Amount of segment users: " + selected.size());
	}

}
