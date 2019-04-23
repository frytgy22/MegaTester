package ua.test.mega.MegaTester.rest.api;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class NotificationDTO {

	@NonNull
	private final String message;

	@NonNull
	private final NotificationTypeDTO type;

	private final long userId;

	private final Throwable error;
}
