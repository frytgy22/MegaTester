package ua.test.mega.MegaTester.core.api;

import reactor.core.publisher.Flux;
import ua.test.mega.MegaTester.core.api.model.Notification;

public interface NotificationAdapter {
	void register(Notification notification);

	Flux<Notification> provideNotificationStream();
}
