package bluepumpkin.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import bluepumpkin.domain.Order;
import bluepumpkin.domain.OrderStatus;
import bluepumpkin.repository.OrderStatusRepository;
import bluepumpkin.repository.OrdersRepository;

public class OrderEventHandler implements OrderService {

	private final OrdersRepository orderRepository;
	private final OrderStatusRepository orderStatusRepository;

	public OrderEventHandler(
			final OrdersRepository orderRepository,
			final OrderStatusRepository orderStatusRepository) {
		this.orderRepository = orderRepository;
		this.orderStatusRepository = orderStatusRepository;
	}
	  
	@Override
	public List<Order> requestAllOrders() {
		List<Order> orders = new ArrayList<Order>();
		for (Order order : orderRepository.findAll()) {
			orders.add(order);
		}
		return orders;
	}

	@Override
	public Order requestOrder(UUID key) {
		Order order = orderRepository.findOne(key.toString());
		if (order == null) {
			return Order.notFound(key.toString());
		}
		return order;
	}

	@Override
	public OrderStatus requestOrderStatus(String key) {  //UUID
		OrderStatus status = orderStatusRepository.findOne(key);
		if (status == null) {
			return OrderStatus.notFound(key);
		}
		return status;
	}
	
	@Override
	public OrderStatus requestOrderStatusByOrderId(String key) {  //UUID
		OrderStatus status = orderStatusRepository.findByOrderId(key);
		if (status == null) {
			return OrderStatus.notFound(key);
		}
		return status;
	}

	@Override
	public Order createOrder(Order order) {
		orderRepository.save(order);
		//(new OrderStatus(UUID.fromString(order.getId()), UUID.randomUUID(), new Date(), "Order Received"))
		setOrderStatus(new OrderStatus(order.getId(), (UUID.randomUUID()).toString(), new Date(), "Order Received"));
		return order;
	}

	@Override
	public OrderStatus setOrderStatus(OrderStatus orderStatus) {
		orderStatusRepository.save(orderStatus);
		return orderStatus;
	}
	
	@Override
	public Order deleteOrder(String key) {
		orderRepository.delete(key);
		return null;
	}
	
	
	@Override
	public void setOrder(Order order) {
		orderRepository.save(order);
	}
	
	@Override
	public void deleteOrderStatus(String key) {
		OrderStatus orderStatus = requestOrderStatusByOrderId(key);
		orderStatusRepository.delete(orderStatus.getId());
	}
}
