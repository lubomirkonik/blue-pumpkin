package bluepumpkin.config;

import bluepumpkin.repository.*;
import bluepumpkin.services.AccountService;
import bluepumpkin.services.MenuEventHandler;
import bluepumpkin.services.MenuService;
import bluepumpkin.services.OrderEventHandler;
import bluepumpkin.services.OrderService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceConfig {

	@Bean
	public AccountService accountPersistenceService(AccountRepository accountRepository) {
		return new AccountService(accountRepository);
	}
	
	@Bean
	public OrderService ordersPersistenceService(OrdersRepository ordersRepository, OrderStatusRepository orderStatusRepository) {
		return new OrderEventHandler(ordersRepository , orderStatusRepository);
	}
	
	@Bean
	public MenuService menuPersistenceService(MenuItemRepository menuItemRepository) {
		return new MenuEventHandler(menuItemRepository);
	}
	
}
