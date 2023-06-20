package moontrack.analytics.communication;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Session;
import org.hibernate.query.Query;

import analytics.model.user.AnalyticsUser;
import analytics.model.user.DistributionUserRegistration;
import core.action.exceptions.InvalidActionException;
import core.cluster.core.MessageProcessor;
import core.geoip.Geoip;
import core.userIdentification.UserServicesContainer;
import moontrack.analytics.LoginInfoUtil;

public class DistributionRegistrationProcessor
		extends MessageProcessor<DistributionRegistrationMessageV2> {

	public DistributionRegistrationProcessor(DistributionRegistrationMessageV2 message) {
		super(message);
	}

	@Override
	protected void perform() throws InvalidActionException {
		DistributionUserRegistration dUser = new DistributionUserRegistration(message.gameId,
				message.distributionUserId, currentDate, message.userServiceId,
				LoginInfoUtil.convert(message.loginInfo));

		session.save(dUser);
		AnalyticsUser aUser = findAnalyticsUser(session, message.getContainer(), message.ipv4,
				true);

		transaction = session.beginTransaction();
		dUser.setAnalyticsUser(aUser);
		transaction.commit();
	}

	public static AnalyticsUser findAnalyticsUser(Session session,
			UserServicesContainer psContainer, int ipv4, boolean createIfNotFound) {
		AnalyticsUser aUser = null;
		if (psContainer != null) {
			Map<Byte, String> psids = psContainer.getPlatformServices();
			if (!psids.isEmpty()) {
				Query<?> query = createAUserSearchQuery(session, psids);
				Integer aUserId = (Integer) query.uniqueResult();
				if (aUserId != null) {
					aUser = session.get(AnalyticsUser.class, aUserId);
				}
			}
		}

		if (aUser == null && createIfNotFound) {
			aUser = new AnalyticsUser();
			aUser.getPsContainer().copyFrom(psContainer);

			String country = Geoip.getInstance().geoip(ipv4);
			aUser.setCountry(country);
			session.save(aUser);
		}
		return aUser;
	}

	private static Query<?> createAUserSearchQuery(Session session, Map<Byte, String> psids) {
		String queryText = "SELECT aUser.id FROM AnalyticsUser AS aUser WHERE ";
		for (Iterator<Entry<Byte, String>> itr = psids.entrySet().iterator(); itr.hasNext();) {
			Entry<Byte, String> entry = itr.next();
			String key = entry.getKey().toString();
			queryText += "aUser.psContainer.userServices[" + key + "]=:arg" + key;
			if (itr.hasNext()) {
				queryText += " OR ";
			}
		}

		Query<?> query = session.createQuery(queryText);
		for (Entry<Byte, String> entry : psids.entrySet()) {
			String argName = "arg" + entry.getKey().toString();
			query.setParameter(argName, entry.getValue());
		}
		return query;
	}

	@Override
	protected boolean shouldLog() {
		return true;
	}
}
