package ua.test.mega.MegaTester.core;

import ua.test.mega.MegaTester.core.api.model.Notification;
import ua.test.mega.MegaTester.core.api.model.NotificationType;
import ua.test.mega.MegaTester.core.api.model.Order;
import ua.test.mega.MegaTester.core.api.model.Position;

public class NotificationFactory {

	public static Notification newPositionCreated(Position position) {
		return Notification.builder()
				.message("New position was created")
				.type(NotificationType.USER)
				.userId(position.getAccountId())
				.build();
	}

	public static Notification errorOnOrderProcessing(Throwable error, Order newOrder) {
		return Notification.builder()
				.message("Error on processing an order")
				.type(NotificationType.USER)
				.userId(newOrder.getAccountId())
				.error(error)
				.build();
	}
}
