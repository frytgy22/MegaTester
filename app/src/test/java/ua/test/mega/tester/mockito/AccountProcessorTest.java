package ua.test.mega.tester.mockito;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import ua.test.mega.tester.core.AccountProcessor;
import ua.test.mega.tester.core.api.AccountAdapter;
import ua.test.mega.tester.core.api.LoggedInUserAdapter;
import ua.test.mega.tester.core.api.model.Account;
import ua.test.mega.tester.core.api.model.User;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class AccountProcessorTest {
	private static Account actualFirstAccount;

	@InjectMocks
	private AccountProcessor accountProcessor;

	@Mock
	private LoggedInUserAdapter loggedInUserAdapter;
	@Mock
	private AccountAdapter accountAdapter;

	@BeforeClass
	public static void init() {
		actualFirstAccount = Account.builder()
				.accountId(1L)
				.balanceInUSD(new BigDecimal(1000))
				.build();
	}

	@Before
	public void setUp() {
		when(accountAdapter.find(1)).thenReturn(
				Account.builder()
						.accountId(1L)
						.balanceInUSD(new BigDecimal(1000))
						.build());
	}

	@WithMockUser(username = "user1")
	@Test
	public void loadLoggedInUserAccountInfo1() {

		when(loggedInUserAdapter.getLoggedInUser()).thenReturn(
				User.builder()
						.username("user1")
						.password("user1")
						.accountId(1)
						.roles(Collections.singletonList("USER"))
						.build());

		Account expected = accountProcessor.loadLoggedInUserAccountInfo();
		Assert.assertEquals(expected, actualFirstAccount);
	}

	@Test
	public void loadAccountInfo2() {
		Account expected = accountProcessor.loadAccountInfo(1L);
		Assert.assertEquals(expected, actualFirstAccount);
	}

	@Test
	public void deposit1() {
		when(accountAdapter.updateBalance(1, new BigDecimal(2000))).thenReturn(new BigDecimal(3000));

		BigDecimal expected = accountProcessor.deposit(1L, new BigDecimal(2000));
		Assert.assertEquals(expected, new BigDecimal(3000));
	}

	@Test
	public void deposit2() {
		when(accountAdapter.updateBalance(-1, new BigDecimal(2000))).thenReturn(new BigDecimal(3000));

		BigDecimal expected = accountProcessor.deposit(-1L, new BigDecimal(2000));
		Assert.assertEquals(expected, new BigDecimal(3000));
	}

	@Test
	public void withdrawal() {
		when(accountAdapter.updateBalance(1, new BigDecimal(-500))).thenReturn(new BigDecimal(500));

		BigDecimal expected = accountProcessor.withdrawal(1, new BigDecimal(500));
		Assert.assertEquals(expected, new BigDecimal(500));
	}

	@Test
	public void withdrawal2() {
		when(accountAdapter.updateBalance(-1, new BigDecimal(-1000))).thenReturn(new BigDecimal(0));

		BigDecimal expected = accountProcessor.withdrawal(-1L, new BigDecimal(1000));
		Assert.assertEquals(expected, new BigDecimal(0));
	}
}
