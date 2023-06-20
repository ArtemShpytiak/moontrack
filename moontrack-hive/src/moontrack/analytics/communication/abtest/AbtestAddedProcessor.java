package moontrack.analytics.communication.abtest;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import com.moonmana.log.Log;

import analytics.model.IAPPurchase;
import analytics.model.user.ARealmUser;
import core.action.exceptions.InvalidActionException;
import core.cluster.core.MessageProcessor;
import hive.model.abtest.AbTest;
import hive.model.abtest.AbTestGroup;
import hive.model.abtest.AbTestGroupByFilter;
import hive.model.abtest.AbTestGroupByPercentage;
import hive.model.abtest.AbTestGroupBySegment;
import hive.model.abtest.AbTestUser;
import hive.model.filter.FilterHub;

public class AbtestAddedProcessor extends MessageProcessor<AbtestAddedMessage> {

	public AbtestAddedProcessor(AbtestAddedMessage message) {
		super(message);
	}

	@Override
	protected void perform() throws InvalidActionException {
		Log.out("AbtestAdded message received");
		AbTest abtest = message.getAbtest();
		Log.out(abtest.toString());

		transaction = session.beginTransaction();
		session.merge(abtest);
		transaction.commit();

		List<AbTestGroupByPercentage> percentages = new ArrayList<>();

		transaction = session.beginTransaction();
		abtest.getGroups().forEach(group -> {
			if (group instanceof AbTestGroupByPercentage) {
				percentages.add((AbTestGroupByPercentage) group);
			} else if (group instanceof AbTestGroupByFilter) {
				createGroupsByFilters(group, ((AbTestGroupByFilter) group).getFilter());
			} else if (group instanceof AbTestGroupBySegment) {
				createGroupsBySegment((AbTestGroupBySegment) group);
			} else {
				Log.outError("Cannot create group: unsupported type.");
			}
		});

		if (!percentages.isEmpty()) {
			createGroupsByPercentage(percentages, abtest);
		}
		transaction.commit();
	}

	private void createGroupsBySegment(AbTestGroupBySegment group) {
		createGroupsByFilters(group, group.getSegment().getFilter());
	}

	private void createGroupsByFilters(AbTestGroup group, FilterHub filter) {
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
		Root<ARealmUser> root = query.from(ARealmUser.class);
		Join<Object, Object> dur = root.join("dUser");
		Join<Object, Object> auserJoin = dur.join("analyticsUser");
		Join<Object, Object> loginJoin = dur.join("loginInfo");

		dur.on(cb.equal(dur.get("game"), group.getAbtest().getGameId()));

		List<Predicate> conditions = new ArrayList<>();
		List<Predicate> orConditions = new ArrayList<>();

		filter.getOses().forEach(os -> orConditions.add(cb.equal(loginJoin.get("osId"), os.getCode())));
		if (!orConditions.isEmpty()) {
			conditions.add(cb.or(orConditions.toArray(new Predicate[orConditions.size()])));
		}
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

		CriteriaQuery<Integer> criteria = query.select(root.get("id").as(Integer.class));

		if (!conditions.isEmpty()) {
			criteria.where(cb.and(conditions.toArray(new Predicate[conditions.size()])));
		}
		List<Integer> selected = session.createQuery(criteria).getResultList();

		for (Integer s : selected) {
			AbTestUser abUser = new AbTestUser();
			abUser.setGroup(group);
			abUser.setRealmUser(session.getReference(ARealmUser.class, s));
			session.save(abUser);
		}

		Log.out("Amount of ab users: " + selected.size());
	}

	private void createGroupsByPercentage(List<AbTestGroupByPercentage> groups, AbTest abTest) {
		int total = getTotalAmountOfUsers(abTest);
		List<Integer> allGroupUsers = new ArrayList<>();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
		Root<ARealmUser> root = query.from(ARealmUser.class);
		Join<Object, Object> duserJoin = root.join("dUser");
		duserJoin.on(cb.equal(duserJoin.get("game"), abTest.getGameId()));

		for (AbTestGroupByPercentage group : groups) {
			int groupSize = group.calculateGroupSize(total);
			CriteriaQuery<Integer> criteria = query.select(root.get("id").as(Integer.class));

			if (!allGroupUsers.isEmpty()) {
				criteria.where(root.get("id").in(allGroupUsers).not());
			}

			List<Integer> selected = session.createQuery(criteria).setMaxResults(groupSize).getResultList();
			allGroupUsers.addAll(selected);

			group.setGroupSize(selected.size());
			session.merge(group);

			for (Integer s : selected) {
				AbTestUser abUser = new AbTestUser();
				abUser.setGroup(group);
				abUser.setRealmUser(session.getReference(ARealmUser.class, s));
				session.save(abUser);
			}
		}
	}

	private int getTotalAmountOfUsers(AbTest test) {
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
		Root<ARealmUser> root = query.from(ARealmUser.class);
		Join<Object, Object> duserJoin = root.join("dUser");
		duserJoin.on(cb.equal(duserJoin.get("game"), test.getGameId()));
		return session.createQuery(query.select(cb.count(root))).getSingleResult().intValue();
	}

}
