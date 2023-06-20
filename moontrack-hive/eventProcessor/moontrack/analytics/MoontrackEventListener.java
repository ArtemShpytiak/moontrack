package moontrack.analytics;

import java.io.Serializable;
import java.util.Date;

import com.moonmana.events.IEvent;
import com.moonmana.events.IEventListener;
import com.moonmana.log.Log;

import core.cluster.channels.ChannelInitializedEvent;
import core.common.gameAccount.LoginEvent;
import core.distribution.RealmStartEvent;
import core.distribution.actions.admin.AnonimizeEvent;
import core.distribution.communication.DistributionRegistrationInfo;
import core.distribution.realm.registration.RegistrationEvent;
import core.events.StatisticEvent;
import core.gameServer.gameAccount.playerSession.SessionCloseEvent;
import core.monetization.IAPEvent;
import core.tomcat.ClientLoginInfo;
import core.tomcat.ServerUndeployServicesDestroyedEvent;
import moontrack.analytics.communication.DistributionRegistrationMessageV2;
import moontrack.analytics.communication.EventMessage;
import moontrack.analytics.communication.SessionCloseMessage;
import moontrack.analytics.communication.TrackAnonimizeMessage;
import moontrack.analytics.communication.TrackIAPMessageV2;
import moontrack.analytics.communication.TrackInstallMessageV2;
import moontrack.analytics.communication.TrackLoginMessageV2;
import moontrack.analytics.communication.TrackRegistrationMessageV2;
import moontrack.analytics.communication.abtest.AbtestAddedMessage;
import moontrack.analytics.communication.abtest.AbtestArchivedMessage;
import moontrack.analytics.communication.abtest.AbtestEditedMessage;
import moontrack.analytics.communication.segment.SegmentAddedMessage;
import moontrack.analytics.communication.segment.SegmentArchivedMessage;
import moontrack.analytics.communication.segment.SegmentEditedMessage;
import moontrack.events.InstallEvent;
import moontrack.events.abtest.AbtestAddedEvent;
import moontrack.events.abtest.AbtestArchivedEvent;
import moontrack.events.abtest.AbtestEditedEvent;
import moontrack.events.segment.SegmentAddedEvent;
import moontrack.events.segment.SegmentArchivedEvent;
import moontrack.events.segment.SegmentEditedEvent;

public class MoontrackEventListener implements IEventListener<IEvent> {
	
	private final AnalyticsCluster analyticsCluster;

	public MoontrackEventListener(AnalyticsCluster analyticsCluster) {
		this.analyticsCluster = analyticsCluster;
	}
	
	private static DistributionRegistrationMessageV2 getDRegistration(RegistrationEvent rEvent, short gameId) {
	DistributionRegistrationInfo dRInfo = rEvent.getDistributionRegistrationInfo();
	return (dRInfo != null) ?
			new DistributionRegistrationMessageV2(dRInfo.dUserId,
					dRInfo.container,
			dRInfo.ipv4,
			dRInfo.userServiceId,
			gameId,
			dRInfo.clientLoginInfo)
		: null;
	}

