package moontrack.analytics.communication;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.HibernateException;
import org.hibernate.query.Query;

import com.moonmana.log.Log;
import com.moonmana.utils.TimeUtils;
import com.moonmana.utils.server.ServerUtils;

import analytics.model.AClientLoginInfo;
import analytics.model.Activity;
import analytics.model.UserLogin;
import analytics.model.user.ARealmUser;
import core.cluster.core.MessageProcessor;
import core.tomcat.ClientLoginInfo;
import moontrack.analytics.LoginInfoUtil;

public class TrackLoginMessageV2 extends AnalyticsMessage {
	private static final long serialVersionUID = 3493139172366639196L;
	private static ConcurrentHashMap<Integer, java.sql.Date> activities = new ConcurrentHashMap<>();
	public final int dUserId;
	public final Integer ipv4;
	public final ClientLoginInfo loginInfo;
	public final Date lastActionDate;
	public final int numUpdates;
	public final int version;
	public final Byte userServicesId;

	public TrackLoginMessageV2(int realmUserId, short realmId, short gameId, Date date, int dUserId,
			ClientLoginInfo loginInfo, Integer ipv4, Date lastUpdateActionDate, int numUpdates,
			int version, Byte userServicesId) {
		super(realmUserId, realmId, gameId, date);
		this.dUserId = dUserId;
		this.loginInfo = loginInfo;
		this.ipv4 = ipv4;
		this.lastActionDate = lastUpdateActionDate;
		this.numUpdates = numUpdates;
		this.version = version;
		this.userServicesId = userServicesId;
	}

	@Override
	protected String subClassFields() {
		return "laDate=" + lastActionDate + ", lInfo=" + writeLoginInfo() + ", ipv4=" + ipv4
				+ ", inet=" + ServerUtils.int2inet(ipv4) + ", ver=" + version + ", nUpd="
				+ numUpdates + ", usId=" + userServicesId + ", duId=" + dUserId;
	}

	private String writeLoginInfo() {
		return loginInfo != null ? loginInfo.writeInfo() : "null";
	}

	@Override
	public MessageProcessor<?> getMessageProcessor() {
		return new AnalyticsMessageProccessor<TrackLoginMessageV2>(this) {
			@Override
			protected void perform() {
				ARealmUser realmUser = getRealmUser();

				UserLogin previousLogin = realmUser.getLastLogin();

				if (lastActionDate != null) {
					trackSessionClosed(lastActionDate, numUpdates, previousLogin);
				}

				AClientLoginInfo aloginInfo = getLoginInfo(previousLogin);

				UserLogin login = new UserLogin(realmUser, currentDate, userServicesId,
						message.ipv4, version, aloginInfo);

				saveActivityIfNeeded(realmUser, login);

				transaction = session.beginTransaction();
				session.save(login);
				realmUser.setLastLogin(login);
				transaction.commit();
			}

			private AClientLoginInfo getLoginInfo(UserLogin previousLogin) {
				AClientLoginInfo aloginInfo = null;
				if (loginInfo != null) {
					aloginInfo = LoginInfoUtil.convert(loginInfo);
				} else if (previousLogin != null) {
					aloginInfo = previousLogin.getLoginInfo();
				} else {
					aloginInfo = AClientLoginInfo.ZERO;
				}
				return aloginInfo;
			}

			private void saveActivityIfNeeded(ARealmUser realmUser, UserLogin login) {
				if (noActivityToday(realmUser)) {
					try {
						transaction = session.beginTransaction();
						Activity activity = new Activity(login);
						trackLastActivity(activity);
						session.save(activity);
						transaction.commit();
					} catch (HibernateException e) {
						Log.outError("Cannot save activity:" + e.getMessage());
						if (transaction != null) {
							transaction.rollback();
						}
					}
				}
			}

			@Override
			protected ARealmUser performPartRegistration() {
				TrackRegistrationProcessor registrationProcessor = new TrackRegistrationProcessor(
						message);
				registrationProcessor.performStatic(session, currentDate, sender);
				return registrationProcessor.getRegisteredUser();
			}

			private void trackLastActivity(Activity activity) {
				activities.put(activity.getdUserRegistrationId(), activity.getDate());
			}

			private boolean noActivityToday(ARealmUser realmUser) {
				int registrationId = realmUser.getdUser().getId();
				java.sql.Date lastDate = activities.get(registrationId);
				java.sql.Date today = new java.sql.Date(currentDate.getTime());
				if (lastDate == null) {
					Query<?> query = session.createQuery(
							"from Activity WHERE dUser.id = :userId AND date = :today");
					Activity lastActivity = (Activity) query.setParameter("userId", registrationId)
							.setParameter("today", today).uniqueResult();
					return (lastActivity == null);
				}
				return !TimeUtils.isSameDay(lastDate, today);
			}
		};
	}
}
