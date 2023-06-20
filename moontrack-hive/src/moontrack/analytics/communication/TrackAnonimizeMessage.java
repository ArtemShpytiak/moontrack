package moontrack.analytics.communication;

import java.util.Date;
import java.util.List;

import org.hibernate.query.Query;

import analytics.model.UserLogin;
import analytics.model.user.ARealmUser;
import core.cluster.core.MessageProcessor;
import core.userIdentification.UserServicesContainer;
import moontrack.analytics.AnalyticsCluster;

public class TrackAnonimizeMessage extends AnalyticsMessage {

	private static final long serialVersionUID = 6339929511153955242L;

	public TrackAnonimizeMessage(int realmUserId, short realmId, short gameId, Date date) {
		super(realmUserId, realmId, gameId, date);
	}

	public TrackAnonimizeMessage(int realmUserId, Date date, AnalyticsCluster cluster) {
		this(realmUserId, cluster.getRealmId(), cluster.getGameId(), date);
	}

	@Override
	protected String subClassFields() {
		return "";
	}
	
	@Override
	public MessageProcessor<?> getMessageProcessor() {
		return new AnalyticsMessageProccessor<TrackAnonimizeMessage>(this) {
			@Override
			protected void perform() {
				ARealmUser rUser = getRealmUser();                
				Query<ARealmUser> getAllRealmUsersByDUser = session
						.createQuery(
								"FROM " + ARealmUser.class.getName()
										+ " AS rUser WHERE rUser.dUser.id = :dUserId",
								ARealmUser.class)
						.setParameter("dUserId", rUser.getdUser().getId());
				List<ARealmUser> realmUsers = getAllRealmUsersByDUser.list();
				
				for (ARealmUser someRUser : realmUsers) {
					int resolvedUserId = someRUser.getId();  
					transaction = session.beginTransaction();
					Query<?> query = session.createQuery("UPDATE " + UserLogin.class.getName() + " SET ipv4 = :newIP WHERE realmUser.id = :userId")
							.setParameter("userId", resolvedUserId)
							.setParameter("newIP", 0);
					query.executeUpdate();
					transaction.commit();
				}
				
				UserServicesContainer usContainer = rUser.getdUser().getAnalyticsUser().getPsContainer();
				transaction = session.beginTransaction();
				usContainer.clearUserServices();
				usContainer.clearDeviceIds();
				transaction.commit();
			}
		};
	}
}
