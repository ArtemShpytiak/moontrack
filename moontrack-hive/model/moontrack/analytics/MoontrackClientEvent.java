package moontrack.analytics;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class MoontrackClientEvent {
	public final String name;
	public final Date date;
	public final Map<String, Boolean> boolMap;
	public final Map<String, Double> doubleMap;
	public final Map<String, Integer> intMap;
	public final Map<String, String> stringMap;
	public final byte dateStrategy;

	public MoontrackClientEvent(String name, Date date, byte dateStrategy, Map<String, Boolean> boolMap,
			Map<String, Double> doubleMap, Map<String, Integer> intMap,
			Map<String, String> stringMap) {
		this.name = name;
		this.date = date;
		this.dateStrategy = dateStrategy;
		this.boolMap = Collections.unmodifiableMap(boolMap);
		this.doubleMap = Collections.unmodifiableMap(doubleMap);
		this.intMap = Collections.unmodifiableMap(intMap);
		this.stringMap = Collections.unmodifiableMap(stringMap);
	}

	public final int fieldsNumber() {
		return boolMap.size() + doubleMap.size() + intMap.size() + stringMap.size();
	}
	
	

}
