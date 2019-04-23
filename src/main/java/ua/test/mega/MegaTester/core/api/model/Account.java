package ua.test.mega.MegaTester.core.api.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Account {

	private final Long accountId;

	private final BigDecimal balanceInUSD;
}
