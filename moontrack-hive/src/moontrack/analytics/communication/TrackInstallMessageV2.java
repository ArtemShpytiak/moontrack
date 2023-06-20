package moontrack.analytics.communication;

import java.io.Serializable;
import java.util.Date;

import analytics.model.AppInstall;
import core.cluster.core.CommunicationMessage;
import core.cluster.core.MessageProcessor;
import core.tomcat.ClientLoginInfo;
import moontrack.analytics.AnalyticsCluster;
import moontrack.analytics.AppInstallsManager;
import moontrack.analytics.LoginInfoUtil;
import moontrack.events.InstallEvent;

public class TrackInstallMessageV2 extends CommunicationMessage implements Serializable {
	private static final long serialVersionUID = 3887187143527230532L;
	
	private String deviceId;
	private short gameId;
	private ClientLoginInfo loginInfo;
	
	public TrackInstallMessageV2(String deviceId, ClientLoginInfo loginInfo, short gameId) {
		super();
		this.deviceId = deviceId;
		this.loginInfo = loginInfo;
		this.gameId = gameId;
	}

	public TrackInstallMessageV2(InstallEvent cEvent, AnalyticsCluster analyticsCluster) {
		this(cEvent.deviceId, cEvent.info, analyticsCluster.getGameId());
	}

	@Override
	public MessageProcessor<?> getMessageProcessor() {
		return new MessageProcessor<TrackInstallMessageV2>(this) {
			@Override
			protected boolean shouldLog() {
				return true;
			}
		
			@Override
			protected void perform() {

				if (!AppInstallsManager.isAppInstallListed(message.deviceId, gameId))
				{
					transaction = session.beginTransaction();
					
					AppInstall install = new AppInstall();
					install.setDeviceId(message.deviceId);
					
					ClientLoginInfo loginInfo = message.loginInfo;
					install.setLoginInfo(LoginInfoUtil.convert(loginInfo));
					
					install.setDate(new Date());
					install.setGameId(gameId);
					
					session.save(install);
					
					transaction.commit();
					
					AppInstallsManager.appendDeviceId(deviceId, gameId);
				}
				
			}
		};
	}

	@Override
	public String toString() {
		return "TrackInstallMessage [deviceId=" + deviceId + ", osInfo=" + loginInfo + ", gameId="
				+ gameId + "]";
	}
	
}
