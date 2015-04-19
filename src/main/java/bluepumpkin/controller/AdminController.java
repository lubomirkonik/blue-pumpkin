package bluepumpkin.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import bluepumpkin.domain.web.Eventw;
import bluepumpkin.services.AdminService;
import bluepumpkin.services.EmployeeService;
import bluepumpkin.support.web.MessageHelper;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);
	
	private final AdminService adminService;
	private final EmployeeService employeeService;
	
	@Autowired
	public AdminController(AdminService adminService, EmployeeService employeeService) {
		this.adminService = adminService;
		this.employeeService = employeeService;
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String adminHome(Model model) {
		model.addAttribute("navigation", "adminPages");
		model.addAttribute("birthdays", employeeService.getBirthdays());
		LOG.debug("Waiting participation requests to admin home view");
		model.addAttribute("participations", adminService.getWaitingParticipations());	
		return "admin/home";
	}
	
	@RequestMapping(value = "/participations/{id}/approve", method = RequestMethod.GET)
	public String approveParticipation(@PathVariable String id, RedirectAttributes redirectAttrs) {
		adminService.changeParticipationStatusToApproved(id);
		MessageHelper.addSuccessAttribute(redirectAttrs, "The participation request has been approved!");
		return "redirect:/admin";
	}
	
	@RequestMapping(value = "/participations/{id}/deny", method = RequestMethod.GET)
	public String denyParticipation(@PathVariable String id, RedirectAttributes redirectAttrs) {
		adminService.changeParticipationStatusToDenied(id);
		MessageHelper.addSuccessAttribute(redirectAttrs, "The participation request has been denied!");
		return "redirect:/admin";
	}
	
	@RequestMapping(value = "/upcomingEvents", method = RequestMethod.GET)
	public String getUpcomingEvents(Model model) {
		model.addAttribute("navigation", "adminPages");
		LOG.debug("All upcoming events");
		model.addAttribute("upcomingEvents", adminService.getUpcomingEvents());
		return "admin/upcomingEvents";
	}
	
	@RequestMapping(value = "/addEvent", method = RequestMethod.GET)
	public String initAddEventForm(Model model) {
		model.addAttribute("navigation", "adminPages");
		model.addAttribute("form", "addEvent");
		model.addAttribute("eventForm", new Eventw());
		model.addAttribute("allTypes", adminService.getEventTypes());
		return "admin/eventForm";
	}	
	
//	TODO common model attributes this way
//	@ModelAttribute("allTypes")
//	private List<String> getEventTypes() {
//		return adminService.getEventTypes();
//	}
	
	@RequestMapping(value = "/addEvent", method = RequestMethod.POST)
	public ModelAndView processAddEventForm(@Valid @ModelAttribute("eventForm") Eventw event,
			Errors errors, RedirectAttributes redirectAttrs) {
		if (errors.hasErrors()) {
			ModelAndView mv = new ModelAndView("admin/eventForm");
			mv.addObject("navigation", "adminPages");
			mv.addObject("form", "addEvent");
			mv.addObject("allTypes", adminService.getEventTypes());
			return mv;
		}
		LOG.debug("No errors, continue with creating of event: {}", event.getName());
		adminService.createEvent(event);
		MessageHelper.addSuccessAttribute(redirectAttrs, "Event has been created!");
		ModelAndView mv = new ModelAndView("redirect:/admin/upcomingEvents");
		return mv;
	}
	
	@RequestMapping(value = "/updateEvent/{eventId}", method = RequestMethod.GET)
	public String initUpdateEventForm(@PathVariable String eventId, Model model) {
		model.addAttribute("navigation", "adminPages");
		model.addAttribute("form", "updateEvent");
		model.addAttribute("allTypes", adminService.getEventTypes());
		model.addAttribute("eventForm", adminService.findEvent(eventId));
		return "admin/eventForm";
	}
	
	@RequestMapping(value = "/updateEvent", method = RequestMethod.POST)
	public ModelAndView processUpdateEventForm(@Valid @ModelAttribute("eventForm") Eventw event,
			Errors errors, RedirectAttributes redirectAttrs) {
		if (errors.hasErrors()) {
			ModelAndView mv = new ModelAndView("admin/eventForm");
			mv.addObject("navigation", "adminPages");
			mv.addObject("form", "updateEvent");
			mv.addObject("allTypes", adminService.getEventTypes());
			return mv;
		}
		LOG.debug("No errors, continue with updating of event: {}", event.getName());
		adminService.updateEvent(event);
		MessageHelper.addSuccessAttribute(redirectAttrs, "Event has been updated!");
		ModelAndView mv = new ModelAndView("redirect:/admin/upcomingEvents");
		return mv;
	}
	
	@RequestMapping(value = "/deleteEvent/{id}", method = RequestMethod.GET)
	public String deleteEvent(@PathVariable String id, @RequestParam("page") String page, RedirectAttributes redirectAttrs) {
		adminService.deleteEvent(id);
		MessageHelper.addSuccessAttribute(redirectAttrs, "Event has been deleted!");
		if (page.equals("upcomingEvents"))
			 return "redirect:/admin/upcomingEvents";
		else return "redirect:/admin/pastEvents";
	}
	
	@RequestMapping(value = "/pastEvents", method = RequestMethod.GET)
	public String getPastEvents(Model model) {
		model.addAttribute("navigation", "adminPages");
		LOG.debug("All past events");
		model.addAttribute("pastEvents", adminService.getPastEvents());
		return "admin/pastEvents";
	}	
	
	@RequestMapping(value = "/accounts", method = RequestMethod.GET)
	public String getAccounts(Model model) {
		model.addAttribute("navigation", "adminPages");
		LOG.debug("All accounts");
		model.addAttribute("accounts", adminService.getAccounts());
		return "admin/accounts";
	}
	
}
