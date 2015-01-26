package bluepumpkin.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 *
 * @author Lubosh
 */
@Entity(name = "employee")
public class Employee {
	
//	The same as Account Id which is generated
//	Manually set it here
    @Id
    @Column(name = "ID")
    private Long id;
    
//    @Column(name = "FirstName")
    private String firstName;
    private String lastName;
    private String position;
    private String department;
    private String telephone;
    private String email;
     
//    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfBirth;
    
    @JoinTable(name = "team_member", joinColumns = {
        @JoinColumn(name = "EmployeeID", referencedColumnName = "ID", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "TeamID", referencedColumnName = "ID", nullable = false)})
    @ManyToMany
    private List<Team> teamList;
    
//    @JoinColumn(name = "Email", referencedColumnName = "Email", nullable = false)
//    @OneToOne(optional = false)
//    private Account email;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeID")
    private List<Participant> participantList;

    public Employee() {
    }

    public Employee(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<Team> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<Team> teamList) {
        this.teamList = teamList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Participant> getParticipantList() {
        return participantList;
    }

    public void setParticipantList(List<Participant> participantList) {
        this.participantList = participantList;
    }

    @Override
    public String toString() {
        return "bluepumpkin.domain.Employee[ id=" + id + " ]";
    }
    
}