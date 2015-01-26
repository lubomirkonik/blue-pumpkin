package bluepumpkin.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bluepumpkin.domain.Employee;
import bluepumpkin.domain.Event;
import bluepumpkin.domain.Participant;
import bluepumpkin.domain.Team;
import bluepumpkin.domain.web.Birthday;
import bluepumpkin.repository.EmployeeRepository;
import bluepumpkin.repository.EventRepository;
import bluepumpkin.repository.ParticipantRepository;
import bluepumpkin.repository.TeamRepository;

//@Transactional
@Service
public class EmployeeService {

	private final EmployeeRepository employeeRepository;
	private final ParticipantRepository participantRepository;
	private final EventRepository eventRepository;
	private final TeamRepository teamRepository;
	
	@Autowired
	public EmployeeService(final EmployeeRepository employeeRepository,
			final ParticipantRepository participantRepository, 
			final EventRepository eventRepository,
			final TeamRepository teamRepository) {
		this.employeeRepository = employeeRepository;
		this.participantRepository = participantRepository;
		this.eventRepository = eventRepository;
		this.teamRepository = teamRepository;
	}
	
	public Employee save(Employee employee) {
		employeeRepository.save(employee);
		return employee;
	}
	
	public Employee findOne(Long id) {
		try {
			return employeeRepository.findOne(id);
		} catch (PersistenceException e) {
			return null;
		}
	}
	
	public Employee findByEmail(String email) {
		try {
			return employeeRepository.findByEmail(email);
		} catch (PersistenceException e) {
			return null;
		}
	}
	
//	TODO move it to the commonsService
	public List<Birthday> getBirthdays() {
		List<Employee> employees = employeeRepository.findAll();
		List<Birthday> birthdays = new ArrayList<>();
		if (employees.isEmpty()) {
			return birthdays; //Collections.emptyList();
		}
		for (Employee e : employees) {
			Date bDate = e.getDateOfBirth();
			
			Calendar birthDate = Calendar.getInstance();
			birthDate.setTime(bDate);
			
			int birthDay = birthDate.get(Calendar.DAY_OF_MONTH);
			int birthMonth = birthDate.get(Calendar.MONTH);
			
			Calendar now = Calendar.getInstance();
			
			if (birthDay == now.get(Calendar.DAY_OF_MONTH) && birthMonth == now.get(Calendar.MONTH)) {
				int birthYear = birthDate.get(Calendar.YEAR);
				int nowYear = now.get(Calendar.YEAR);
				int age = nowYear - birthYear;
				Birthday empBirthday = new Birthday(e.getFirstName(), e.getLastName(), e.getPosition(), e.getDepartment(), age);
				birthdays.add(empBirthday);
			}
		}
		return birthdays;
	}
	
//	Gets most recent sports event with score
	public Event getLatestSportsEvent() {
//		List<Event> events = eventRepository.findByType("Sports Event");		
//		List<Event> eventsWithScore = new ArrayList<>();
////		Find events whose teams have score
//		for (Event e : events) {
//			if (e.getTeamList().get(0) != null && e.getTeamList().get(1) != null) {
//				eventsWithScore.add(e);
//			}
//		}
		
//		All teams
		List<Team> teams = teamRepository.findAll();
//		Teams with score - to ensure that it is past sports event    with score
		List<Team> teamsWithScore = teams.stream()
		.filter(t -> t.getScore() != null)
		.collect(Collectors.toList());
//		Events from teams with score
		List<Event> eventsOfTeamsWithScore = teamsWithScore.stream()
		.map(t -> t.getEventID())
		.distinct() //teams are two for one event, so we need to reduce the two same events in stream to one
		.collect(Collectors.toList());
//		Find latest sports event
		Event latest = eventsOfTeamsWithScore.stream()
		.max((e1, e2) -> e1.getDateTime().compareTo(e2.getDateTime()))
		.orElse(null);
		
//		Find teams for the latest event and add them to the latest event
		teamsWithScore.stream()
		.filter(t -> t.getEventID().getId() == latest.getId())
		.forEach(t -> latest.getTeamList().add(t));

		return latest;
	}
	
	private List<Participant> getParticipationsNotSorted(Long employeeId, List<Participant> allParticipations) {
		List<Participant> participations = new ArrayList<>();
		for (Participant participation : allParticipations) {
			if (participation.getEmployeeID().getId() == employeeId && 
					participation.getEventID().getDateTime().compareTo(new Date()) > 0) {
				
				participations.add(participation);
			}
		}
		return participations;
	}
	
	public List<Participant> getParticipations(Long employeeId) {
//		TODO create findByEmployeeID method in ParticipantRepository
		List<Participant> allParticipations = participantRepository.findAll();
		if (allParticipations.isEmpty())
			return allParticipations;
//		TODO if getParticipationsNotSorted returns empty list return empty list
		List<Participant> sorted = getParticipationsNotSorted(employeeId, allParticipations).stream()
		.sorted((p1, p2) -> p1.getEventID().getDateTime().compareTo(p2.getEventID().getDateTime()))
		.collect(Collectors.toList());
		return sorted;
	}
	
