package bluepumpkin.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import bluepumpkin.domain.Account;
import bluepumpkin.domain.Employee;
import bluepumpkin.domain.Event;
import bluepumpkin.domain.Participant;
import bluepumpkin.domain.Team;
import bluepumpkin.services.AccountService;
import bluepumpkin.services.EmployeeService;
import bluepumpkin.services.EventService;
import bluepumpkin.services.ParticipantService;
import bluepumpkin.support.web.MessageHelper;

@Controller
public class EmployeeController {

	private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);
	
	private final AccountService accountService;
	private final EmployeeService employeeService;
	private final EventService eventService;
	private final ParticipantService participantService;
	
	@Autowired
	public EmployeeController(AccountService accountService, EmployeeService employeeService,
			EventService eventService, ParticipantService participantService) {
		this.accountService = accountService;
		this.employeeService = employeeService;
		this.eventService = eventService;
		this.participantService = participantService;
	}
	
	private Employee getEmployee(Principal principal) {
		Assert.notNull(principal);
		return employeeService.findByEmail(principal.getName());
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String employeeHome(Model model, Principal principal) {
		if (accountService.findByEmail(principal.getName()).isAdmin()) {
			return "redirect:/admin";
		}
		Employee employee = getEmployee(principal);
		model.addAttribute("navigation", "pages");
		model.addAttribute("birthdays", employeeService.getBirthdays());
		model.addAttribute("sportsEvent", employeeService.getLatestSportsEvent());
		LOG.debug("Employee's participation requests to home view");
		model.addAttribute("participations", employeeService.getParticipations(employee.getId()));
		return "employee/home";
	}
	
	@RequestMapping(value = "/participations/{eventId}/doRequest", method = RequestMethod.GET)
	public String requestForParticipation(@PathVariable String eventId, Principal principal, RedirectAttributes redirectAttrs) {
		Employee employee = getEmployee(principal);
		employeeService.createParticipationRequest(employee, eventId);
//		TODO assure that request was created
		MessageHelper.addSuccessAttribute(redirectAttrs, "You have requested for the participation!");
		return "redirect:/upcomingEvents";
	}
	
	@RequestMapping(value = "/participations/{id}/cancel", method = RequestMethod.GET) 
	public String cancelParticipation(@PathVariable String id, @RequestParam("page") String page, RedirectAttributes redirectAttrs) {
		participantService.delete(id);
		MessageHelper.addSuccessAttribute(redirectAttrs, "Your participation request has been canceled!");
		if (page.equals("home"))
			 return "redirect:/";
		else return "redirect:/upcomingEvents";
	}
	
	@RequestMapping(value = "/upcomingEvents", method = RequestMethod.GET)
	public String getUpcomingEvents(Model model, Principal principal) {
		Employee employee = getEmployee(principal);
		model.addAttribute("navigation", "pages");
		LOG.debug("Upcoming events for employee");
		model.addAttribute("upcomingEvents", employeeService.getUpcomingEvents(employee.getId()));
		return "employee/upcomingEvents";
	}
	
	@RequestMapping(value = "/pastEvents", method = RequestMethod.GET)
	public String getPastEvents(Model model) {
		model.addAttribute("navigation", "pages");
		LOG.debug("All past events");
		model.addAttribute("pastEvents", employeeService.getPastEvents());
		return "employee/pastEvents";
	}
	
	@RequestMapping(value = "/contacts", method = RequestMethod.GET)
	public String getAccounts(Model model) {
		model.addAttribute("navigation", "pages");
		LOG.debug("All contacts");
		model.addAttribute("contacts", employeeService.getContacts());
		return "employee/contacts";
	}
	
	@PostConstruct
	private void init() {
//		Employee for default account "user"
		Account account = accountService.findByEmail("user");
		Employee employee = employee(account.getId(), "firstName", "lastName", "position", "department", "telephone", account.getEmail(), getDate(1980, 1, 20));
		employee = employeeService.save(employee);
		
//		Accounts and Employees
		account = new Account("user1@bluepumpkin.com", "user", Account.ROLE_USER);
		account = accountService.save(account);
		Employee employee1 = employee(account.getId(), "firstName1", "lastName1", "position1", "department1", "telephone1", account.getEmail(), getBirthday(1970));
		employeeService.save(employee1);
		
		account = new Account("user2@bluepumpkin.com", "user", Account.ROLE_USER);
		account = accountService.save(account);
		Employee employee2 = employee(account.getId(), "firstName1", "lastName1", "position1", "department1", "telephone1", account.getEmail(), getBirthday(1983));
		employeeService.save(employee2);
		
		account = new Account("user3@bluepumpkin.com", "user", Account.ROLE_USER);
		account = accountService.save(account);
		Employee employee3 = employee(account.getId(), "firstName1", "lastName1", "position1", "department1", "telephone1", account.getEmail(), getBirthday(1964));
		employeeService.save(employee3);
		
		account = new Account("user4@bluepumpkin.com", "user", Account.ROLE_USER);
		account = accountService.save(account);
		Employee employee4 = employee(account.getId(), "firstName1", "lastName1", "position1", "department1", "telephone1", account.getEmail(), getDate(1970, 1, 20));
		employeeService.save(employee4);
		
		account = new Account("user5@bluepumpkin.com", "user", Account.ROLE_USER);
		account = accountService.save(account);
		Employee employee5 = employee(account.getId(), "firstName1", "lastName1", "position1", "department1", "telephone1", account.getEmail(), getDate(1970, 1, 20));
		employeeService.save(employee5);
		
		account = new Account("user6@bluepumpkin.com", "user", Account.ROLE_USER);
		account = accountService.save(account);
		Employee employee6 = employee(account.getId(), "firstName1", "lastName1", "position1", "department1", "telephone1", account.getEmail(), getDate(1970, 1, 20));
		employeeService.save(employee6);
		
		account = new Account("user7@bluepumpkin.com", "user", Account.ROLE_USER);
		account = accountService.save(account);
		Employee employee7 = employee(account.getId(), "firstName1", "lastName1", "position1", "department1", "telephone1", account.getEmail(), getDate(1970, 1, 20));
		employeeService.save(employee7);
		
		account = new Account("user8@bluepumpkin.com", "user", Account.ROLE_USER);
		account = accountService.save(account);
		Employee employee8 = employee(account.getId(), "firstName1", "lastName1", "position1", "department1", "telephone1", account.getEmail(), getDate(1970, 1, 20));
		employeeService.save(employee8);
		
		account = new Account("user9@bluepumpkin.com", "user", Account.ROLE_USER);
		account = accountService.save(account);
		Employee employee9 = employee(account.getId(), "firstName1", "lastName1", "position1", "department1", "telephone1", account.getEmail(), getDate(1970, 1, 20));
		employeeService.save(employee9);
		
		account = new Account("user10@bluepumpkin.com", "user", Account.ROLE_USER);
		account = accountService.save(account);
		Employee employee10 = employee(account.getId(), "firstName1", "lastName1", "position1", "department1", "telephone1", account.getEmail(), getDate(1970, 1, 20));
		employeeService.save(employee10);
		
//		Events
		Event event = new Event(UUID.randomUUID().toString(), "Sports Event", "Football", "Near Football Stadium", getDateEvent(2015,5,20,8,30),"description");
		eventService.save(event);
		Event eventOne = new Event(UUID.randomUUID().toString(), "Sports Event", "Football1", "Near Football Stadium1", getDateEvent(2015,5,23,10,00), "description1");
		eventService.save(eventOne);
		Event eventTwo = new Event(UUID.randomUUID().toString(), "Sports Event", "Football2", "Near Football Stadium2", getDateEvent(2015,5,21,13,15), "description2");
		eventService.save(eventTwo);
		Event eventThree = new Event(UUID.randomUUID().toString(), "Meeting", "Annual Meeting", "Manager Office", getDateEvent(2015,5,26,9,00), "description3");
		eventService.save(eventThree);
		Event eventFour = new Event(UUID.randomUUID().toString(), "Meeting", "Annual Meeting1", "Manager Office1", getDateEvent(2015,5,25,12,00), "description4");
		eventService.save(eventFour);
		
//		Past Sports Events
		Event sportsEvent = new Event(UUID.randomUUID().toString(), "Sports Event", "Football3", "Near Football Stadium", getDatePastEvent(), "");
		eventService.save(sportsEvent);
		Event sportsEventWithScore = new Event(UUID.randomUUID().toString(), "Sports Event", "Football4", "Near Football Stadium", getDatePastEvent(), ""); //(2015,1,22,12,00)
		eventService.save(sportsEventWithScore);
		
//		Participants for user
//		TODO remove
		Participant participant = new Participant(UUID.randomUUID().toString(), null, employee, event);
		participantService.save(participant);
		
		participant = new Participant(UUID.randomUUID().toString(), "Waiting", employee, eventOne);
		participantService.save(participant);
		participant = new Participant(UUID.randomUUID().toString(), "Approved", employee, eventTwo);
		participantService.save(participant);
		participant = new Participant(UUID.randomUUID().toString(), "Denied", employee, eventThree);
		participantService.save(participant);
		
//		Participants for user1
		participant = new Participant(UUID.randomUUID().toString(), "Waiting", employee1, event);
		participantService.save(participant);
		participant = new Participant(UUID.randomUUID().toString(), "Denied", employee1, eventThree);
		participantService.save(participant);
		participant = new Participant(UUID.randomUUID().toString(), "Approved", employee1, eventFour);
		participantService.save(participant);
		participant = new Participant(UUID.randomUUID().toString(), "Waiting", employee1, sportsEventWithScore);
		participantService.save(participant);
		
//		Teams for Sports Event 'eventTwo'
		List<Employee> members1 = Arrays.asList(employee1, employee2, employee3, employee4, employee5);
		List<Employee> members2 = Arrays.asList(employee6, employee7, employee8, employee9, employee10);
		Team team1 = new Team(UUID.randomUUID().toString(), 14, members1, sportsEventWithScore);
		Team team2 = new Team(UUID.randomUUID().toString(), 6, members2, sportsEventWithScore);
		employeeService.createTeam(team1);
		employeeService.createTeam(team2);
	}
	private Date getDate(int year, int month, int day) {
		Calendar date = Calendar.getInstance();
		date.set(year, month - 1, day);
		return date.getTime();
	}
	private Date getDateEvent(int year, int month, int day, int hour, int minute) {
		Calendar date = Calendar.getInstance();
		date.set(year, month - 1, day, hour, minute);
		return date.getTime();
	}
	private Date getDatePastEvent() {
		Calendar date = Calendar.getInstance();
		date.set(Calendar.SECOND, Calendar.getInstance().get(Calendar.SECOND) + 10);
		return date.getTime();
	}
	private Date getBirthday(int year) {
		Calendar date = Calendar.getInstance();
		date.set(Calendar.YEAR, year);
		return date.getTime();
	}
	private Employee employee(Long id, String firstName, String lastName, String position, String department, String telephone, String email, Date dateOfBirth) {
		Employee employee = new Employee();
		employee.setId(id);
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		employee.setPosition(position);
		employee.setDepartment(department);
		employee.setTelephone(telephone);
		employee.setEmail(email);
		employee.setDateOfBirth(dateOfBirth);
		return employee;
	}
	
}
