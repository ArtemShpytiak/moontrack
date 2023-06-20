package moontrack.events;

import com.moonmana.events.IEvent;

import core.tomcat.ClientLoginInfo;

public class InstallEvent implements IEvent {

	public final String deviceId;
	public final ClientLoginInfo info;

	public InstallEvent(String deviceId, ClientLoginInfo info) {
		this.deviceId = deviceId;
		this.info = info;
	}
}
