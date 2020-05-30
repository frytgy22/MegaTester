package ua.test.mega.tester.core;

import ua.test.mega.tester.core.api.model.Notification;
import ua.test.mega.tester.core.api.model.NotificationType;
import ua.test.mega.tester.core.api.model.Order;
import ua.test.mega.tester.core.api.model.Position;

public class NotificationFactory {

	public static Notification newPositionCreated(Order order, Position position) {
		return Notification.builder()
				.message("New position was created")
				.type(NotificationType.USER)
				.userId(position.getAccountId())
				.orderId(order.getOrderId())
				.positionId(position.getPositionId())
				.build();
	}

	public static Notification errorOnOrderProcessing(Throwable error, Order order) {
		return Notification.builder()
				.message("Error on processing an order")
				.type(NotificationType.USER)
				.userId(order.getAccountId())
				.orderId(order.getOrderId())
				.error(error)
				.build();
	}
}
