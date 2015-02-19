package bluepumpkin.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
	
//	TODO could be in admin controller
	@PostConstruct
	private void init() {
//		Employee for default account "user"
		Account account = accountService.findByEmail("user");
		Employee employee = employee(account.getId(), "Andrew", "Bean", "Manager", "Accounting Department", "+191 708 654 333", account.getEmail(), getDate(1974, 1, 12));
		employee = employeeService.save(employee);
		
//		Accounts and Employees
		account = new Account("sonia.johnson@bluepumkin.com", "user", Account.ROLE_USER);
		account = accountService.save(account);
		Employee employee1 = employee(account.getId(), "Sonia", "Johnson", "Accountant", "Accounting Department", "+191 708 654 320", account.getEmail(), getBirthday(1983));
		employeeService.save(employee1);
		
		account = new Account("peter.parker@bluepumpkin.com", "user", Account.ROLE_USER);
		account = accountService.save(account);
		Employee employee2 = employee(account.getId(), "Peter", "Parker", "Accountant", "Accounting Department", "+191 408 654 000", account.getEmail(), getDate(1976, 9, 15));
		employeeService.save(employee2);
		
		account = new Account("dan.bruns@bluepumpkin.com", "user", Account.ROLE_USER);
		account = accountService.save(account);
		Employee employee3 = employee(account.getId(), "Dan", "Bruns", "IT Junior", "IT Department", "+191 408 654 444", account.getEmail(), getDate(1985, 8, 30));
		employeeService.save(employee3);
		
		account = new Account("joseph.karabas@bluepumpkin.com", "user", Account.ROLE_USER);
		account = accountService.save(account);
		Employee employee4 = employee(account.getId(), "Joseph", "Karabas", "IT Senior", "IT Department", "+191 708 654 109", account.getEmail(), getBirthday(1964));
		employeeService.save(employee4);
		
		account = new Account("andy.cole@bluepumpkin.com", "user", Account.ROLE_USER);
		account = accountService.save(account);
		Employee employee5 = employee(account.getId(), "Andy", "Cole", "IT Senior", "IT Department", "+151 608 587 779", account.getEmail(), getDate(1967, 11, 24));
		employeeService.save(employee5);
		
		account = new Account("barry.jackson@bluepumpkin.com", "user", Account.ROLE_USER);
		account = accountService.save(account);
		Employee employee6 = employee(account.getId(), "Barry", "Jackson", "Technician Junior", "Claims Department", "+191 708 653 330", account.getEmail(), getDate(1987, 7, 17));
		employeeService.save(employee6);
		
		account = new Account("carl.raid@bluepumpkin.com", "user", Account.ROLE_USER);
		account = accountService.save(account);
		Employee employee7 = employee(account.getId(), "Carl", "Raid", "Technician Senior", "Claims Department", "+191 705 029 889", account.getEmail(), getDate(1965, 12, 8));
		employeeService.save(employee7);
		
		account = new Account("alan.fabrikks@bluepumpkin.com", "user", Account.ROLE_USER);
		account = accountService.save(account);
		Employee employee8 = employee(account.getId(), "Alan", "Fabrikks", "Technician Senior", "Claims Department", "+191 700 303 444", account.getEmail(), getDate(1959, 6, 29));
		employeeService.save(employee8);
		
		account = new Account("annemarie.firefly@bluepumpkin.com", "user", Account.ROLE_USER);
		account = accountService.save(account);
		Employee employee9 = employee(account.getId(), "Annemarie", "Firefly", "HR Generalist", "HR Department", "+191 708 654 321", account.getEmail(), getDate(1976, 8, 16));
		employeeService.save(employee9);
		
		account = new Account("carmen.winch@bluepumpkin.com", "user", Account.ROLE_USER);
		account = accountService.save(account);
		Employee employee10 = employee(account.getId(), "Carmen", "Winch", "HR Generalist", "HR Department", "+191 708 444 444", account.getEmail(), getDate(1979, 5, 16));
		employeeService.save(employee10);
		
		account = new Account("katherine.boyle@bluepumpkin.com", "user", Account.ROLE_USER);
		account = accountService.save(account);
		Employee employee11 = employee(account.getId(), "Katherine", "Boyle", "HR Manager", "HR Department", "+191 708 493 766", account.getEmail(), getBirthday(1974));
		employeeService.save(employee11);
		
//		Events
		Event event = new Event(UUID.randomUUID().toString(), "Sports Event", "Skittles", "Aupark, Sunrise Avenue 146, NY", getDateEvent(2015,2,26, 16,30), "description");
		eventService.save(event);
		Event eventOne = new Event(UUID.randomUUID().toString(), "Sports Event", "Target Shooting", "Springfield West Geneseo, IL 61254", getDateEvent(2015,4,11, 10,00), "description");
		eventService.save(eventOne);
		Event eventTwo = new Event(UUID.randomUUID().toString(), "Sports Event", "Basketball", "Hall, Grapevine Lane Stone City, 57216", getDateEvent(2015,5,21, 13,15), "description");
		eventService.save(eventTwo);
		Event eventThree = new Event(UUID.randomUUID().toString(), "Meeting", "Internal Annual Meeting", "Conference Hall BP Head.", getDateEvent(2015,12,15, 9,00), "description");
		eventService.save(eventThree);
		Event eventFour = new Event(UUID.randomUUID().toString(), "Meeting", "Meeting with customer, Orion Group", "Bluepumpkin Headquarters", getDateEvent(2015,5,14, 13,30), "description");
		eventService.save(eventFour);
		Event eventFive = new Event(UUID.randomUUID().toString(), "Training", "IT Training, Microservices", "Microsoft, Apollo Business Center II", getDateEvent(2015,3,6, 9,15), "description");
		eventService.save(eventFive);
		Event eventSix = new Event(UUID.randomUUID().toString(), "Training", "Payroll News 2015", "PayWell Headquarters, TI", getDateEvent(2015,4,17, 13,45), "description");
		eventService.save(eventSix);
		Event eventSeven = new Event(UUID.randomUUID().toString(), "Trip", "Hiking", "Blue Lake, Saint Helier, Jersey", getDateEvent(2015,6,2, 8,05), "description");
		eventService.save(eventSeven);
		Event eventEight = new Event(UUID.randomUUID().toString(), "Trip", "Mountain climbing", "Rock Hill, South Carolina", getDateEvent(2015,8,17, 7,45), "description");
		eventService.save(eventEight);
		
//		Past Events
		Event eventNine = new Event(UUID.randomUUID().toString(), "Meeting", "Meeting with customer, Microram inc.", "Bluepumpkin Headquarters", getDateEvent(2014,11,3, 10,15), "description");
		eventService.save(eventNine);
		
//		Past Sports Events
		Event sportsEvent = new Event(UUID.randomUUID().toString(), "Sports Event", "Baseball", "Company's outside premises", getDateEvent(2014,9,15, 9,00), "");
		eventService.save(sportsEvent);
		Event sportsEventWithScore = new Event(UUID.randomUUID().toString(), "Sports Event", "Football", "Near Football Stadium", getDateEvent(2014,10,8, 10,05), "");
		eventService.save(sportsEventWithScore);
		
//		Participants for 'user'
//		Participant participant = new Participant(UUID.randomUUID().toString(), null, employee, event);
//		participantService.save(participant);
		Participant participant = new Participant(UUID.randomUUID().toString(), "Denied", employee, eventOne);
		participantService.save(participant);
		participant = new Participant(UUID.randomUUID().toString(), "Approved", employee, eventTwo);
		participantService.save(participant);
		participant = new Participant(UUID.randomUUID().toString(), "Waiting", employee, eventThree);
		participantService.save(participant);
//		Past event
//		participant = new Participant(UUID.randomUUID().toString(), "Waiting", employee, sportsEventWithScore);
//		participantService.save(participant);
		
//		Participants for 'user1'
		participant = new Participant(UUID.randomUUID().toString(), "Approved", employee1, event);
		participantService.save(participant);
		participant = new Participant(UUID.randomUUID().toString(), "Approved", employee1, eventTwo);
		participantService.save(participant);
		participant = new Participant(UUID.randomUUID().toString(), "Waiting", employee1, eventSeven);
		participantService.save(participant);
		
//		Teams for Past Sports Event with score
//		List<Employee> members1 = Arrays.asList(employee1, employee2, employee3, employee4, employee5);
//		List<Employee> members2 = Arrays.asList(employee6, employee7, employee8, employee9, employee10);
		Team team1 = new Team(UUID.randomUUID().toString(), 14, sportsEventWithScore);
		Team team2 = new Team(UUID.randomUUID().toString(), 6, sportsEventWithScore);
		employeeService.createTeam(team1);
		employeeService.createTeam(team2);
		employee1.setTeamList(Arrays.asList(team1)); employeeService.save(employee1);
		employee2.setTeamList(Arrays.asList(team1)); employeeService.save(employee2);
		employee3.setTeamList(Arrays.asList(team1)); employeeService.save(employee3);
		employee4.setTeamList(Arrays.asList(team1)); employeeService.save(employee4);
		employee5.setTeamList(Arrays.asList(team1)); employeeService.save(employee5);
		employee6.setTeamList(Arrays.asList(team2)); employeeService.save(employee6);
		employee7.setTeamList(Arrays.asList(team2)); employeeService.save(employee7);
		employee8.setTeamList(Arrays.asList(team2)); employeeService.save(employee8);
		employee9.setTeamList(Arrays.asList(team2)); employeeService.save(employee9);
		employee10.setTeamList(Arrays.asList(team2)); employeeService.save(employee10);
//		sportsEventWithScore.setTeamList(Arrays.asList(team1, team2));
//		eventService.save(sportsEventWithScore);
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
