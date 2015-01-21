package bluepumpkin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bluepumpkin.domain.Event;
import bluepumpkin.repository.EventRepository;

@Service
public class EventService {

	private final EventRepository eventRepository;
	
	@Autowired
	public EventService(final EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}
	
	public Event save(Event event) {
		eventRepository.save(event);
		return event;
	}
	
	public Event findOne(String id) {
			return eventRepository.findOne(id);
	}
	
}
