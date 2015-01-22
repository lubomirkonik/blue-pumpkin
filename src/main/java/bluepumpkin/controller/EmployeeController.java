package bluepumpkin.controller;

import java.math.BigDecimal;
import java.security.Principal;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import bluepumpkin.domain.Account;
import bluepumpkin.domain.Employee;
import bluepumpkin.domain.Event;
import bluepumpkin.domain.Participant;
import bluepumpkin.services.AccountService;
import bluepumpkin.services.EmployeeService;
import bluepumpkin.services.EventService;
import bluepumpkin.services.ParticipantService;
import bluepumpkin.services.UserService;
import bluepumpkin.support.web.MessageHelper;

@Controller
public class EmployeeController {

	private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);
	
	private UserService userService;
	
	private AccountService accountService;
	private EmployeeService employeeService;
	private EventService eventService;
	private ParticipantService participantService;
	
//	private Employee employee;
	
	@Autowired
	public EmployeeController(AccountService accountService, EmployeeService employeeService,
			EventService eventService, ParticipantService participantService,
			UserService userService) {
		this.accountService = accountService;
		this.employeeService = employeeService;
		this.eventService = eventService;
		this.participantService = participantService;
		this.userService = userService;
	}
	
	@PostConstruct
	private void init() {
//		userService.signin(new Account("user", "user", Account.ROLE_USER));
		
//		Employee for default account "user"
		Account account = accountService.findByEmail("user");
		Employee employee = employee(account.getId(), "firstName", "lastName", "position", "department", "telephone", account.getEmail(), getDate(1980, 1, 20));
		employee = employeeService.save(employee);
		
//		New account and its employee
		account = new Account("user@bluepumpkin.com", "user", Account.ROLE_USER);
		account = accountService.save(account);
		Employee employeeOne = employee(account.getId(), "firstName1", "lastName1", "position1", "department1", "telephone1", account.getEmail(), getDate(1970, 1, 20));
		employeeService.save(employeeOne);
		
//		Events
		Event event = new Event(UUID.randomUUID().toString(), "Sports Event", "Football", "Near Football Stadium", getDate(2015, 3, 20),"description");
		eventService.save(event);
		Event eventOne = new Event(UUID.randomUUID().toString(), "Sports Event", "Football1", "Near Football Stadium1", getDate(2015, 3, 23), "description1");
		eventService.save(eventOne);
		Event eventTwo = new Event(UUID.randomUUID().toString(), "Sports Event", "Football2", "Near Football Stadium2", getDate(2015,3,21), "description2");
		eventService.save(eventTwo);
		Event eventThree = new Event(UUID.randomUUID().toString(), "Meeting", "Annual Meeting", "Manager Office", getDate(2015,3,26), "description3");
		eventService.save(eventThree);
		Event eventFour = new Event(UUID.randomUUID().toString(), "Meeting", "Annual Meeting1", "Manager Office1", getDate(2015,3,25), "description4");
		eventService.save(eventFour);
		
//		Participants
		Participant participant = new Participant(UUID.randomUUID().toString(), null, employee, event);
		participantService.save(participant);
		
		participant = new Participant(UUID.randomUUID().toString(), "Waiting", employee, eventOne);
		participantService.save(participant);
//		participant = new Participant(UUID.randomUUID().toString(), "Denied", employee, eventTwo);
		participant = new Participant(UUID.randomUUID().toString(), "Approved", employee, eventTwo);
		participantService.save(participant);
		
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
	
	private Employee getEmployee(Principal principal) {
		Assert.notNull(principal);
		return employeeService.findByEmail(principal.getName());
	}
	
	private Date getDate(int year, int month, int day) {
		Calendar date = Calendar.getInstance();
		date.set(year, month, day);
		return date.getTime();
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String employeeHome(Model model, Principal principal) {
		if (accountService.findByEmail(principal.getName()).isAdmin()) {
			return "redirect:/admin";
		}
		Employee employee = getEmployee(principal);
		model.addAttribute("navigation", "pages");
//		TODO model.addAttribute("birthdays", );
//		TODO model.addAttribute("sportsEvent", );
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
		if (page == "home")
			return "redirect:/";
		else if (page == "upcomingEvents")
			return "redirect:/upcomingEvents";
		return "redirect:/";
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
}
