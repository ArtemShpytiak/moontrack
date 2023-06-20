package moontrack.analytics.communication;

import java.util.Date;

import core.cluster.core.MessageProcessor;

public class TrackRegistrationMessageV2 extends AnalyticsMessage {
	private static final long serialVersionUID = 8838129368964742944L;
	private int distributionUserId;
	private DistributionRegistrationMessageV2 trackDRegistration;

	public int getDistributionUserId() {
		return distributionUserId;
	}

	public DistributionRegistrationMessageV2 getTrackDRegistration() {
		return trackDRegistration;
	}

	public TrackRegistrationMessageV2(int rUserId, short realmId, short gameId, int dUserId,
			DistributionRegistrationMessageV2 trackDRegistration, Date date) {
		super(rUserId, realmId, gameId, date);
		distributionUserId = dUserId;
		this.trackDRegistration = trackDRegistration;
	}
	
	@Override
	public MessageProcessor<?> getMessageProcessor() {
		return new TrackRegistrationProcessor(this);
	}

	@Override
	protected String subClassFields() {
		return "distributionUserId=" + distributionUserId
				+ ", trackDRegistration=" + trackDRegistration;
	}
}
