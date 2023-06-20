package moontrack.analytics;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;

import com.moonmana.log.Log;

public class AppInstallsManager {
	public static Map<Short, Set<String>> appInstalls;
		
	public synchronized static void initAppInstalls(Session session) {
		appInstalls = new HashMap<>();
		List<Object[]> list = session.createQuery("SELECT deviceId, gameId FROM AppInstall", Object[].class).list();
		fillAppInstalls(list);
		Log.out("AppInstallsManager inited with " + appInstalls.size() + " records");
	}

	private static void fillAppInstalls(List<Object[]> list) {
		for (Object[] o : list) {
			Short gameId = (Short) o[1];
			if(!appInstalls.containsKey(gameId)) {
				appInstalls.put(gameId, new HashSet<>());
			}
			appInstalls.get(gameId).add((String) o[0]);
			
		}
	}
	
	public static Boolean isAppInstallListed(String deviceId, short gameId) {
		return appInstalls.getOrDefault(gameId, Collections.emptySet()).contains(deviceId);	
	}
	
	public static void appendDeviceId(String deviceId, short gameId) {
		Set<String> set = new HashSet<>();
		Set<String> previous = appInstalls.putIfAbsent(gameId, set);
		if (previous != null) {
			set = previous;
		}
		set.add(deviceId);
	}
}
