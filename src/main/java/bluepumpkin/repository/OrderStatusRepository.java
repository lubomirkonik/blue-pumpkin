package bluepumpkin.repository;

import bluepumpkin.domain.OrderStatus;

import org.springframework.data.repository.CrudRepository;

public interface OrderStatusRepository extends CrudRepository<OrderStatus, String> { //GemfireRepository<OrderStatus, UUID>
	OrderStatus findByOrderId(String orderId);

//	@Query("SELECT DISTINCT * FROM /YummyNoodleOrder WHERE orderId = $1 ORDER BY statusDate")
//	public Collection<OrderStatus> getOrderHistory(UUID orderId);
}