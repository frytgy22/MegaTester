package ua.test.mega.tester.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.test.mega.tester.core.api.LoggedInUserAdapter;
import ua.test.mega.tester.core.api.OrderAdapter;
import ua.test.mega.tester.core.api.model.Order;

@Component
public class OrderProcessor {

	private LoggedInUserAdapter loggedInUserAdapter;
	private OrderAdapter orderAdapter;

	@Autowired
	public OrderProcessor(LoggedInUserAdapter loggedInUserAdapter, OrderAdapter orderAdapter) {
		this.loggedInUserAdapter = loggedInUserAdapter;
		this.orderAdapter = orderAdapter;
	}

	public List<Order> findOrdersForLoffedinUser() {
		long accountId = loggedInUserAdapter.getLoggedInUser().getAccountId();

		return orderAdapter.findAllByAccountId(accountId);
	}
}
