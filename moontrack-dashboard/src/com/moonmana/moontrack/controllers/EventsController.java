package com.moonmana.moontrack.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.moonmana.moontrack.model.Event;
import com.moonmana.moontrack.repositories.HiveDAO;

@Component
public class EventsController {

	private List<Event> events;
	private Map<Integer, String> prefixList;

	@SuppressWarnings("unchecked")
	private void initEvents() {
		events = new ArrayList<Event>();
		Session session = HiveDAO.openSession();
		Query<?> query = session.createNativeQuery("SELECT table_name FROM moontrack_event_table_schemas");
		List<String> eventList = (List<String>) query.getResultList();

//		query = session.createNativeQuery("SELECT id, app_inner_alias FROM application_alias");
//		prefixList = (Map<Integer, String>) query.getResultList();
		session.close();

		Set<String> eventNames = new HashSet<String>();
		for (String eventName : eventList) {
			eventNames.add(eventName.split("_", 2)[1]);
		}
		int eventId = 0;
		for (String name : eventNames) {
			Event event = new Event();
			event.setId(eventId);
			event.setName(name);
			events.add(eventId, event);
			eventId++;
		}
	}

	public List<Event> getEvents() {
		if (events == null) {
			initEvents();
		}
		return this.events;
	}

	public Map<Integer, String> getPrefixList() {
		return prefixList;
	}
}
