package moontrack.analytics.communication;

import java.util.concurrent.locks.Lock;

import com.google.common.util.concurrent.Striped;
import com.moonmana.PlatformID;
import com.moonmana.UserServicesID;

import analytics.model.user.ARealmUser;
import analytics.model.user.AnalyticsUser;
import analytics.model.user.DistributionUserRegistration;
import core.action.exceptions.InvalidActionException;
import moontrack.analytics.LoginInfoUtil;

public class TrackRegistrationProcessor extends AnalyticsMessageProccessor<TrackRegistrationMessageV2> {
	
	private static final int UNKNOWN_DUID = -43951; 

	private final int distributionUserId;
	private final DistributionRegistrationMessageV2 trackDRegistration;
	
	private final boolean completeInfo;
	private ARealmUser registeredUser = null;

	private static final Striped<Lock> registrationLocks = Striped.lock(Runtime.getRuntime().availableProcessors() * 4);

	public TrackRegistrationProcessor(TrackRegistrationMessageV2 message) {
		this(message, true);
	}
	
	public TrackRegistrationProcessor(TrackRegistrationMessageV2 message, boolean completeInfo) {
		super(message);
		distributionUserId = message.getDistributionUserId();
		trackDRegistration = message.getTrackDRegistration();
		this.completeInfo = completeInfo;
	}

	public TrackRegistrationProcessor(TrackLoginMessageV2 message) {
		this(substitution(message), false);
	}

	private static TrackRegistrationMessageV2 substitution(TrackLoginMessageV2 message) {
		return new TrackRegistrationMessageV2(message.getRealmUserId(), 
				message.getRealmId(), 
				message.getGameId(), 
				message.dUserId, 
				new DistributionRegistrationMessageV2(
						message.dUserId, 
						null, 
						message.ipv4, 
						message.userServicesId, 
						message.getGameId(), 
						null), 
				message.getEventDate());
	}
	
	public TrackRegistrationProcessor(TrackIAPMessageV2 message) {
		this(substitution(message), false);
	}

	public TrackRegistrationProcessor(EventMessage message) {
		this(substitution(message), false);
	}

	private static TrackRegistrationMessageV2 substitution(TrackIAPMessageV2 message) {
		return new TrackRegistrationMessageV2(message.getRealmUserId(), 
				message.getRealmId(), 
				message.getGameId(), 
				UNKNOWN_DUID, 
				new DistributionRegistrationMessageV2(
						UNKNOWN_DUID, 
						null, 
						0, 
						PlatformID.NONE, 
						message.getGameId(), 
						null), 
				message.getEventDate());
	}
	
	private static TrackRegistrationMessageV2 substitution(EventMessage message) {
		return new TrackRegistrationMessageV2(message.getRealmUserId(), 
				message.getRealmId(), 
				message.getGameId(), 
				UNKNOWN_DUID, 
				new DistributionRegistrationMessageV2(
						UNKNOWN_DUID, 
						null, 
						0, 
						UserServicesID.NONE, 
						message.getGameId(), 
						null), 
				message.getEventDate());
	}
	
	private static class Triad {
		private final int realmUserId;

		private final short realmId;
		private final short gameId;

		public Triad(TrackRegistrationMessageV2 message) {
			realmUserId = message.getRealmUserId();
			realmId = message.getRealmId();
			gameId = message.getGameId();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + gameId;
			result = prime * result + realmId;
			result = prime * result + realmUserId;
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Triad other = (Triad) obj;
			return (gameId == other.gameId)
				&& (realmId == other.realmId)
				&& (realmUserId == other.realmUserId);
		}
	}

	@Override
	protected void perform() throws InvalidActionException {
		Triad triad = new Triad(message);
		ARealmUser rUser = null;
		Lock lock = registrationLocks.get(triad);
		try {
			lock.lock();
			rUser = getRealmUser();
			
			if (rUser == null) {
				createNewRecord();
			} 
		} finally {
			lock.unlock();
		}
		if (rUser != null && !isStaticPerform() 
				&& !rUser.isCompleteInfo()) { 
			updateExistingRecord(rUser);
		}
	}

	private void updateExistingRecord(ARealmUser rUser) {
		transaction = session.beginTransaction();
		rUser.setStartDate(currentDate);
		rUser.setCompleteInfo(completeInfo);
		transaction.commit();
		updateDUserIfNeeded(rUser);
	}

	private void updateDUserIfNeeded(ARealmUser rUser) {
		DistributionUserRegistration dUser = rUser.getdUser();
		if (dUser != null 
				&& (dUser.getId() == distributionUserId 
				|| dUser.getId() == UNKNOWN_DUID)) {
		
			if (dUser.getRegistrationDate().after(currentDate)) {
				dUser.setRegistrationDate(currentDate);
				
			}
			if (dUser.getUserServicesId() == UserServicesID.NONE) {
				dUser.setUserServicesId(trackDRegistration.userServiceId);
			}
			if (dUser.getLoginInfo().uninited()) {
				dUser.setLoginInfo(LoginInfoUtil.convert(trackDRegistration.loginInfo));
			}
		} else if (shouldCreateDUser(dUser)) {
			dUser = getOrCreateDUser();
			rUser.setdUser(dUser);
			return;
		}
		updateAUserIfNeeded(dUser);
	}

	private void updateAUserIfNeeded(DistributionUserRegistration dUser) {
		if (completeInfo) {
			AnalyticsUser aUser = dUser.getAnalyticsUser();
			if (aUser == null) {
				
			}
		}
	}

	private boolean shouldCreateDUser(DistributionUserRegistration dUser) {
		return (dUser == null || (dUser.getId() != distributionUserId && completeInfo));
	}

	private void createNewRecord() {
		DistributionUserRegistration dUser = getOrCreateDUser();
		ARealmUser rUser = new ARealmUser(message.getRealmUserId(), dUser, message.getRealmId(),
				currentDate);
		rUser.setCompleteInfo(completeInfo);
		transaction = session.beginTransaction();
		session.save(rUser);
		transaction.commit();
		registeredUser = rUser;
	}

	private DistributionUserRegistration getOrCreateDUser() {
		DistributionUserRegistration dUser = getDistributionUser(distributionUserId, message.getGameId());
		if (dUser == null 
				&& trackDRegistration != null) {
			trackDRegistration.getMessageProcessor().performStatic(session, currentDate, sender);
		} 
		dUser = getDistributionUser(distributionUserId, message.getGameId());
		return dUser;
	}

	public ARealmUser getRegisteredUser() {
		return registeredUser;
	}

}
