package bluepumpkin.repository;

//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bluepumpkin.domain.Event;

@Repository								//JpaRepository<Event, String>
public interface EventRepository extends CrudRepository<Event, String> {

}
