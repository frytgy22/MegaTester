package ua.test.mega.tester.core.api;

import java.util.List;

import ua.test.mega.tester.core.api.model.Order;

public interface OrderAdapter {
	Order create(Order order);

	Order find(long orderId);

	List<Order> findAllByAccountId(long accountId);
}
