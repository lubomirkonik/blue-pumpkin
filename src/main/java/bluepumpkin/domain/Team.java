package bluepumpkin.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

/**
 *
 * @author Lubosh
 */
@Entity(name = "team")
public class Team {
	
    @Id
    @Column(name = "ID")
    private String id;
    
    @Column(name = "Score")
    private Integer score;
    
    @ManyToMany(mappedBy = "teamList")
    private List<Employee> employeeList;
    
    @JoinColumn(name = "EventID", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
    private Event eventID;

    public Team() {
    }

    public Team(String id) {
        this.id = id;
    }

    public Team(String id, Integer score, List<Employee> employeeList,
			Event eventID) {
		this.id = id;
		this.score = score;
		this.employeeList = employeeList;
		this.eventID = eventID;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public Event getEventID() {
        return eventID;
    }

    public void setEventID(Event eventID) {
        this.eventID = eventID;
    }

    @Override
    public String toString() {
        return "bluepumpkin.domain.Team[ id=" + id + " ]";
    }
    
}

