package bluepumpkin.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.UUID;

import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;


public class WebOrder {
	
	private static final String NOT_BLANK_MESSAGE = "The value may not be empty!"; //{notBlank.message}
	
	private String id;	

	private Date dateTimeOfSubmission;

	private Map<String, Integer> orderItems;
	
  	//put items ids and quantity from update form to orderItems
  	public void updateOrderItems(List<MenuOrderItem> menuAndOrderItems) {
  		Map<String, Integer> orderItems = new HashMap<String, Integer>();
  		for (MenuOrderItem item : menuAndOrderItems) {
  			if (item.isChosen()) {
  				orderItems.put(item.getId(), item.getQuantity());
  			}
  		}
  		this.orderItems = orderItems;
  	}
  
  	private List<MenuOrderItem> menuAndOrderItems;
  	
  	public List<MenuOrderItem> getMenuAndOrderItems() {
		return menuAndOrderItems;
	}
	public void setMenuAndOrderItems(List<MenuOrderItem> items) {
		for (MenuOrderItem item: items) {
			if (!item.isChosen() && item.getQuantity() != 0) {
				item.setQuantity(0);
			}
		}
		this.menuAndOrderItems = items;
	}
	/**
	 * checks every item, if is chosen and quantity is <= 0 returns false 
	 * @return
	 */
	public boolean checkItemsChosenAndQuantityGreaterThan0() {
		for (MenuOrderItem item : this.menuAndOrderItems) {
			if (item.isChosen() && item.getQuantity() <= 0) {
				return false;
			}
		}
		return true;
	}
	/**
	 * checks every item, if is not chosen and item total cost has some value, set the value to null;
	 * 
	 */
	public void  checkItemsNotChosenAndTotalCostsNull() {
		for (MenuOrderItem item : this.menuAndOrderItems) {
			if (!item.isChosen() && item.getItemTotalCost() != null) {
				item.setItemTotalCost(null);
			}
		}
	}
//	put menuItems into menuAndOrderItems and check every item whether is present in order according to orderItems ids
	public List<MenuOrderItem> initMenuAndOrderItems(List<MenuItem> menuItems) {
		Map<String, Integer> orderItems = this.orderItems;
		Set<String> orderItemsKeys = this.orderItems.keySet();
		
		List<MenuOrderItem> menuAndOrderItems = new ArrayList<>();
		
		for (int i = 0; i < menuItems.size(); i++) {
			MenuItem menuItem = menuItems.get(i);
			
			menuAndOrderItems.add(new MenuOrderItem());
			MenuOrderItem menuOrderItem = menuAndOrderItems.get(i);
			
			menuOrderItem.setId(menuItem.getId());
			menuOrderItem.setName(menuItem.getName());
			menuOrderItem.setCost(menuItem.getCost());
			menuOrderItem.setMinutesToPrepare(menuItem.getMinutesToPrepare());
			menuOrderItem.setQuantity(0);
			menuOrderItem.setChosen(false);
			
//			actual menuItem id in interation is compared with ids of orderItems hashmap, if there is equality,
//			menuOrderItem has property chosen set on true and its property quantity is set according to value of orderItems
			for (String id : orderItemsKeys) {
				if (menuItem.getId().equals(id)) {
					menuOrderItem.setChosen(true);
					menuOrderItem.setQuantity(orderItems.get(id)); //orderItems.get(menuItem.getId())
					break;
				}
			}
		}
		
		this.menuAndOrderItems = menuAndOrderItems;
		return menuAndOrderItems;
	}

	private List<WebOrderStatus> orderStatuses;
	
	public List<WebOrderStatus> getOrderStatuses() {
		return orderStatuses;
	}
	public void setOrderStatuses(List<WebOrderStatus> orderStatuses) {
		this.orderStatuses = orderStatuses;
	}

//current status
  private String status;
  
  public String getStatus() {
	  return status;
  }
  public void setStatus(String status) {
	  this.status = status;
  }
  
  //total cost
  private BigDecimal cost;
  
  public BigDecimal getCost() {
	  return cost;
  }
  // if error occurs in update order form  
  public void setCost(BigDecimal cost) {
	this.cost = cost;
  }
  /**
   * get order items total costs;
   * get order total cost
   * @param menuItems
   */
public void calculateTotalCost() {
	  cost = BigDecimal.ZERO;
	  
	  for (MenuOrderItem item : menuAndOrderItems) {
		  if (item.isChosen()) {
			  item.calculateItemTotalCost();
			  cost = cost.add(item.getItemTotalCost());  
		  }
	  }
  }
  
//  CustomerInfo
  @NotNull(message = WebOrder.NOT_BLANK_MESSAGE)
  @NotEmpty(message = WebOrder.NOT_BLANK_MESSAGE)
  private String name;
  @NotNull(message = WebOrder.NOT_BLANK_MESSAGE)
  @NotEmpty(message = WebOrder.NOT_BLANK_MESSAGE)
  private String address1;
  @NotNull(message = WebOrder.NOT_BLANK_MESSAGE)
  @NotEmpty(message = WebOrder.NOT_BLANK_MESSAGE)
  private String postcode;
  
// private Long userId;
  
  public void setId(String id) {
    this.id = id;
  }

  public void setDateTimeOfSubmission(Date dateTimeOfSubmission) {
	  this.dateTimeOfSubmission = dateTimeOfSubmission;
  }

  public Date getDateTimeOfSubmission() {
    return dateTimeOfSubmission;
  }

  public String getId() {
    return id;
  }
  
  public String getName() {
	  return name;
  }

  public void setName(String name) {
	  this.name = name;
  }

  public String getAddress1() {
	  return address1;
  }

  public void setAddress1(String address) {
	  this.address1 = address;
  }

  public String getPostcode() {
	  return postcode;
  }

  public void setPostcode(String postcode) {
	  this.postcode = postcode;
  }

//  public void setOrderItems(Map<String, Integer> orderItems) {
//    if (orderItems == null) {
//      this.orderItems = Collections.emptyMap();
//    } else {
//      this.orderItems = Collections.unmodifiableMap(orderItems);
//    }
//  }
  
  public void setOrderItems(Map<String, Integer> orderItems) {
	  if (orderItems == null) {
	      this.orderItems = Collections.emptyMap();
	    } else {
	    	this.orderItems = orderItems;
	    }
  }

  public Map<String, Integer> getOrderItems() {
    return orderItems;
  }
  
}