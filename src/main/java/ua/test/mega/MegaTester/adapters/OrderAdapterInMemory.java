package ua.test.mega.MegaTester.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

import ua.test.mega.MegaTester.core.api.OrderAdapter;
import ua.test.mega.MegaTester.core.api.model.Order;

@Component
public class OrderAdapterInMemory implements OrderAdapter {

	private AtomicLong idGenerator = new AtomicLong();
	private Map<Long, Order> ordersByUserId = new HashMap<>();
	private Map<Long, List<Order>> ordersByAccountId = new HashMap<>();

	@Override
	public Order create(Order order) {
		Objects.requireNonNull(order);

		Order orderForSave = order.toBuilder()
				.orderId(idGenerator.incrementAndGet())
				.build();

		ordersByUserId.put(orderForSave.getOrderId(), orderForSave);
		ordersByAccountId.computeIfAbsent(order.getAccountId(), accountId -> new ArrayList<>())
				.add(orderForSave);

		return orderForSave;
	}

	@Override
	public Order find(long orderId) {
		return ordersByUserId.get(orderId);
	}

	@Override
	public List<Order> findAllByAccountId(long accountId) {
		return ordersByAccountId.computeIfAbsent(accountId, __ -> new ArrayList<>());
	}
}
