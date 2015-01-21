package bluepumpkin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bluepumpkin.domain.Event;

@Repository								
public interface EventRepository extends JpaRepository<Event, String> {

}
