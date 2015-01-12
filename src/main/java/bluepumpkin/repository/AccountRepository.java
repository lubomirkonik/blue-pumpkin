package bluepumpkin.repository;

import org.springframework.data.repository.CrudRepository;

import bluepumpkin.domain.Account;

public interface AccountRepository extends CrudRepository<Account, Long> {

		Account findByEmail(String email);
}
