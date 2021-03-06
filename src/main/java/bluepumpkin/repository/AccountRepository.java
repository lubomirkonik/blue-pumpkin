package bluepumpkin.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bluepumpkin.domain.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

		Account findByEmail(String email);
}
