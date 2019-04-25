package ua.test.mega.tester.adapters;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import ua.test.mega.tester.core.api.AccountAdapter;
import ua.test.mega.tester.core.api.UserAdapter;
import ua.test.mega.tester.core.api.model.Account;
import ua.test.mega.tester.core.api.model.User;

@Component
public class AccountAdapterInMemory implements AccountAdapter {

	private static final BigDecimal DEFAULT_BALANCE_IN_USD = BigDecimal.valueOf(1000);
	private final Map<Long, Account> accountByAccountId;

	public AccountAdapterInMemory(UserAdapter userAdapter) {
		Stream<User> stream = userAdapter.getAllUsers()
				.stream();

		accountByAccountId = stream
				.collect((Supplier<Map<Long, Account>>) ConcurrentHashMap::new,
						(accumulator, user) -> accumulator.put(user.getAccountId(), toAccount(user)),
						(m1, m2) -> m1.putAll(m2));
	}

	private Account toAccount(User user) {
		return Account.builder()
				.accountId(user.getAccountId())
				.balanceInUSD(DEFAULT_BALANCE_IN_USD)
				.build();
	}

	@Override
	public Account find(long accountId) {
		return accountByAccountId.get(accountId);
	}

	@Override
	public void update(Account updatedAccount) {
		Objects.requireNonNull(updatedAccount);

		if (!accountByAccountId.containsKey(updatedAccount.getAccountId())) {
			throw new IllegalStateException("Unable update account. Account is missing: {} " + updatedAccount);
		}

		accountByAccountId.put(updatedAccount.getAccountId(), updatedAccount);
	}

	@Override
	public BigDecimal updateBalance(long accountId, BigDecimal amountInUSD) {
		Account account = accountByAccountId.get(accountId);

		Account updatedAccoutn = account.toBuilder()
				.balanceInUSD(account.getBalanceInUSD().add(amountInUSD))
				.build();

		accountByAccountId.put(accountId, updatedAccoutn);

		return updatedAccoutn.getBalanceInUSD();
	}
}
