package ua.test.mega.tester.rest.api;

import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class PositionDTO {

	private final long positionId;

	private long accountId;

	private final long orderId;

	@NonNull
	private final ZonedDateTime executionDate;

	private final double priceInUSD;
}
