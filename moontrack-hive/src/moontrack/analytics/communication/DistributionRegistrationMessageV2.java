package moontrack.analytics.communication;

import java.io.Serializable;

import core.cluster.core.CommunicationMessage;
import core.cluster.core.MessageProcessor;
import core.tomcat.ClientLoginInfo;
import core.userIdentification.UserServicesContainer;

public class DistributionRegistrationMessageV2 extends CommunicationMessage implements Serializable {
	private static final long serialVersionUID = -208322399162686322L;

	public final int distributionUserId;
	public final short gameId;
	public final int ipv4;
	public final UserServicesContainer container;
	public final byte userServiceId;
	public final ClientLoginInfo loginInfo;

	public DistributionRegistrationMessageV2(int dUserId, UserServicesContainer container,
			Integer ipv4, byte platformId, short gameId, ClientLoginInfo clientLoginInfo) {
		distributionUserId = dUserId;
		this.ipv4 = ipv4;
		this.userServiceId = platformId;
		this.gameId = gameId;
		this.container = container;
		this.loginInfo = clientLoginInfo;
	}

	@Override
	public MessageProcessor<?> getMessageProcessor() {
		return new DistributionRegistrationProcessor(this);
	}
	
	public UserServicesContainer getContainer() {
		return container;
	}
	
	@Override
	public String toString() {
		return "DistributionRegistrationMessageV2 [distributionUserId=" + distributionUserId
				+ ", gameId=" + gameId + ", usId=" + userServiceId + ", ipv4=" + ipv4 + ", loginInfo=" + writeLoginInfo() + "]";
	}
	
	private String writeLoginInfo() {
		return loginInfo != null ? loginInfo.writeInfo() : "null";
	}
}