package bluepumpkin.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bluepumpkin.domain.Employee;
import bluepumpkin.domain.Event;
import bluepumpkin.domain.Participant;
import bluepumpkin.repository.EmployeeRepository;
import bluepumpkin.repository.EventRepository;
import bluepumpkin.repository.ParticipantRepository;

//@Transactional
@Service
public class EmployeeService {

	private final EmployeeRepository employeeRepository;
	private final ParticipantRepository participantRepository;
	private final EventRepository eventRepository;
	
	@Autowired
	public EmployeeService(final EmployeeRepository employeeRepository,
			final ParticipantRepository participantRepository, 
			final EventRepository eventRepository) {
		this.employeeRepository = employeeRepository;
		this.participantRepository = participantRepository;
		this.eventRepository = eventRepository;
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
	
	private List<Participant> getEmployeeParticipations(Long employeeId) {
		List<Participant> participations = new ArrayList<>();
		for (Participant participation : participantRepository.findAll()) {
			if (participation.getEmployeeID().getId() == employeeId && 
					participation.getEventID().getDateTime().compareTo(new Date()) > 0) {
				
				participations.add(participation);
			}
		}
		return participations;
	}
	
	public List<Participant> getParticipations(Long employeeId) {
		List<Participant> sorted = getEmployeeParticipations(employeeId).stream()
		.sorted((p1, p2) -> p1.getEventID().getDateTime().compareTo(p2.getEventID().getDateTime()))
		.collect(Collectors.toList());
		
		return sorted;
	}
	
	public List<Participant> getUpcomingEvents(Long employeeId) {
		List<Participant> employeeParticipations = getEmployeeParticipations(employeeId);
		List<Participant> participationsEvents = new ArrayList<>();
		
		List<Event> upcomingEvents = new ArrayList<>();
		for (Event event : eventRepository.findAll()) {
			if (event.getDateTime().compareTo(new Date()) > 0) {
				upcomingEvents.add(event);
			}
		}
		for (Participant p : employeeParticipations) {
			for (Event e : upcomingEvents) {
				if (p.getEventID().getId() != e.getId()) {
					Participant pNoStat = new Participant();
					pNoStat.setId(UUID.randomUUID().toString());
					pNoStat.setEmployeeID(employeeRepository.findOne(employeeId));
					pNoStat.setEventID(e);
					pNoStat.setStatus(null);
					participationsEvents.add(pNoStat);
				}
				break;
			}
		}
		participationsEvents.addAll(employeeParticipations);
		
		List<Participant> sorted = participationsEvents.stream()
		.sorted((p1, p2) -> p1.getEventID().getDateTime().compareTo(p2.getEventID().getDateTime()))
		.collect(Collectors.toList());	
		return sorted;
	}
	
}
