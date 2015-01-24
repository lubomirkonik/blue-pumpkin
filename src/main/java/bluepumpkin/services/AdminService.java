package bluepumpkin.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bluepumpkin.domain.Employee;
import bluepumpkin.domain.Event;
import bluepumpkin.domain.Participant;
import bluepumpkin.domain.Team;
import bluepumpkin.repository.EmployeeRepository;
import bluepumpkin.repository.EventRepository;
import bluepumpkin.repository.ParticipantRepository;
import bluepumpkin.repository.TeamRepository;

@Service
public class AdminService {
	
	private final ParticipantRepository participantRepository;
	private final EventRepository eventRepository;
	private final EmployeeRepository employeeRepository;
	private final TeamRepository teamRepository;
	
	@Autowired
	public AdminService(final ParticipantRepository participantRepository,
			final EventRepository eventRepository,
			final EmployeeRepository employeeRepository,
			final TeamRepository teamRepository) {
		this.participantRepository = participantRepository;
		this.eventRepository = eventRepository;
		this.employeeRepository = employeeRepository;
		this.teamRepository = teamRepository;
	}

	public List<Participant> getWaitingParticipations() {
		List<Participant> waitingParticipations = participantRepository.findByStatus("Waiting");
		Collections.reverse(waitingParticipations);
		return waitingParticipations;
	}

	public void changeParticipationStatusToApproved(String id) {
		Participant participation = participantRepository.findOne(id);
		participation.setStatus("Approved");
		participantRepository.save(participation);
	}

	public void changeParticipationStatusToDenied(String id) {
		Participant participation = participantRepository.findOne(id);
		participation.setStatus("Denied");
		participantRepository.save(participation);
	}

	public List<Event> getUpcomingEvents() {
		List<Event> allEvents = eventRepository.findAll();
		List<Event> upcomingEvents = new ArrayList<>();
		for (Event event : allEvents) {
			if (event.getDateTime().compareTo(new Date()) > 0) {
				upcomingEvents.add(event);
			}
		}
		upcomingEvents = sortEventsByTime(upcomingEvents);
//		sorted by time ascending
		return upcomingEvents;
	}
	public List<Event> getPastEvents() {
		List<Event> allEvents = eventRepository.findAll();
		List<Event> pastEvents = new ArrayList<>();
		for (Event event : allEvents) {
			if (event.getDateTime().compareTo(new Date()) < 0) {
				pastEvents.add(event);
			}
		}
		pastEvents = sortEventsByTime(pastEvents);
		Collections.reverse(pastEvents);
//		sorted by time descending
		return pastEvents;
	}
	private List<Event> sortEventsByTime(List<Event> events) {
		List<Event> sorted = events.stream()
			.sorted((p1, p2) -> p1.getDateTime().compareTo(p2.getDateTime()))
			.collect(Collectors.toList());	
		return sorted;
	}
	
	public Event findEvent(String id) {
		return eventRepository.findOne(id);
	}
	
	public void createEvent(Event event) {
		event.setId(UUID.randomUUID().toString());
		eventRepository.save(event);
	}
	
	public void updateEvent(Event event) {
		eventRepository.save(event);
	}
	
	public List<String> getEventTypes() {
		return Arrays.asList("Meeting,Training,Sports Event,Trip".split(","));
	}

	public void deleteEvent(String id) {
		List<Participant> participations = participantRepository.findAll();
		for (Participant p : participations) {
			if (p.getEventID().getId() == id) {
				participantRepository.delete(p);
			}
		}
//		TODO filter by event type 'Sports Event'
		List<Team> teams = teamRepository.findAll();
		for (Team t : teams) {
			if (t.getEventID().getId() == id) {
				teamRepository.delete(t);
			}
		}
		eventRepository.delete(id);
	}
	
	public List<Employee> getAccounts() {
		return employeeRepository.findAll().stream()
				.sorted((c1, c2) -> c1.getLastName().compareToIgnoreCase(c2.getLastName()))
				.collect(Collectors.toList());
	}

	
}
