package bluepumpkin.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import bluepumpkin.services.MenuService;
import bluepumpkin.services.OrderService;
import bluepumpkin.support.web.AjaxUtils;
import bluepumpkin.support.web.MessageHelper;
import bluepumpkin.domain.MenuItem;
import bluepumpkin.domain.Order;
import bluepumpkin.domain.OrderStatus;
import bluepumpkin.domain.WebOrder;
import bluepumpkin.domain.WebOrderStatus;

@Controller
public class AdminController {
	
	private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private MenuService menuService;
	
	@PostConstruct
	private void init() {
		menuService.createMenuItem(menuItem("YM1", new BigDecimal("1.99"), 11, "Yummy Noodles"));
		menuService.createMenuItem(menuItem("YM2", new BigDecimal("2.99"), 12, "Special Noodles"));
		menuService.createMenuItem(menuItem("YM3", new BigDecimal("3.59"), 13, "Low Cal Noodles"));
		menuService.createMenuItem(menuItem("YM4", new BigDecimal("4.09"), 5, "Greek Salad"));
		
		orderService.createOrder(createOrder(UUID.randomUUID().toString(), new Date(), createOrderItems("YM4", "YM1"), "Mark Brown", "Greenwitch St. 402/C, London", "4802 35"));
		orderService.createOrder(createOrder(UUID.randomUUID().toString(), new Date(), createOrderItems("YM2", "YM1", "YM3"), "Valter Longo", "Le Marais St. 156/D, Paris", "569 21"));
	}
	
	private Order createOrder(String id, Date dateTimeOfSubmission, Map<String, Integer> orderItems, String name, String address, String postcode) {
		Order order = new Order();
		order.setId(id);
		order.setDateTimeOfSubmission(dateTimeOfSubmission);
		order.setOrderItems(orderItems);
		order.setName(name);
		order.setAddress1(address);
		order.setPostcode(postcode);
		return order;
	}

	private Map<String, Integer> createOrderItems(String... orderIds) {
		Map<String, Integer> orderItems = new HashMap<String, Integer>();
		for (String orderId : orderIds) {
			orderItems.put(orderId, 1);
		}
		return orderItems;
	}
	
	private MenuItem menuItem(String id, BigDecimal cost, int minutesToPrepare, String name) {
		MenuItem item = new MenuItem();
		item.setId(id);
		item.setCost(cost);
		item.setMinutesToPrepare(minutesToPrepare);
		item.setName(name);
		return item;
	}
	
	private static boolean visited = false;
						  //{"/","orders"}
	@RequestMapping(value = "orders", method = RequestMethod.GET)
	public String getOrders(Model model) {
		if (!visited) {
			init();
			visited = true;
		}
		
		List<Order> orders = new ArrayList<>();
		orders = orderService.requestAllOrders();
		
		List<WebOrder> webOrders = new ArrayList<>();
		for (int i = 0; i < orders.size(); i++) {
			webOrders.add(new WebOrder());
			BeanUtils.copyProperties(orders.get(i), webOrders.get(i));
		}
		
		for (WebOrder order : webOrders) {
			// get current status of order
			OrderStatus orderStatus = orderService.requestOrderStatusByOrderId(order.getId());
			order.setStatus(orderStatus.getStatus());
			
			// get all menu items objects of order by menu item ids as keys of Map orderItems
			Map<String, Integer> orderItems = order.getOrderItems();
			List<MenuItem> menuItems = new ArrayList<>();
			for (String itemId : orderItems.keySet()) {
				menuItems.add(menuService.requestMenuItem(itemId));
			}
			
			// get order items
			order.initMenuAndOrderItems(menuItems);
			// get total costs of order items
			// get total cost of order
			order.calculateTotalCost();
		}
		
		model.addAttribute("orders", webOrders);
		return "admin/orders";
	}
	
