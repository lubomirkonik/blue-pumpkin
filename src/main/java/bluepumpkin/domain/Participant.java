package bluepumpkin.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Lubosh
 */
@Entity(name = "participant")
public class Participant {

    @Id
    @Column(name = "ID")
    private String id;
    
    @Column(name = "Status")
    private String status;
    
    @JoinColumn(name = "EmployeeID", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
//    TODO remove ID from the name of the field
    private Employee employeeID;
    
    @JoinColumn(name = "EventID", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
    private Event eventID;

    public Participant() {
    }

    public Participant(String id) {
        this.id = id;
    }
    
    public Participant(String id, String status, Employee employeeID,
			Event eventID) {
		super();
		this.id = id;
		this.status = status;
		this.employeeID = employeeID;
		this.eventID = eventID;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Employee getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Employee employeeID) {
        this.employeeID = employeeID;
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
//        if (!(object instanceof Participant)) {
//            return false;
//        }
//        Participant other = (Participant) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }

    @Override
    public String toString() {
        return "bluepumpkin.domain.Participant[ id=" + id + " ]";
    }
    
}

