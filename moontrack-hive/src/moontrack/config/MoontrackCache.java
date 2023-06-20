package moontrack.config;

import analytics.model.UserLogin;
import core.cache.DataCache;

public class MoontrackCache extends DataCache {

	public MoontrackCache(String databaseKey) {
		super(databaseKey);
	}

	@Override
	public void initCache() {
		super.initCache();
		addCache(UserLogin.class, 20000, 1);		
	}

	public static MoontrackCache getInstance() {
		return null;
	}

}
