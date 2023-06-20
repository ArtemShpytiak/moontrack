package moontrack.analytics.communication;

import java.io.Serializable;
import java.util.Date;

import com.moonmana.OS;
import com.moonmana.PlatformID;

import analytics.model.IAPPurchase;
import analytics.model.UserLogin;
import analytics.model.user.ARealmUser;
import core.cluster.core.MessageProcessor;

public class TrackIAPMessageV2 extends AnalyticsMessage implements Serializable {
	private static final long serialVersionUID = -983485649799543040L;
	public final int type;
	public final int status;
	public final boolean isSandBox;
	public final float price;
	public final byte paymentProvider;

	public TrackIAPMessageV2(int userId, int type, float price, int status, 
			boolean isSandBox, byte paymentProvider, Date date,
			short realmId, short gameId) {
		super(userId, realmId, gameId, date);
		this.type = type;
		this.price = price;
		this.status = status;
		this.isSandBox = isSandBox;
		this.paymentProvider = paymentProvider;
	}
	
	@Override
	public MessageProcessor<?> getMessageProcessor() {
		return new AnalyticsMessageProccessor<TrackIAPMessageV2>(this) {

			@Override
			protected void perform() {
				ARealmUser realmUser = getRealmUser();
				if (realmUser != null) {
					UserLogin lastLogin = realmUser.getLastLogin();
					byte osId = OS.NONE;
					byte platformID = PlatformID.NONE;
					if (lastLogin != null && lastLogin.getLoginInfo() != null) {
						Byte osIdStored = lastLogin.getLoginInfo().osId;
						if (osIdStored != null) {
							osId = osIdStored; 
						}
						Byte platformIDStored = lastLogin.getLoginInfo().platformID;
						if (platformIDStored != null) {
							platformID = platformIDStored; 
						}
					}
							
					IAPPurchase purchase = new IAPPurchase(realmUser, currentDate, message.type,message.price,
								message.status, message.isSandBox, 
								paymentProvider, 
								osId,
								platformID
							);
	
					transaction = session.beginTransaction();
					session.save(purchase);
					transaction.commit();
				}
			}
			
			@Override
			protected ARealmUser performPartRegistration() {
				TrackRegistrationProcessor registrationProcessor = new TrackRegistrationProcessor(message);
				registrationProcessor.performStatic(session, currentDate, sender);
				return registrationProcessor.getRegisteredUser();
			}
		};
	}

	@Override
	protected String subClassFields() {
		return "type=" + type + ", status=" + status + ", isSandBox=" + isSandBox
				+ ", price=" + price + ", platform=" + paymentProvider;
	}
}
