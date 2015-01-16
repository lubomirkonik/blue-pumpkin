//package bluepumpkin.domain;
//
//import org.hibernate.validator.constraints.*;
//
//import bluepumpkin.domain.Account;
//
////Use AccountEmployeeWeb class instead
//public class SignupForm {
//
//	private static final String NOT_BLANK_MESSAGE = "The value may not be empty!"; //{notBlank.message}
//	private static final String EMAIL_MESSAGE = "The value must be a valid email!"; //{email.message}
//
//    @NotBlank(message = SignupForm.NOT_BLANK_MESSAGE)
//	@Email(message = SignupForm.EMAIL_MESSAGE)
//	private String email;
//
//    @NotBlank(message = SignupForm.NOT_BLANK_MESSAGE)
//	private String password;
//
//    public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public Account createAccount() {
//        return new Account(getEmail(), getPassword(), "ROLE_USER");
//	}
//}
