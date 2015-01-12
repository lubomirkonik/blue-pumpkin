package bluepumpkin.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import bluepumpkin.domain.Account;
import bluepumpkin.services.AccountService;
import bluepumpkin.services.UserService;
import bluepumpkin.support.web.*;
import bluepumpkin.domain.SignupForm;

@Controller
public class SignupController {

    private static final String SIGNUP_VIEW_NAME = "signup/signup";

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "signup")
	public String signup(Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		model.addAttribute(new SignupForm());
        if (AjaxUtils.isAjaxRequest(requestedWith)) {
            return SIGNUP_VIEW_NAME.concat(" :: signupForm");
        }
        return SIGNUP_VIEW_NAME;
	}
	
	@RequestMapping(value = "signup", method = RequestMethod.POST)	
	public String signup(@Valid @ModelAttribute SignupForm signupForm, Errors errors, RedirectAttributes ra) {
		if (!checkEmailNotUsed(signupForm, errors) || errors.hasErrors()) {
			return SIGNUP_VIEW_NAME;
		}
		
		Account account = accountService.save(signupForm.createAccount());
		
		userService.signin(account);
        MessageHelper.addSuccessAttribute(ra, "Congratulations! You have successfully signed up."); //"signup.success"
		return "redirect:/";
	}
	
	private boolean checkEmailNotUsed(SignupForm signupForm, Errors errors) {
		if (accountService.findByEmail(signupForm.getEmail()) != null) {
			errors.rejectValue("email", "duplicate", "This email is already used!");
			return false;
		}
		return true;
	}
	
}
