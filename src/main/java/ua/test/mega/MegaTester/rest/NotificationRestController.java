package ua.test.mega.MegaTester.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import ua.test.mega.MegaTester.core.NotificationProcessor;
import ua.test.mega.MegaTester.core.api.model.Notification;
import ua.test.mega.MegaTester.core.api.model.NotificationType;
import ua.test.mega.MegaTester.rest.api.NotificationDTO;
import ua.test.mega.MegaTester.rest.api.NotificationTypeDTO;

@RestController
@RequestMapping("/api/notification/")
public class NotificationRestController {

	private final NotificationProcessor notificationProcessor;

	@Autowired
	public NotificationRestController(NotificationProcessor notificationProcessor) {
		this.notificationProcessor = notificationProcessor;
	}

	@GetMapping(path = "/", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<NotificationDTO> getNotificationsForLoggedInUser() {
		return notificationProcessor.provideNotificationsForLoggedinUser()
				.map(this::toNotificationDTO);
	}

	@Secured("ROLE_ADMIN")
	@GetMapping(path = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<NotificationDTO> getAllNotifications() {
		return notificationProcessor.provideAllNotifications()
				.map(this::toNotificationDTO);
	}

	private NotificationDTO toNotificationDTO(Notification notification) {
		return NotificationDTO.builder()
				.message(notification.getMessage())
				.type(toNotificationTypeDTO(notification.getType()))
				.userId(notification.getUserId())
				.error(notification.getError())
				.build();
	}

	private NotificationTypeDTO toNotificationTypeDTO(NotificationType type) {
		switch (type) {
			case USER:
				return NotificationTypeDTO.USER;
			case CONFIGURATION:
				return NotificationTypeDTO.CONFIGURATION;
			case ADMINISTRATION:
				return NotificationTypeDTO.ADMINISTRATION;
			case INFO:
				return NotificationTypeDTO.INFO;
			default:
				throw new IllegalArgumentException("Unknown NotificationType " + type);
		}
	}
}
