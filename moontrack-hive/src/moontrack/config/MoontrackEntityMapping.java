package moontrack.config;

import org.hibernate.cfg.Configuration;

import analytics.model.Activity;
import analytics.model.AnalyticsEvent;
import analytics.model.AppInstall;
import analytics.model.IAPPurchase;
import analytics.model.UserLogin;
import analytics.model.UserSession;
import analytics.model.user.ARealmUser;
import analytics.model.user.AnalyticsUser;
import analytics.model.user.DistributionUserRegistration;
import analytics.model.user.IncompleteInfo;
import core.hibernate.EntityMapping;
import core.userIdentification.DeviceIdContainer;
import hive.model.abtest.AbTest;
import hive.model.abtest.AbTestGroup;
import hive.model.abtest.AbTestGroupByFilter;
import hive.model.abtest.AbTestGroupByPercentage;
import hive.model.abtest.AbTestGroupBySegment;
import hive.model.abtest.AbTestUser;
import hive.model.filter.FilterCountry;
import hive.model.filter.FilterDevice;
import hive.model.filter.FilterHub;
import hive.model.filter.FilterOs;
import hive.model.filter.FilterPlatform;
import hive.model.filter.FilterRealm;
import hive.model.segment.Segment;
import hive.model.segment.SegmentUser;

public class MoontrackEntityMapping extends EntityMapping {

	@Override
	public void initEntities(Configuration config) {
		config.addAnnotatedClass(UserLogin.class);
		config.addAnnotatedClass(IAPPurchase.class);
		config.addAnnotatedClass(AppInstall.class);
		config.addAnnotatedClass(AnalyticsEvent.class);
		config.addAnnotatedClass(UserSession.class);
		config.addAnnotatedClass(Activity.class);
		config.addAnnotatedClass(ARealmUser.class);
		config.addAnnotatedClass(DistributionUserRegistration.class);
		config.addAnnotatedClass(AnalyticsUser.class);
		config.addAnnotatedClass(DeviceIdContainer.class);
		config.addAnnotatedClass(IncompleteInfo.class);

		config.addAnnotatedClass(AbTest.class);
		config.addAnnotatedClass(AbTestGroup.class);
		config.addAnnotatedClass(AbTestGroupByPercentage.class);
		config.addAnnotatedClass(AbTestGroupByFilter.class);
		config.addAnnotatedClass(AbTestGroupBySegment.class);
		config.addAnnotatedClass(AbTestUser.class);

		config.addAnnotatedClass(FilterHub.class);
		config.addAnnotatedClass(FilterCountry.class);
		config.addAnnotatedClass(FilterDevice.class);
		config.addAnnotatedClass(FilterOs.class);
		config.addAnnotatedClass(FilterPlatform.class);
		config.addAnnotatedClass(FilterRealm.class);

		config.addAnnotatedClass(Segment.class);
		config.addAnnotatedClass(SegmentUser.class);
	}

}
