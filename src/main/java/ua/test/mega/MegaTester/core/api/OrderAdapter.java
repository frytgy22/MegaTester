package ua.test.mega.MegaTester.core.api;

import java.util.List;

import ua.test.mega.MegaTester.core.api.model.Order;

public interface OrderAdapter {
	Order create(Order order);

	Order find(long orderId);

	List<Order> findAllByAccountId(long accountId);
}
