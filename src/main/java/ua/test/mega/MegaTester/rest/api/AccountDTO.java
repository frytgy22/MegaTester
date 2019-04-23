package ua.test.mega.MegaTester.rest.api;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class AccountDTO {

	private final Long accountId;

	public final BigDecimal balanceInUSD;
}
