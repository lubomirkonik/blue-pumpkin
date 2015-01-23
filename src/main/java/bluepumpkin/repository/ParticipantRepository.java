package bluepumpkin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bluepumpkin.domain.Participant;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, String> {
	
	List<Participant> findByStatus(String status);
//	List<Participant> findByEventId(String id);
}
