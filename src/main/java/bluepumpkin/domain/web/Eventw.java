package bluepumpkin.domain.web;

import java.util.Date;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

public class Eventw {
	
private static final String NOT_BLANK_MESSAGE = "The value may not be empty!";
	
    private String id;
    
    private String type;
    
    @NotNull(message = Eventw.NOT_BLANK_MESSAGE)
    @NotEmpty(message = Eventw.NOT_BLANK_MESSAGE)
    private String name;
    
    @NotNull(message = Eventw.NOT_BLANK_MESSAGE)
    @NotEmpty(message = Eventw.NOT_BLANK_MESSAGE)
    private String place;
    
//    @Temporal(TemporalType.TIMESTAMP)
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    @Future(message = "The value \"${formatter.format('%1$td.%1$tm.%1$tY', validatedValue)}\" is not in future!")
    private Date dateTime;

	public Eventw() {
	}

	public Eventw(String id, String type, String name, String place,
			Date dateTime) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.place = place;
		this.dateTime = dateTime;
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
    
    
}