	@Override
	public boolean process(IEvent event) {
		Log.outDebug("got event" + event);
		if (event instanceof RegistrationEvent) {
			RegistrationEvent cEvent = (RegistrationEvent) event;
			track(new TrackRegistrationMessageV2(
					cEvent.getUser().getId(), 
					analyticsCluster.getRealmId(), 
					analyticsCluster.getGameId(),
					cEvent.getUser().getDistributionUserId(), 
					getDRegistration(cEvent, analyticsCluster.getGameId()),
					cEvent.getUser().getJoiningDate()));
			return true;
		}
		if (event instanceof InstallEvent) {
			InstallEvent cEvent = (InstallEvent) event;
			track(new TrackInstallMessageV2(cEvent, analyticsCluster));
			return true;
		}
		if (event instanceof LoginEvent) {
			LoginEvent cEvent = (LoginEvent) event;
			track(getTrackLoginMessage(cEvent, analyticsCluster));
			return true;
		}
		if (event instanceof RealmStartEvent) {
			RealmStartEvent cEvent = (RealmStartEvent) event;
			analyticsCluster.setRealmId(cEvent.getRealmId());
			return true;
		}
		if (event instanceof SessionCloseEvent) {
			SessionCloseEvent cEvent = (SessionCloseEvent) event;
			track(new SessionCloseMessage(cEvent.playerId, analyticsCluster,
					cEvent.playerSessionCloseResult.lastUpdateActionDate,
					cEvent.playerSessionCloseResult.numUpdates));
			return true;
		}
		if (event instanceof StatisticEvent) {
			StatisticEvent cEvent = (StatisticEvent) event;
			track(new EventMessage(cEvent.getUserId(), 
					cEvent.getStatType(), 
					cEvent.getResourcesRemoved().getPremiumCurrency(),
					cEvent.getDate(), 
					analyticsCluster));
			return true;
		}
		if (event instanceof IAPEvent) {
			IAPEvent cEvent = (IAPEvent) event;
			track(new TrackIAPMessageV2(cEvent.playerId,
					cEvent.type,
					cEvent.price,
					cEvent.purchaseInfo.getRespondStatus(), 
					cEvent.purchaseInfo.isSandBox(),
					cEvent.purchaseInfo.getPaymentProvider(),
					cEvent.date,
					analyticsCluster.getRealmId(),
					analyticsCluster.getGameId()));
			return true;
		}
		if (event instanceof ChannelInitializedEvent) {
			ChannelInitializedEvent cEvent = (ChannelInitializedEvent) event;
			analyticsCluster.init(cEvent.getProvider());
			return true;
		}
		if (event instanceof ServerUndeployServicesDestroyedEvent) {
			analyticsCluster.destroy((ServerUndeployServicesDestroyedEvent) event);
			return true;
		}
		if (event instanceof AbtestAddedEvent) {
			AbtestAddedEvent ev = (AbtestAddedEvent) event;
			track(new AbtestAddedMessage(ev));
			return true;
		}
		if (event instanceof AbtestEditedEvent) {
			AbtestEditedEvent ev = (AbtestEditedEvent) event;
			track(new AbtestEditedMessage(ev));
			return true;
		}
		if (event instanceof AbtestArchivedEvent) {
			AbtestArchivedEvent ev = (AbtestArchivedEvent) event;
			track(new AbtestArchivedMessage(ev));
			return true;
		}
		if (event instanceof SegmentAddedEvent) {
			SegmentAddedEvent ev = (SegmentAddedEvent) event;
			track(new SegmentAddedMessage(ev));
			return true;
		}
		if (event instanceof SegmentEditedEvent) {
			SegmentEditedEvent ev = (SegmentEditedEvent) event;
			track(new SegmentEditedMessage(ev));
			return true;
		}
		if (event instanceof SegmentArchivedEvent) {
			SegmentArchivedEvent ev = (SegmentArchivedEvent) event;
			track(new SegmentArchivedMessage(ev));
			return true;
		}
		if (event instanceof AnonimizeEvent) {
			AnonimizeEvent cEvent = (AnonimizeEvent) event;
			track(new TrackAnonimizeMessage(cEvent.realmUserId, cEvent.date, analyticsCluster));
			return true;
		}
		return false;
	}

	private TrackLoginMessageV2 getTrackLoginMessage(LoginEvent cEvent,
			AnalyticsCluster cluster) {
		return getTrackLoginMessage(cEvent.realmUserId, cEvent.dUserId, cEvent.loginInfo, cEvent.ipv4,
				cEvent.playerSession, cEvent.date, cluster);
	}

	private TrackLoginMessageV2 getTrackLoginMessage(int realmUserId, int dUserId,
			ClientLoginInfo loginInfo, Integer ipv4,
			core.gameServer.gameAccount.playerSession.PlayerSession playerSession, Date date,
			AnalyticsCluster cluster) {
		return new TrackLoginMessageV2(realmUserId, cluster.getRealmId(), cluster.getGameId(), date,
				dUserId, loginInfo, ipv4, playerSession.getLastUpdateActionDate(),
				playerSession.getNumUpdates(), playerSession.getVersion(),
				playerSession.getUserServicesId());
	}

	private void track(Serializable msg) {
		analyticsCluster.track(msg);
	}
}
