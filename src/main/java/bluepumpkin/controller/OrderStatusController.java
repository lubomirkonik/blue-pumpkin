package bluepumpkin.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import bluepumpkin.domain.Basket;
import bluepumpkin.domain.Order;
import bluepumpkin.domain.OrderStatus;
import bluepumpkin.services.OrderService;

@Controller
@RequestMapping("/order/{orderId}")
public class OrderStatusController {

	private static final Logger LOG = LoggerFactory
			.getLogger(OrderStatusController.class);

	@Autowired
	private Basket basket;
	
	@Autowired
	private OrderService orderService;

	@RequestMapping(method = RequestMethod.GET)			// OrderStatus orderStatus
	public String orderStatus(@ModelAttribute("orderStatus") OrderStatus orderStatus) {
		LOG.info("Get order status for order id {} customer {}", orderStatus.getOrderId(), orderStatus.getName());
		return "/order";
	}

	@ModelAttribute("orderStatus")
	private OrderStatus getOrderStatus(@PathVariable("orderId") String orderId) {
		Order orderDetailsEvent = orderService.requestOrder(UUID.fromString(orderId));
		OrderStatus orderStatusEvent = orderService.requestOrderStatusByOrderId(orderId); //UUID.fromString(orderId)
		
		//orderStatusEvent could be used to set customer's name and to return it as status
		OrderStatus status = new OrderStatus();
		status.setName(orderDetailsEvent.getName());
		status.setOrderId(orderId);  // UUID.fromString(orderId)
		status.setStatus(orderStatusEvent.getStatus());
		
		return status;
	}
	
	//added basket to see its size
	@ModelAttribute("basket")
	public Basket getBasket() {
		return basket;
	}
}
