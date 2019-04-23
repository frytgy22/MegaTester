package ua.test.mega.MegaTester.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import ua.test.mega.MegaTester.core.api.LoggedInUserAdapter;
import ua.test.mega.MegaTester.core.api.NotificationAdapter;
import ua.test.mega.MegaTester.core.api.model.Notification;
import ua.test.mega.MegaTester.core.api.model.NotificationType;

@Log4j2
@Component
public class NotificationProcessor {

	private LoggedInUserAdapter loggedInUserAdapter;
	private NotificationAdapter notificationAdapter;

	@Autowired
	public NotificationProcessor(LoggedInUserAdapter loggedInUserAdapter,
			NotificationAdapter notificationAdapter) {
		this.loggedInUserAdapter = loggedInUserAdapter;
		this.notificationAdapter = notificationAdapter;
	}

	public Flux<Notification> provideNotificationsForLoggedinUser() {
		long accountId = loggedInUserAdapter.getLoggedInUser().getAccountId();

		log.info("Get notifications for account {}", accountId);

		return notificationAdapter.provideNotificationStream()
				.filter(notification -> isNotificationForUser(notification, accountId));
	}

	private boolean isNotificationForUser(Notification notification, long accountId) {
		return NotificationType.USER == notification.getType() && notification.getUserId() == accountId;
	}
}
