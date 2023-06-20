package moontrack.actions;

import com.moonmana.app.App;
import com.moonmana.net.ResponseCode;

import core.action.Action;
import core.action.exceptions.InvalidActionException;
import core.tomcat.ClientLoginInfo;
import moontrack.events.InstallEvent;

public class TrackInstallAction extends Action {

	@Override
	public void perform() throws InvalidActionException {
		
		String deviceId = getString(AnalyticsRequestParameters.DEVICE_ID);
		
		ClientLoginInfo clientInfo = ClientLoginInfo.generateClientLoginInfo(this);
		InstallEvent installEvent = new InstallEvent(deviceId, clientInfo);
		App.instance().eventProcessor().proccess(installEvent);
		
		pack.push(ResponseCode.OK);
	}
}
