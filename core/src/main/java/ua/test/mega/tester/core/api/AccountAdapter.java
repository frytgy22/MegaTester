package ua.test.mega.tester.core.api;

import ua.test.mega.tester.core.api.model.Account;

public interface AccountAdapter {

	Account find(long accountId);

	void update(Account updatedAccount);
}
