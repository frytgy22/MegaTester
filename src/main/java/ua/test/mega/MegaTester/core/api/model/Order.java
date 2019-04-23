package ua.test.mega.MegaTester.core.api.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Order {

	@NonNull
	private final long orderId;

	@NonNull
	private long accountId;

	@NonNull
	private final Currency baseCurrency;
	@NonNull
	private final Currency quoteCurrency;
	@NonNull
	private final double rate;

	@NonNull
	private final Side side;

	@NonNull
	private final BigDecimal amount;

	@NonNull
	private final ZonedDateTime createDate;
	private final ZonedDateTime executionDate;

}
