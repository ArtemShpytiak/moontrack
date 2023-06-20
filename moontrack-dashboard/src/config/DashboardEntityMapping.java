package config;

import org.hibernate.cfg.Configuration;

import com.moonmana.moontrack.abtests.cache.AbtestCache;
import com.moonmana.moontrack.model.Company;
import com.moonmana.moontrack.model.Event;
import com.moonmana.moontrack.model.Game;
import com.moonmana.moontrack.model.GameWidget;
import com.moonmana.moontrack.model.Platform;
import com.moonmana.moontrack.model.Realm;
import com.moonmana.moontrack.model.User;
import com.moonmana.moontrack.model.UtmLink;
import com.moonmana.moontrack.model.Widget;
import com.moonmana.moontrack.model.cache.DashboardMetricCache;
import com.moonmana.moontrack.model.cache.GameDashboardCache;
import com.moonmana.moontrack.segments.cache.SegmentCache;

import core.hibernate.EntityMapping;
import hive.model.abtest.AbTestGroup;
import hive.model.abtest.AbTestGroupByFilter;
import hive.model.abtest.AbTestGroupByPercentage;
import hive.model.abtest.AbTestGroupBySegment;
import hive.model.abtest.AbTest;
import hive.model.filter.FilterCountry;
import hive.model.filter.FilterDevice;
import hive.model.filter.FilterHub;
import hive.model.filter.FilterOs;
import hive.model.filter.FilterPlatform;
import hive.model.filter.FilterRealm;
import hive.model.segment.Segment;

public class DashboardEntityMapping extends EntityMapping {

	@Override
	public void initEntities(Configuration config) {
		config.addAnnotatedClass(Company.class);
		config.addAnnotatedClass(Game.class);
		config.addAnnotatedClass(User.class);
		config.addAnnotatedClass(Event.class);
		config.addAnnotatedClass(Realm.class);
		config.addAnnotatedClass(Platform.class);
		config.addAnnotatedClass(Widget.class);
		config.addAnnotatedClass(GameWidget.class);
		config.addAnnotatedClass(UtmLink.class);

		config.addAnnotatedClass(AbTest.class);
		config.addAnnotatedClass(AbTestGroup.class);
		config.addAnnotatedClass(AbTestGroupByFilter.class);
		config.addAnnotatedClass(AbTestGroupByPercentage.class);
		config.addAnnotatedClass(AbTestGroupBySegment.class);

		config.addAnnotatedClass(FilterHub.class);
		config.addAnnotatedClass(FilterRealm.class);
		config.addAnnotatedClass(FilterPlatform.class);
		config.addAnnotatedClass(FilterCountry.class);
		config.addAnnotatedClass(FilterDevice.class);
		config.addAnnotatedClass(FilterOs.class);

		config.addAnnotatedClass(Segment.class);

		// cache
		config.addAnnotatedClass(AbtestCache.class);
		config.addAnnotatedClass(DashboardMetricCache.class);
		config.addAnnotatedClass(GameDashboardCache.class);
		config.addAnnotatedClass(SegmentCache.class);
	}

}
