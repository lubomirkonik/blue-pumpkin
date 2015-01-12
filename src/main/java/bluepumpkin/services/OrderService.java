package bluepumpkin.services;

import java.util.List;
import java.util.UUID;

import bluepumpkin.domain.Order;
import bluepumpkin.domain.OrderStatus;

public interface OrderService {

  public List<Order> requestAllOrders();

  public Order requestOrder(UUID key);

  public OrderStatus requestOrderStatus(String key);  //UUID
  
  public OrderStatus requestOrderStatusByOrderId(String key);  //UUID

  public Order createOrder(Order order);

  public OrderStatus setOrderStatus(OrderStatus orderStatus);

//  public Order setOrderPayment(PaymentDetails paymentDetails);

  public Order deleteOrder(String key);  //UUID
  
  
  public void setOrder(Order order);
  
  public void deleteOrderStatus(String key);

}
