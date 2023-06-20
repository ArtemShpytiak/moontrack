package actions;

import core.action.ActionFactory;

public class MoontrackCGActionFactory extends ActionFactory {

	@Override
	protected void initActions() {
		put(MoontrackCGActions.USER_DATA, SendAnalyticEventAction.class);
		put(MoontrackCGActions.TEST_ACTION, SendTestMessageMoontrackEventsAction.class);

	}

}
