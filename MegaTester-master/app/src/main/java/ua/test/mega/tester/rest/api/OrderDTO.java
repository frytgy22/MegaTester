package ua.test.mega.tester.rest.api;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class OrderDTO {

	private final long orderId;

	@NonNull
	private final CurrencyDTO baseCurrency;
	@NonNull
	private final CurrencyDTO quoteCurrency;
	@NonNull
	private final double rate;

	@NonNull
	private final SideDTO side;

	@NonNull
	private final BigDecimal amount;

	@NonNull
	private final ZonedDateTime createDate;
	private final ZonedDateTime executionDate;
}
