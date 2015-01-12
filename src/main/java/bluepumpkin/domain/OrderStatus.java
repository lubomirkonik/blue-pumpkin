package bluepumpkin.domain;

//import tut.webdata.events.orders.OrderStatusDetails;
//import org.springframework.data.annotation.Id;

import javax.persistence.*;

//import java.io.Serializable;
import java.util.Date;

//import javax.persistence.Column;

@Entity(name = "ORDER_STATUS")
public class OrderStatus { // implements Serializable

//  @Column(name = "ORDER_ID")
  private String orderId;
  
  @Id
//  @Column(name = "STATUS_ID")
  private String id;
  
  private Date statusDate;
  private String status;
  
  @Transient
  private String name;
  public String getName() {
	  return name;
  }
  public void setName(String name) {
	  this.name = name;
  }

  public OrderStatus(String orderId, String id, final Date date, final String status) {
    this.orderId = orderId;
    this.id = id;
    this.status = status;
    this.statusDate = date;
  }

  public OrderStatus() {

  }
  
  public Date getStatusDate() {
    return statusDate;
  }

  public String getStatus() {
    return status;
  }

  public String getOrderId() {
    return orderId;
  }

  public String getId() {
    return id;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setStatusDate(Date statusDate) {
    this.statusDate = statusDate;
  }

  public void setStatus(String status) {
    this.status = status;
  }

//  public OrderStatusDetails toStatusDetails() {
//    return new OrderStatusDetails(orderId, id, statusDate, status);
//  }
//
//  public static OrderStatus fromStatusDetails(OrderStatusDetails orderStatusDetails) {
//    return new OrderStatus(
//        orderStatusDetails.getOrderId(), orderStatusDetails.getId(),
//        orderStatusDetails.getStatusDate(), orderStatusDetails.getStatus());
//  }
  
  private boolean entityFound = true;
  public static OrderStatus notFound(String key) {
	  OrderStatus ev = new OrderStatus();
	  ev.id = key;
	  ev.entityFound=false;
	  return ev;
  }
}
