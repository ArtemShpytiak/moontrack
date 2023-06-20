package moontrack.analytics;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.hibernate.Session;

public class EventTableSchema {
	private final Map<String, Class<?>> fields;  
	private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
	
	
	public EventTableSchema(Map<String, Class<?>> fields) {
		this.fields = new HashMap<>(fields);
	}

	public SchemeVerificationResult verify(MoontrackClientEvent event) {
		rwLock.readLock().lock();
		try {
			return allMatch(event.boolMap, Boolean.class)
					.merge(allMatch(event.intMap, Integer.class))
					.merge(allMatch(event.doubleMap, Double.class))
					.merge(allMatch(event.stringMap, String.class));
		} finally {
			rwLock.readLock().unlock();
		}
	}

	private final <T>SchemeVerificationResult allMatch(Map<String, T> valuesMap, Class<?> class1) {
		SchemeVerificationResult result = new SchemeVerificationResult();
		for (String fieldName : valuesMap.keySet()) {
			result.verify(fields, fieldName, class1);
		}
		return result;
		
		//return valuesMap.keySet().stream().collect(new VerificationResultCollector(fields, class1)); //((k)->fields.get(k) == class1);
	}
	
	
	public void updateSchema(MoontrackClientEvent event, Connection connection, Session session) {
		rwLock.writeLock().lock();
		SchemeVerificationResult result = verify(event);
		if (!result.isOK()) {
			
		}
		
		rwLock.writeLock().unlock();
	}
	
}
