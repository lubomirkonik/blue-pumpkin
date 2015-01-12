package bluepumpkin.domain;

//import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.index.Indexed;
//import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.mongodb.core.mapping.Field;


import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

//@Document(collection = "menu")
@Entity(name = "MENU_ITEMS")
public class MenuItem implements Serializable {
	
	private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";
	private static final String COST_MESSAGE = "{cost.message}";
	private static final String MINUTES_MESSAGE = "{minutes.message}";

  @NotNull(message = MenuItem.NOT_BLANK_MESSAGE)
  @NotEmpty(message = MenuItem.NOT_BLANK_MESSAGE)
  @Id
  private String id;

  @NotNull(message = MenuItem.NOT_BLANK_MESSAGE)
  @NotEmpty(message = MenuItem.NOT_BLANK_MESSAGE)
//  @Field("itemName")
//  @Indexed
  private String name;

  private String description;

  @Transient
  private Set<Ingredient> ingredients;
  
  @DecimalMin(value = "0.99", message = MenuItem.COST_MESSAGE)
  @DecimalMax(value = "99.99", message = MenuItem.COST_MESSAGE)
  private BigDecimal cost;

  @Min(value = 1, message = MenuItem.MINUTES_MESSAGE)
  @Max(value = 60, message = MenuItem.MINUTES_MESSAGE)
  private int minutesToPrepare;

  public String getDescription() {
    return description;
  }

  public Set<Ingredient> getIngredients() {
    return ingredients;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setIngredients(Set<Ingredient> ingredients) {
    this.ingredients = ingredients;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
	  
	  try {
		  cost = cost.setScale(2, RoundingMode.HALF_UP);
		} catch (NullPointerException exception) {
			System.out.println("Null Pointer Exception");
			cost = new BigDecimal("0.00");
		}
	   
    this.cost = cost;
  }

  public int getMinutesToPrepare() {
    return minutesToPrepare;
  }

  public void setMinutesToPrepare(int minutesToPrepare) {
    this.minutesToPrepare = minutesToPrepare;
  }
  
  private boolean entityFound = true;
  public static MenuItem notFound(String key) {
	  MenuItem ev = new MenuItem();
	  ev.id = key;
	  ev.entityFound=false;
	  return ev;
  }
}