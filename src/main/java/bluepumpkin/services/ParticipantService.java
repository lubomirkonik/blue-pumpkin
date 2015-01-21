package bluepumpkin.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bluepumpkin.domain.Participant;
import bluepumpkin.repository.ParticipantRepository;

@Service
public class ParticipantService {

	private final ParticipantRepository participantRepository;
	
	@Autowired
	public ParticipantService(final ParticipantRepository participantRepository) {
		this.participantRepository = participantRepository;
	}
	
	public Participant save(Participant participant) {
		participantRepository.save(participant);
		return participant;
	}
	
	public Participant findOne(String id) {
		return participantRepository.findOne(id);
	}
	
	public void delete(String id) {
		participantRepository.delete(id);
	}
	
}
