package ua.test.mega.MegaTester.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.test.mega.MegaTester.core.api.AccountAdapter;
import ua.test.mega.MegaTester.core.api.LoggedInUserAdapter;
import ua.test.mega.MegaTester.core.api.model.Account;

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
}
