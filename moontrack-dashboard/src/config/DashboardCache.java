package config;

import com.moonmana.moontrack.model.Company;
import com.moonmana.moontrack.model.Game;

import core.cache.DataCache;

public class DashboardCache extends DataCache {

	public DashboardCache(String databaseKey) {
		super(databaseKey);
	
	}
	@Override
	public void initCache() {
		super.initCache();
		addEntity(Company.class, 1, 1);
		addEntity(Game.class, 1, 1);
	}
}