	@RequestMapping(value = "orders/{orderId}/edit", method = RequestMethod.GET)
															 // @ModelAttribute("updateOrderForm") WebOrder order
	public String initUpdateOrderForm(@PathVariable("orderId") String orderId, Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {

		//	needs to get path variable 'orderId' from orders page
		if (AjaxUtils.isAjaxRequest(requestedWith)) {
			getOrderAndPutIntoModel(orderId, model);
            return "admin/updateOrder".concat(" :: updateOrderForm");
        }
		
		getOrderAndPutIntoModel(orderId, model);
        return "admin/updateOrder";
	}
	
	private void getOrderAndPutIntoModel(String orderId, Model model) {
		Order order = orderService.requestOrder(UUID.fromString(orderId));
		
		WebOrder webOrder = new WebOrder();
		BeanUtils.copyProperties(order, webOrder);
		
		// get all menu items
		List<MenuItem> menuItems = menuService.requestAllMenuItems();
		// get menu and order items
		webOrder.initMenuAndOrderItems(menuItems);
		// get total costs of order items
		// get total cost of order
		webOrder.calculateTotalCost();
		
		// get current status and other statuses
		WebOrderStatus currentStatus = new WebOrderStatus(orderService.requestOrderStatusByOrderId(order.getId()).getStatus(),true);
		List<WebOrderStatus> statuses = getAllStatuses();
		List<WebOrderStatus> orderStatuses = new ArrayList<>();
		orderStatuses.add(currentStatus);
		for (WebOrderStatus status : statuses) {
			if (!status.getStatus().equals(currentStatus.getStatus())) {
				orderStatuses.add(status);
			}
		}
		
		webOrder.setOrderStatuses(orderStatuses);
		
		model.addAttribute("updateOrderForm", webOrder);
	}
	
	private List<WebOrderStatus> getAllStatuses() {
		List<WebOrderStatus> statuses = new ArrayList<>();
		statuses.add(new WebOrderStatus("Order Received", false));
		statuses.add(new WebOrderStatus("Order Processing", false));
		statuses.add(new WebOrderStatus("Order Processed", false));
		statuses.add(new WebOrderStatus("Order Completed", false));
		statuses.add(new WebOrderStatus("Order Cancelled", false));
		return statuses;
	}
	
	@RequestMapping(value = "orders/{orderId}/edit", method = RequestMethod.POST)
	public String updateUpdateOrderFormDetails(@Valid @ModelAttribute("updateOrderForm") WebOrder webOrder, Errors errors) {
		
		webOrder.checkItemsNotChosenAndTotalCostsNull();
		
		if (!webOrder.checkItemsChosenAndQuantityGreaterThan0()) {
			// add field error to quantity that can't be less than 1
			errors.reject("error");
		}
		
		webOrder.calculateTotalCost();
		
		return "admin/updateOrder";
	}
	
	@RequestMapping(value = "orders/{orderId}/update", method = RequestMethod.POST)
	public String processUpdateOrderForm(@PathVariable("orderId") String orderId, @Valid @ModelAttribute("updateOrderForm") WebOrder webOrder, Errors errors, RedirectAttributes redirectAttrs) {
		boolean chosenItemHasQuantity = webOrder.checkItemsChosenAndQuantityGreaterThan0();
		if (errors.hasErrors() || !chosenItemHasQuantity) { 
			if (!chosenItemHasQuantity) {  // !notChosenItemHasTotalCostNull || !chosenItemHasQuantity
//				errors.rejectValue("menuAndOrderItems0.quantity", "Quantity may not be less than 1!");
				errors.reject("error");	
			}
			webOrder.checkItemsNotChosenAndTotalCostsNull();
			webOrder.calculateTotalCost();
			return "admin/updateOrder";
		}
		
		// needed property for date/time of order update - updated details except status
		
		webOrder.updateOrderItems(webOrder.getMenuAndOrderItems());
		
		Order order = orderService.requestOrder(UUID.fromString(orderId));
		order.setOrderItems(webOrder.getOrderItems());
		order.setName(webOrder.getName());
		order.setAddress1(webOrder.getAddress1());
		order.setPostcode(webOrder.getPostcode());
		
		orderService.setOrder(order);
		
		String message = "has been updated!";
		
		// order status before order/order-status update
		OrderStatus orderStatus = orderService.requestOrderStatusByOrderId(orderId);
		// if order status has been modified, update order status
		if (!orderStatus.getStatus().equals(webOrder.getStatus())) {
			orderStatus.setStatusDate(new Date());
			orderStatus.setStatus(webOrder.getStatus());
			orderService.setOrderStatus(orderStatus);
			message = "Order Status ".concat(message);
		} else {
			message = "Order ".concat(message);
		}
		// if both were updated set message: Order and Order Status have been updated!
		
		MessageHelper.addSuccessAttribute(redirectAttrs, message);
		
		return "redirect:/orders";
	}
	
	@RequestMapping(value = "orders/{orderId}/delete", method = RequestMethod.GET)
	public String deleteOrderAndOrderStatus(@PathVariable("orderId") String orderId, RedirectAttributes redirectAttrs) {
		orderService.deleteOrderStatus(orderId);
		orderService.deleteOrder(orderId);
		
		MessageHelper.addSuccessAttribute(redirectAttrs, "Order has been deleted!");
		
		return "redirect:/orders";
	}
	
	private List<MenuItem> menuItems;

	@ModelAttribute("addMenuItemForm")
	private MenuItem getMenuItem() {
		return new MenuItem();
	}

	@RequestMapping(value = "menuItems", method = RequestMethod.GET)
	public String getMenuItems(Model model) {
		LOG.debug("All menu items to menuItems view");
		model.addAttribute("menuItems", menuService.requestAllMenuItems());
		return "admin/menuItems";
	}
	
	@RequestMapping(value = "addMenuItem")
	public String initAddMenuItemForm(@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		if (AjaxUtils.isAjaxRequest(requestedWith)) {
            return "admin/addMenuItem".concat(" :: addMenuItemForm");
        }
        return "admin/addMenuItem";
    }
	
	@RequestMapping(value = "addMenuItem", method = RequestMethod.POST)
	public String processAddMenuItemForm(@Valid @ModelAttribute("addMenuItemForm") MenuItem menuItem, Errors errors, RedirectAttributes redirectAttrs) {  //BindingResult result
		if (errors.hasErrors() | !checkMenuItemIdNotExisting(menuItem, errors) | !checkMenuItemCostNotNull(menuItem, errors)) {
			return "admin/addMenuItem";
		}
		LOG.debug("No errors, continue with creating of menu item {}:", menuItem.getName());
		menuService.createMenuItem(menuItem);
		// redirectAttrs.addFlashAttribute("message","Menu item has been created!");
		MessageHelper.addSuccessAttribute(redirectAttrs, "Menu item has been created!");	// "addMenuItem.success"
		return "redirect:/menuItems";
	}
	
	private boolean checkMenuItemIdNotExisting(MenuItem menuItem, Errors errors) {
		List<MenuItem> menuItems = new ArrayList<>(menuService.requestAllMenuItems());
		for (MenuItem item : menuItems) {
			if (item.getId().equals(menuItem.getId())) {
				// log
				errors.rejectValue("id", "duplicate", "The value already exists!");
				return false;	
			}
		}
		return true;
	}
	
	private boolean checkMenuItemCostNotNull(MenuItem menuItem, Errors errors) {
		if (menuItem.getCost() == null) {
			errors.rejectValue("cost", "type", "The value is not a number!");
			return false;
		}
		return true;
	}
		
//	show error for corresponding field
	@RequestMapping(value = "updateMenuItem", method = RequestMethod.POST)
	public String processUpdateMenuItemForm(@Valid @ModelAttribute MenuItem menuItem, Errors errors, RedirectAttributes redirectAttrs) {
		if (menuItem.getCost() == null) {
			menuItem.setCost(BigDecimal.ZERO);
		}
		
		int index = 0;
		if (errors.hasErrors()) {	// || menuItem.getCost().equals(BigDecimal.ZERO)
			menuItems = menuService.requestAllMenuItems();
			for (MenuItem item : menuItems) {
				if (item.getId().equals(menuItem.getId())) {
					index = menuItems.indexOf(item);
					menuItems.remove(item);
					break;
				}
			}
			menuItems.add(index, menuItem);
			
			MessageHelper.addErrorAttribute(redirectAttrs, "Form contains errors! Please try again.");  // "updateMenuItem.error" 
			return "redirect:/errorInMenuItems";
		}
		LOG.debug("Update {} on MongoDB", menuItem.getId());
		menuService.updateMenuItem(menuItem);
		MessageHelper.addSuccessAttribute(redirectAttrs, "Menu item has been updated!");
		return "redirect:/menuItems";
	}
	
	@RequestMapping(value = "errorInMenuItems", method = RequestMethod.GET)
	public String getMenuItemsWithError(Model model) {
		model.addAttribute("menuItems", menuItems);
		return "admin/menuItems";
	}

	@RequestMapping(value = "deleteMenuItem", method = RequestMethod.POST)
	public String processDeleteMenuItemForm(@ModelAttribute MenuItem menuItem, RedirectAttributes redirectAttrs) {
		List<Order> orders = orderService.requestAllOrders();
		for (Order order : orders) {
			Set<String> orderItemsIds = order.getOrderItems().keySet();
			for (String id : orderItemsIds) {
				if (id.equals(menuItem.getId())) {
					//log
					MessageHelper.addErrorAttribute(redirectAttrs, "Menu item is included in one or more orders!");
					return "redirect:/menuItems";
				}
			}
		}
		LOG.debug("Remove {} from MongoDB", menuItem.getId());
		menuService.deleteMenuItem(menuItem.getId());
		MessageHelper.addSuccessAttribute(redirectAttrs, "Menu item has been deleted!");
		return "redirect:/menuItems";
	}	
}
