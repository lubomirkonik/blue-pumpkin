package bluepumpkin.domain.web;

public class Birthday {

	private String firstName;
    private String lastName;
    private String position;
    private String department;
    private int age;
	
	public Birthday(String firstName, String lastName, String position,
			String department, int age) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.position = position;
		this.department = department;
		this.age = age;
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
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
    
}
