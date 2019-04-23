package ua.test.mega.MegaTester.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.test.mega.MegaTester.core.api.AccountAdapter;
import ua.test.mega.MegaTester.core.api.model.Account;

@Component
public class AccountProcessor {

	private AccountAdapter accountAdapter;

	@Autowired
	public AccountProcessor(AccountAdapter accountAdapter) {
		this.accountAdapter = accountAdapter;
	}

	public Account loadAccountInfo(long accountId) {
		return accountAdapter.find(accountId);
	}
}
