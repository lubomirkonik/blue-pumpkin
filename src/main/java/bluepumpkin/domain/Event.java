package bluepumpkin.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Lubosh
 */
@Entity(name = "event")
public class Event {
//    private static final long serialVersionUID = 1L;
	
//	private static final String NOT_BLANK_MESSAGE = "The value may not be empty!";
	
    @Id
    @Column(name = "ID")
    private String id;
    
    private String type;
    
//    @NotNull(message = Event.NOT_BLANK_MESSAGE)
//    @NotEmpty(message = Event.NOT_BLANK_MESSAGE)
    private String name;
    
//    @NotNull(message = Event.NOT_BLANK_MESSAGE)
//    @NotEmpty(message = Event.NOT_BLANK_MESSAGE)
    private String place;
    
//    @Temporal(TemporalType.TIMESTAMP)
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
//    @Future(message = "The value \"${formatter.format('%1$td.%1$tm.%1$tY', validatedValue)}\" is not in future!")
    private Date dateTime;

    private String description;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventID")
    private List<Team> teamList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventID")
    private List<Participant> participantList;

    public Event() {
    }

    public Event(String id) {
        this.id = id;
    }
    
    public Event(String id, String type, String name, String place,
			Date dateTime, String description 
//			List<Team> teamList, List<Participant> participantList
			) {
		super();
		this.id = id;
		this.type = type;
		this.name = name;
		this.place = place;
		this.dateTime = dateTime;
		this.description = description;
//		this.teamList = teamList;
//		this.participantList = participantList;
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

//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (id != null ? id.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof Event)) {
//            return false;
//        }
//        Event other = (Event) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }

    @Override
    public String toString() {
        return "bluepumpkin.domain.Event[ id=" + id + " ]";
    }
    
}

