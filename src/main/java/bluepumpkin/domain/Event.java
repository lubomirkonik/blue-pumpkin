package bluepumpkin.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Lubosh
 */
@Entity(name = "event")
public class Event {

    @Id
    @Column(name = "ID")
    private String id;
    
    private String type;
    private String name;
    private String place;
//    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;
    private String description;
    
    @OneToMany(mappedBy = "eventID") //orphanRemoval = true
    private List<Team> teamList;
    
    @OneToMany(mappedBy = "eventID") //orphanRemoval = true - when updating Event-collection with cascade=”all-delete-orphan” was no longer referenced by the owning entity instance
    private List<Participant> participantList;

    public Event() {
    }

    public Event(String id) {
        this.id = id;
    }
    
    public Event(String id, String type, String name, String place,
			Date dateTime, String description) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.place = place;
		this.dateTime = dateTime;
		this.description = description;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Team> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<Team> teamList) {
        this.teamList = teamList;
    }

    public List<Participant> getParticipantList() {
        return participantList;
    }

    public void setParticipantList(List<Participant> participantList) {
        this.participantList = participantList;
    }

    @Override
    public String toString() {
        return "bluepumpkin.domain.Event[ id=" + id + " ]";
    }
    
}

