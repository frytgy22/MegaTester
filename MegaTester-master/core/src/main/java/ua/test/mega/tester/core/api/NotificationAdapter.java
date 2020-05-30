package ua.test.mega.tester.core.api;

import reactor.core.publisher.Flux;
import ua.test.mega.tester.core.api.model.Notification;

public interface NotificationAdapter {
	void register(Notification notification);

	Flux<Notification> provideNotificationStream();
}
