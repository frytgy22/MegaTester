package ua.test.mega.tester.junit;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

import ua.test.mega.tester.adapters.*;
import ua.test.mega.tester.core.OrderManagementProcessor;
import ua.test.mega.tester.core.api.*;
import ua.test.mega.tester.core.api.model.Currency;
import ua.test.mega.tester.core.api.model.Order;
import ua.test.mega.tester.core.api.model.Side;
import ua.test.mega.tester.core.exceptions.InconsistentOrderAmount;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@RunWith(SpringRunner.class)
public class OrderManagementProcessorTest {
	private static UserAdapter userAdapter;
	private OrderManagementProcessor orderManagementProcessor;
	private Order firstActualOrder;
	private Order secondActualOrder;

	@BeforeClass
	public static void init() {
		userAdapter = new UserAdapterInMemory();
	}

	@Before
	public void setUp() {
		LoggedInUserAdapter loggedInUserAdapter = new LoggedInUserAdapterForSpringSecurity(userAdapter);
		OrderAdapter orderAdapter = new OrderAdapterInMemory();
		PositionAdapter positionAdapter = new PositionAdapterInMemory();
		AccountAdapter accountAdapter = new AccountAdapterInMemory(userAdapter);
		NotificationAdapter notificationAdapter = new NotificationReactiveStreamAdapter();

		orderManagementProcessor = new OrderManagementProcessor(
				orderAdapter, positionAdapter, accountAdapter, notificationAdapter, loggedInUserAdapter);

		firstActualOrder = Order.builder()
				.accountId(1L)
				.baseCurrency(Currency.UAH)
				.quoteCurrency(Currency.EUR)
				.rate(10)
				.side(Side.BUY)
				.amount(new BigDecimal(10))
				.createDate(ZonedDateTime.now())
				.executionDate(ZonedDateTime.now())
				.build();

		secondActualOrder = Order.builder()
				.orderId(1)
				.accountId(firstActualOrder.getAccountId())
				.baseCurrency(firstActualOrder.getBaseCurrency())
				.quoteCurrency(firstActualOrder.getQuoteCurrency())
				.rate(firstActualOrder.getRate())
				.side(firstActualOrder.getSide())
				.amount(firstActualOrder.getAmount())
				.createDate(firstActualOrder.getCreateDate())
				.executionDate(firstActualOrder.getExecutionDate())
				.build();
	}

	@WithMockUser(username = "user1")
	@Test
	public void placeOrder1() {
		//when
		Order expected = orderManagementProcessor.placeOrder(firstActualOrder);

		//then
		Assert.assertEquals(expected, secondActualOrder);
	}

	@WithMockUser(username = "admin")
	@Test
	public void placeOrder2() {
		//when
		Order expected = orderManagementProcessor.placeOrder(firstActualOrder);

		//then
		Assert.assertNotEquals(expected, secondActualOrder);
	}

	@WithAnonymousUser
	@Test(expected = NullPointerException.class)
	public void placeOrder3() {
		orderManagementProcessor.placeOrder(firstActualOrder);
	}

	@WithMockUser(username = "user1")
	@Test(expected = InconsistentOrderAmount.class)
	public void placeOrder4() {
		//when
		Order newOrder = Order.builder()
				.accountId(1L)
				.baseCurrency(Currency.UAH)
				.quoteCurrency(Currency.EUR)
				.rate(10)
				.side(Side.BUY)
				.amount(new BigDecimal(0))
				.createDate(ZonedDateTime.now())
				.executionDate(ZonedDateTime.now())
				.build();

		//then
		orderManagementProcessor.placeOrder(newOrder);
	}

	@WithMockUser(username = "user1")
	@Test()
	public void placeOrder5() {
		//when
		Order newOrder = Order.builder()
				.accountId(1)
				.baseCurrency(Currency.UAH)
				.quoteCurrency(Currency.EUR)
				.rate(10)
				.side(Side.BUY)
				.amount(new BigDecimal(10))
				.createDate(ZonedDateTime.now())
				.executionDate(ZonedDateTime.now())
				.build();

		//when
		Order expected = orderManagementProcessor.placeOrder(newOrder);

		//then
		assertThat(newOrder).isEqualToComparingOnlyGivenFields(expected);
	}
}