	public List<Participant> getUpcomingEvents(Long employeeId) {
		List<Participant> participations = new ArrayList<>();
		List<Event> allEvents = eventRepository.findAll();
		List<Event> upcomingEvents = new ArrayList<>();
//		if (allEvents.isEmpty())
//			return participations;
		List<Participant> allParticipations = participantRepository.findAll();
		List<Participant> participationsWithStatNull = new ArrayList<>();
		
//		in case no participations exist, but some events may exist
		if (allParticipations.isEmpty()) {
			return transformUpcomingEventsToParticipationsWithStatNull(allEvents, employeeId, 
					participationsWithStatNull);
		
//		employee has some participations requests	
		} else if (!(participations = getParticipationsNotSorted(employeeId, allParticipations)).isEmpty()) {
			for (Event event : allEvents) {
				if (event.getDateTime().compareTo(new Date()) > 0) {
					upcomingEvents.add(event);
				}
			}
//			- nested iteration			
//			event 1		part2
//			event 2 	part3
//			event 3		-----
			for (Event e : upcomingEvents) { 
				Iterator<Participant> pIt = participations.iterator();
					while(true) {
						Participant p = pIt.next();
//							if event id equals part.request event id, terminate nested iteration
							if (e.getId() == p.getEventID().getId())
								break;
//							if IDs don't equal and it is last element in the nested iteration, create Participant with status null (transform event to participant for employee)
							if (!pIt.hasNext()) {
								Participant pStatNull = new Participant();
								pStatNull.setId(UUID.randomUUID().toString());
								pStatNull.setEmployeeID(employeeRepository.findOne(employeeId));
								pStatNull.setEventID(e);
								pStatNull.setStatus(null);
								participationsWithStatNull.add(pStatNull);
								break;
							}
					}			
			}
			participations.addAll(participationsWithStatNull);
			
			List<Participant> sorted = participations.stream()
			.sorted((p1, p2) -> p1.getEventID().getDateTime().compareTo(p2.getEventID().getDateTime()))
			.collect(Collectors.toList());	
			return sorted;
			
		} else {
//			employee doesn't have any participations requests
//			do the same as in case of no participations at all
			return transformUpcomingEventsToParticipationsWithStatNull(allEvents, employeeId, 
					participationsWithStatNull);
		}
		
	}
	
	private List<Participant> transformUpcomingEventsToParticipationsWithStatNull(List<Event> allEvents, 
			Long employeeId, List<Participant> participationsWithStatNull) {
		List<Event> upcomingEvents = new ArrayList<>();
		
		for (Event event : allEvents) {
			if (event.getDateTime().compareTo(new Date()) > 0) {
				upcomingEvents.add(event);
			}
		}
		for (Event e : upcomingEvents) {
			Participant pStatNull = new Participant();
			pStatNull.setId(UUID.randomUUID().toString());
			pStatNull.setEmployeeID(employeeRepository.findOne(employeeId));
			pStatNull.setEventID(e);
			pStatNull.setStatus(null);
			participationsWithStatNull.add(pStatNull);
		}
		List<Participant> sorted = participationsWithStatNull.stream()
			.sorted((p1, p2) -> p1.getEventID().getDateTime().compareTo(p2.getEventID().getDateTime()))
			.collect(Collectors.toList());	
			return sorted;
	}
	
	public void createParticipationRequest(Employee employee, String eventId) {
		Event event = eventRepository.findOne(eventId);
		Participant participation = new Participant(UUID.randomUUID().toString(), "Waiting", employee, event);
		participantRepository.save(participation);
	}
	
	public List<Event> getPastEvents() {
		List<Event> allEvents = eventRepository.findAll();
		List<Event> pastEvents = new ArrayList<>();
		for (Event event : allEvents) {
			if (event.getDateTime().compareTo(new Date()) < 0) {
				pastEvents.add(event);
			}
		}
		List<Event> sorted = pastEvents.stream()
			.sorted((p1, p2) -> p1.getDateTime().compareTo(p2.getDateTime()))
			.collect(Collectors.toList());	
		Collections.reverse(sorted);
//		sorted by time descending
		return sorted;
	}
	
	public List<Employee> getContacts() {
		Comparator<Employee> byLastName = (c1, c2) -> c1.getLastName()
	            .compareTo(c2.getLastName());
		Comparator<Employee> byDepartment = (c1, c2) -> c1.getDepartment()
	            .compareTo(c2.getDepartment());
		return employeeRepository.findAll().stream()
				.sorted(byDepartment.thenComparing(byLastName))
				.collect(Collectors.toList());
	}

	public void createTeam(Team team) {
		teamRepository.save(team);
	}

}
