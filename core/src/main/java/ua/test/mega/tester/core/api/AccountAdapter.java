package ua.test.mega.tester.core.api;

import java.math.BigDecimal;

import ua.test.mega.tester.core.api.model.Account;

public interface AccountAdapter {

	Account find(long accountId);

	void update(Account updatedAccount);

	BigDecimal updateBalance(long accountId, BigDecimal amountInUSD);
}
