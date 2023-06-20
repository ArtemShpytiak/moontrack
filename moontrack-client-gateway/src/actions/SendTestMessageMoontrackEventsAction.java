package actions;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import core.action.exceptions.InvalidActionException;
import core.admin.action.HTMLAdminAction;
import moontrack.analytics.communication.MoontrackEventsCluster;
import moontrack.analytics.communication.TestMessage;

public class SendTestMessageMoontrackEventsAction extends HTMLAdminAction {

	@Override
	public void perform() throws InvalidActionException {
		String messageText = getString("m");
		if (messageText == null) {
			messageText = "Vita mortis careo!";
		}
		Serializable testMessage = new TestMessage(messageText);
		startHTML();
		try {
			MoontrackEventsCluster.instance.track(testMessage);
			writeln("Test message sent.");
		} catch (Throwable e) {
			writeln(e.toString() + " : " + e.getMessage());
			for (StackTraceElement el : e.getStackTrace()) {
				writeln("-" + el.toString());
			}
			throw e;
		}
	}

	@Override
	protected void openHibernateSession() throws InvalidActionException {
		//do nothing		
	}

	@Override
	protected void checkAdminIp(HttpServletRequest httpRequest) {
		//do nothing
	}
}

	



	

