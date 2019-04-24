package ua.test.mega.tester.core.api.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class Notification {

	@NonNull
	private final String message;

	@NonNull
	private final NotificationType type;

	private final long userId;

	private final Throwable error;
}
