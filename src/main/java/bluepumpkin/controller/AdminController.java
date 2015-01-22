package bluepumpkin.controller;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import bluepumpkin.services.AdminService;
import bluepumpkin.support.web.MessageHelper;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);
	
	private AdminService adminService;
	
	@Autowired
	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}
	
//	@PostConstruct
//	private void init() {
//
//	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String adminHome(Model model) {
		model.addAttribute("navigation", "adminPages");
//		TODO model.addAttribute("birthdays", );
//		TODO model.addAttribute("sportsEvent", );
		LOG.debug("Waiting participation requests to admin home view");
//		TODO _assure_ list is reversed in order to latest request is first
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
	
//	TODO add event
	
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
