package ua.test.mega.tester.junit;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import ua.test.mega.tester.core.AccountProcessor;
import ua.test.mega.tester.core.api.model.Account;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountProcessorTest {
	private static final String ERROR_MESSAGE = "doesn't match to ";

	@Autowired
	private AccountProcessor accountProcessor;

	@WithMockUser(username = "user1")
	@Test
	public void loadLoggedInUserAccountInfo1() {

		//given

		Account actualFirstAccount = Account.builder()
				.accountId(1L)
				.balanceInUSD(new BigDecimal(2500))
				.build();

		Account expected = accountProcessor.loadLoggedInUserAccountInfo();
		Assert.assertEquals(expected, actualFirstAccount);
	}

	@WithMockUser(username = "admin")
	@Test
	public void loadLoggedInUserAccountInfo2() {
		//given
		Account actualSecondAccount = Account.builder()
				.accountId(-1L)
				.balanceInUSD(new BigDecimal(2500))
				.build();

		//when
		Account expected = accountProcessor.loadLoggedInUserAccountInfo();

		//then
		Assert.assertEquals(expected + ERROR_MESSAGE + actualSecondAccount, expected, actualSecondAccount);
	}

	@WithAnonymousUser
	@Test(expected = NullPointerException.class)
	public void loadLoggedInUserAccountInfo3() {
		accountProcessor.loadLoggedInUserAccountInfo();
	}

	@Test
	public void loadAccountInfo1() {
		//given
		Account actualThirdAccount = Account.builder()
				.accountId(1L)
				.balanceInUSD(new BigDecimal(500))
				.build();

		//when
		Account expected = accountProcessor.loadAccountInfo(1L);

		//then
		Assert.assertEquals(expected + ERROR_MESSAGE + actualThirdAccount, expected, actualThirdAccount);
	}

	@Test
	public void loadAccountInfo2() {
		//given
		Account actualFourAccount = Account.builder()
				.accountId(-1L)
				.balanceInUSD(new BigDecimal(1000))
				.build();

		//when
		Account expected = accountProcessor.loadAccountInfo(-1L);

		//then
		Assert.assertEquals(expected + ERROR_MESSAGE + actualFourAccount, expected, actualFourAccount);
	}

	@Test
	public void loadAccountInfo3() {
		//when
		Account expected = accountProcessor.loadAccountInfo(-5L);

		//then
		Assert.assertNull(expected + "must be null", expected);
	}

	@Test
	public void deposit1() {
		//when
		BigDecimal expected = accountProcessor.deposit(1L, new BigDecimal(2000));

		//then
		Assert.assertEquals(expected + ERROR_MESSAGE + new BigDecimal(2500), expected, new BigDecimal(2500));
	}

	@Test
	public void deposit2() {
		//when
		BigDecimal expected = accountProcessor.deposit(-1L, new BigDecimal(2000));

		//then
		Assert.assertEquals(expected + ERROR_MESSAGE + new BigDecimal(3000), expected, new BigDecimal(3000));
	}

	@Test(expected = NullPointerException.class)
	public void deposit3() {
		accountProcessor.deposit(-5L, new BigDecimal(2000));
	}

	@Test
	public void withdrawal() {
		//when
		BigDecimal expected = accountProcessor.withdrawal(1L, new BigDecimal(500));

		//then
		Assert.assertEquals(expected, new BigDecimal(500));
	}

	@Test
	public void withdrawal2() {
		//when
		BigDecimal expected = accountProcessor.withdrawal(-1L, new BigDecimal(500));

		//then
		Assert.assertEquals(expected + ERROR_MESSAGE + new BigDecimal(2500), expected, new BigDecimal(2500));

	}

	@Test(expected = NullPointerException.class)
	public void withdrawal3() {
		accountProcessor.withdrawal(-5L, new BigDecimal(500));
	}
}
