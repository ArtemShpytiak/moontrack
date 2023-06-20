package moontrack.analytics;

import analytics.model.AClientLoginInfo;
import core.tomcat.ClientLoginInfo;

public class LoginInfoUtil {

	public static AClientLoginInfo convert(ClientLoginInfo loginInfo) {
		if (loginInfo == null) {
			return AClientLoginInfo.ZERO;
		}
		return new AClientLoginInfo(loginInfo.getPlatformID(), 
				loginInfo.getOsId(), 
				loginInfo.osVersion, 
				loginInfo.deviceDescription);
	}
}
