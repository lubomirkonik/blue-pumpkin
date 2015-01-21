package bluepumpkin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bluepumpkin.domain.Participant;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, String> {

}
