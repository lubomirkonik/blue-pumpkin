package bluepumpkin.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bluepumpkin.domain.Participant;

@Repository
public interface ParticipantRepository extends CrudRepository<Participant, String> {

}
