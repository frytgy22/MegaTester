package ua.test.mega.tester.mockito;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import ua.test.mega.tester.core.OrderProcessor;
import ua.test.mega.tester.core.api.LoggedInUserAdapter;
import ua.test.mega.tester.core.api.OrderAdapter;
import ua.test.mega.tester.core.api.model.Currency;
import ua.test.mega.tester.core.api.model.Order;
import ua.test.mega.tester.core.api.model.Side;
import ua.test.mega.tester.core.api.model.User;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class OrderProcessorTest {

	@InjectMocks
	private OrderProcessor orderProcessor;

	@Mock
	private OrderAdapter orderAdapter;

	@Mock
	private LoggedInUserAdapter loggedInUserAdapter;

	@WithMockUser(username = "user1")
	@Test
	public void findOrdersForLoffedinUser1() {

		List<Order> actual = Collections.singletonList(
				Order.builder()
						.orderId(1)
						.accountId(1)
						.baseCurrency(Currency.UAH)
						.quoteCurrency(Currency.EUR)
						.rate(10)
						.side(Side.BUY)
						.amount(new BigDecimal(100))
						.createDate(ZonedDateTime.of(2020, 3, 5, 1, 1, 1,
								1, ZoneId.of("UTC")))
						.executionDate(ZonedDateTime.of(2020, 3, 5, 1, 1, 1,
								1, ZoneId.of("UTC")))
						.build());

		when(orderAdapter.findAllByAccountId(1)).thenReturn(actual);

		when(loggedInUserAdapter.getLoggedInUser()).thenReturn(
				User.builder()
						.username("user1")
						.password("user1")
						.accountId(1)
						.roles(Collections.singletonList("USER"))
						.build());

		List<Order> expected = orderProcessor.findOrdersForLoffedinUser();

		Assert.assertEquals(expected, actual);
	}
}