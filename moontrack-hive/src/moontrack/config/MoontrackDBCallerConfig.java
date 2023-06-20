package moontrack.config;

import core.hibernate.DBCallerConfig;
import moontrack.analytics.communication.AnalyticsMessage;
import moontrack.analytics.communication.DistributionRegistrationMessageV2;
import moontrack.analytics.communication.EventMessage;
import moontrack.analytics.communication.PackedEventsMessage;
import moontrack.analytics.communication.TestMessage;
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

public class MoontrackDBCallerConfig extends DBCallerConfig {

	private static final String ANALYTICS = "analytics";
	private static final String MOONTRACK_EVENTS = "moontrackevents";

	@Override
	public void init() {
		super.init();

		add(AnalyticsMessage.class, ANALYTICS);
		add(TrackRegistrationMessageV2.class, ANALYTICS);
		add(EventMessage.class, ANALYTICS);
		add(TrackIAPMessageV2.class, ANALYTICS);
		add(TrackLoginMessageV2.class, ANALYTICS);
		add(TrackInstallMessageV2.class, ANALYTICS);
		add(DistributionRegistrationMessageV2.class, ANALYTICS);

		// a/b tests & segments
		add(AbtestAddedMessage.class, ANALYTICS);
		add(AbtestEditedMessage.class, ANALYTICS);
		add(AbtestArchivedMessage.class, ANALYTICS);
		add(SegmentAddedMessage.class, ANALYTICS);
		add(SegmentEditedMessage.class, ANALYTICS);
		add(SegmentArchivedMessage.class, ANALYTICS);

		add(PackedEventsMessage.class, MOONTRACK_EVENTS);
		add(TestMessage.class, MOONTRACK_EVENTS);
	}

}
