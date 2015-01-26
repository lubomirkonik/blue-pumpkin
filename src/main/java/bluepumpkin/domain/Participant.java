package bluepumpkin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
//		super();
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

    @Override
    public String toString() {
        return "bluepumpkin.domain.Participant[ id=" + id + " ]";
    }
    
}

