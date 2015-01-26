package bluepumpkin.domain.web;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import bluepumpkin.domain.Account;

/**
*
* @author Lubosh
*/
public class AccountEmployee {

   private static final String NOT_BLANK_MESSAGE = "The value may not be empty!";
   private static final String EMAIL_MESSAGE = "The value must be a valid email!";

//   in case of entity update
   private Long id;
   
   @NotBlank(message = AccountEmployee.NOT_BLANK_MESSAGE)
   @Email(message = AccountEmployee.EMAIL_MESSAGE)
   private String email;
   
   @NotBlank(message = AccountEmployee.NOT_BLANK_MESSAGE)
   private String password;
   
   @NotNull(message = AccountEmployee.NOT_BLANK_MESSAGE)
   @NotEmpty(message = AccountEmployee.NOT_BLANK_MESSAGE)
   private String firstName;
   
   @NotNull(message = AccountEmployee.NOT_BLANK_MESSAGE)
   @NotEmpty(message = AccountEmployee.NOT_BLANK_MESSAGE)
   private String lastName;

//   TODO Select-Options input in presentation layer
   @NotNull(message = AccountEmployee.NOT_BLANK_MESSAGE)
   @NotEmpty(message = AccountEmployee.NOT_BLANK_MESSAGE)
   private String position;
   
//   TODO Select-Options input in presentation layer
   @NotNull(message = AccountEmployee.NOT_BLANK_MESSAGE)
   @NotEmpty(message = AccountEmployee.NOT_BLANK_MESSAGE)
   private String department;
   
//   TODO Set pattern for telephone number (eg.+421 900 900 900) in presentation layer
   @NotNull(message = AccountEmployee.NOT_BLANK_MESSAGE)
   @NotEmpty(message = AccountEmployee.NOT_BLANK_MESSAGE)
   private String telephone;
   
//   TODO Set pattern for date input in presentation layer and here format with pattern (01.12.2001)
// 		  (or add here fields for day, month, year and method to set this date field by those fields, on page there will be 3 options inputs)
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   @Past(message = "The value \"${formatter.format('%1$td.%1$tm.%1$tY', validatedValue)}\" is not in past!")
   private Date dateOfBirth;

   public AccountEmployee() {
   }

   public AccountEmployee(Long id) {
       this.id = id;
   }
   
   public Account createAccount() {
       return new Account(getEmail(), getPassword(), "ROLE_USER");
	}

   public Long getId() {
       return id;
   }

   public void setId(Long id) {
       this.id = id;
   }

   public String getFirstName() {
       return firstName;
   }

   public void setFirstName(String firstName) {
       this.firstName = firstName;
   }

   public String getLastName() {
       return lastName;
   }

   public void setLastName(String lastName) {
       this.lastName = lastName;
   }

   public String getPosition() {
       return position;
   }

   public void setPosition(String position) {
       this.position = position;
   }

   public String getDepartment() {
       return department;
   }

   public void setDepartment(String department) {
       this.department = department;
   }

   public String getTelephone() {
       return telephone;
   }

   public void setTelephone(String telephone) {
       this.telephone = telephone;
   }

   public Date getDateOfBirth() {
       return dateOfBirth;
   }

   public void setDateOfBirth(Date dateOfBirth) {
       this.dateOfBirth = dateOfBirth;
   }

   public String getEmail() {
       return email;
   }

   public void setEmail(String email) {
       this.email = email;
   }
   
   public String getPassword() {
		return password;
	}

   public void setPassword(String password) {
		this.password = password;
   }
   
   @Override
   public String toString() {
       return "bluepumpkin.domain.web.AccountEmployee[ id=" + id + " ]";
   }
   
}
