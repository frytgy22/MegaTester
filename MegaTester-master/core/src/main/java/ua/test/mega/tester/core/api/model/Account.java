package ua.test.mega.tester.core.api.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Account {

	private final Long accountId;

	private final BigDecimal balanceInUSD;
}
