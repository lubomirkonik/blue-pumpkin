//package bluepumpkin.config;
//
//import bluepumpkin.repository.*;
//import bluepumpkin.services.AccountService;
//import bluepumpkin.services.EmployeeService;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class PersistenceConfig {
//
//	@Bean
//	public AccountService accountPersistenceService(AccountRepository accountRepository) {
//		return new AccountService(accountRepository);
//	}
//	
//	@Bean
//	public EmployeeService employeePersistenceService(EmployeeRepository employeeRepository, ParticipantRepository participantRepository) {
//		return new EmployeeService(employeeRepository, participantRepository);
//	}
//	
////	Others services are annotated by @Service and theirs contructors by @Autowired to inject repositories
//	
//}
