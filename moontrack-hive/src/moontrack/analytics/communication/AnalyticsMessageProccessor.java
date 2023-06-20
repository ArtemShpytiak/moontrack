package moontrack.analytics.communication;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.query.Query;

import com.moonmana.log.Log;

import analytics.model.UserLogin;
import analytics.model.UserSession;
import analytics.model.user.ARealmUser;
import analytics.model.user.DistributionUserRegistration;
import core.cluster.core.MessageProcessor;

public abstract class AnalyticsMessageProccessor<Mess extends AnalyticsMessage>
		extends MessageProcessor<Mess> {

	private static final String GAME = "game";

	private static final String REALM_ID = "realmId";

	private static final String REALM_USER_ID = "realmUserId";

	private static final String GET_REALM_USER = "FROM ARealmUser AS rUser "
			+ "WHERE rUser.realmId = :" + REALM_ID + " AND rUser.realmUserId = :" + REALM_USER_ID
			+ " AND rUser.dUser.game = :" + GAME;

	private static final String GET_D_USER = "FROM DistributionUserRegistration AS dUser "
			+ "WHERE dUser.distributionUserId = :dUserId " + "AND dUser.game = :game";

	private ARealmUser realmUser = null;

	public AnalyticsMessageProccessor(Mess message) {
		super(message);
	}

	@Override
	public void init() {
		super.init();
		if (message.getEventDate() != null) {
			currentDate = message.getEventDate();
		}
	}

	protected final ARealmUser getRealmUser() {
		if (realmUser == null) {
			Log.out("get realmUser rUserId=" + message.getRealmUserId() + " realmId="
					+ message.getRealmId() + " gameId=" + message.getGameId());
			Query<ARealmUser> query = session.createQuery(GET_REALM_USER, ARealmUser.class)
					.setParameter(REALM_USER_ID, message.getRealmUserId())
					.setParameter(REALM_ID, message.getRealmId())
					.setParameter(GAME, message.getGameId());
			realmUser = query.uniqueResult();
		}
		if (realmUser == null) {
			realmUser = performPartRegistration();
		}
		return realmUser;
	}

	protected ARealmUser performPartRegistration() {
		return null;
	}

	protected void trackSessionClosed(Date endDate, int numUpdates) {
		ARealmUser realmUser = getRealmUser();
		if (realmUser != null) {
			UserLogin previousLogin = realmUser.getLastLogin();
			trackSessionClosed(endDate, numUpdates, previousLogin);
		}
	}

	protected void trackSessionClosed(Date endDate, int numUpdates, UserLogin previousLogin) {
		if (previousLogin != null) {
			if (session.get(UserSession.class, previousLogin.getId()) != null) {
				return;
			}
			try {
				UserSession userSession = new UserSession(previousLogin.getId(), endDate,
						numUpdates);
				transaction = session.beginTransaction();
				session.save(userSession);
				transaction.commit();
			} catch (HibernateException e) {
				Log.outError("Cannot save UserSession: " + e.getMessage());
				if (transaction != null) {
					transaction.rollback();
					transaction = null;
				}
			}
		} else {
			Log.outError("Cannot save UserSession: previousLogin not found");
		}
	}

	protected DistributionUserRegistration getDistributionUser(int distributionUserId,
			short gameId) {
		Query<DistributionUserRegistration> query = session
				.createQuery(GET_D_USER, DistributionUserRegistration.class)
				.setParameter("dUserId", distributionUserId).setParameter("game", gameId);
		
		return query.uniqueResult();
	}

	@Override
	protected boolean shouldLog() {
		return true;
	}
}
