package ua.test.mega.tester.core.api.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value
@Builder
@ToString
public class Notification {

	@NonNull
	private final String message;

	@NonNull
	private final NotificationType type;

	private final long userId;

	private final Throwable error;

	private final long orderId;

	private final long positionId;
}
