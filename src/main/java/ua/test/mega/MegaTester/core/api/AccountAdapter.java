package ua.test.mega.MegaTester.core.api;

import ua.test.mega.MegaTester.core.api.model.Account;

public interface AccountAdapter {

	Account find(long accountId);

	void update(Account updatedAccount);
}
