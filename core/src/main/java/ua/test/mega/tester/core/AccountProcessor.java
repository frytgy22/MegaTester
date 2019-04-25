package ua.test.mega.tester.core;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.test.mega.tester.core.api.AccountAdapter;
import ua.test.mega.tester.core.api.LoggedInUserAdapter;
import ua.test.mega.tester.core.api.model.Account;

@Component
public class AccountProcessor {

	private LoggedInUserAdapter loggedInUserAdapter;
	private AccountAdapter accountAdapter;

	@Autowired
	public AccountProcessor(LoggedInUserAdapter loggedInUserAdapter, AccountAdapter accountAdapter) {
		this.loggedInUserAdapter = loggedInUserAdapter;
		this.accountAdapter = accountAdapter;
	}

	public Account loadLoggedInUserAccountInfo() {
		long accountId = loggedInUserAdapter.getLoggedInUser().getAccountId();

		return accountAdapter.find(accountId);
	}

	public Account loadAccountInfo(long accountId) {
		return accountAdapter.find(accountId);
	}

	public BigDecimal deposit(long accountId, BigDecimal amount) {
		return accountAdapter.updateBalance(accountId, amount);
	}

	public BigDecimal withdrawal(long accountId, BigDecimal amountInUSD) {
		return accountAdapter.updateBalance(accountId, amountInUSD.negate());
	}
}
