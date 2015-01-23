package bluepumpkin.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
		super();
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
//        if (!(object instanceof Team)) {
//            return false;
//        }
//        Team other = (Team) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }

    @Override
    public String toString() {
        return "bluepumpkin.domain.Team[ id=" + id + " ]";
    }
    
}

