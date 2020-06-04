package ua.test.mega.tester.rest.api;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class AccountDTO {

	private final Long accountId;

	private final BigDecimal balanceInUSD;
}
