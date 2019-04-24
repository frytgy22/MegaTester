package ua.test.mega.tester.core.api.model;

import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Position {

	private final long positionId;

	@NonNull
	private long accountId;

	@NonNull
	private final long orderId;

	@NonNull
	private final ZonedDateTime executionDate;

	@NonNull
	private final double priceInUSD;
}
