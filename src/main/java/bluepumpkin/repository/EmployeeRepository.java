package bluepumpkin.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bluepumpkin.domain.Employee;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

		Employee findByEmail(String email);
}
