package bluepumpkin.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

import java.io.Serializable;

public class CustomerInfo implements Serializable {

  private static final String NOT_BLANK_MESSAGE = "The value may not be empty!"; //{notBlank.message}
	
  @NotNull(message = CustomerInfo.NOT_BLANK_MESSAGE)
  @NotEmpty(message = CustomerInfo.NOT_BLANK_MESSAGE)
  private String name;

  @NotNull(message = CustomerInfo.NOT_BLANK_MESSAGE)
  @NotEmpty(message = CustomerInfo.NOT_BLANK_MESSAGE)
  private String address1;

  @NotNull(message = CustomerInfo.NOT_BLANK_MESSAGE)
  @NotEmpty(message = CustomerInfo.NOT_BLANK_MESSAGE)
  private String postcode;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress1() {
    return address1;
  }

  public void setAddress1(String address1) {
    this.address1 = address1;
  }

  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

}
