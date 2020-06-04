package ua.test.mega.tester.junit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import ua.test.mega.tester.adapters.*;
import ua.test.mega.tester.core.OrderManagementProcessor;
import ua.test.mega.tester.core.api.*;
import ua.test.mega.tester.core.api.model.Currency;
import ua.test.mega.tester.core.api.model.Order;
import ua.test.mega.tester.core.api.model.Side;
import ua.test.mega.tester.core.exceptions.InconsistentOrderAmount;
import ua.test.mega.tester.core.exceptions.InconsistentOrderParameters;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@RunWith(SpringRunner.class)
public class OrderManagementProcessorTest {
    private static UserAdapter userAdapter;
    private OrderManagementProcessor orderManagementProcessor;
    private Order order;
    private Order actual;

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

        order = Order.builder()
                .accountId(1L)
                .baseCurrency(Currency.UAH)
                .quoteCurrency(Currency.EUR)
                .rate(10)
                .side(Side.BUY)
                .amount(new BigDecimal(10))
                .createDate(ZonedDateTime.now())
                .executionDate(ZonedDateTime.now())
                .build();

        actual = Order.builder()
                .orderId(1)
                .accountId(order.getAccountId())
                .baseCurrency(order.getBaseCurrency())
                .quoteCurrency(order.getQuoteCurrency())
                .rate(order.getRate())
                .side(order.getSide())
                .amount(order.getAmount())
                .createDate(order.getCreateDate())
                .executionDate(order.getExecutionDate())
                .build();
    }

    @WithMockUser(username = "user1")
    @Test
    public void placeOrder1() {
        Order expected = orderManagementProcessor.placeOrder(order);
        Assert.assertEquals(actual, expected);
    }

    @WithMockUser(username = "admin")
    @Test
    public void placeOrder2() {
        Order expected = orderManagementProcessor.placeOrder(order);
        Assert.assertNotEquals(actual, expected);
    }

    @WithAnonymousUser
    @Test(expected = NullPointerException.class)
    public void placeOrder3() {
        orderManagementProcessor.placeOrder(order);
    }

    @WithMockUser(username = "user1")
    @Test(expected = InconsistentOrderAmount.class)
    public void placeOrder4() {
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

        orderManagementProcessor.placeOrder(newOrder);
    }

    @WithMockUser(username = "user1")
    @Test(expected = InconsistentOrderParameters.class)
    public void placeOrder5() {
        Order newOrder = Order.builder()
                .accountId(0)
                .baseCurrency(Currency.UAH)
                .quoteCurrency(Currency.EUR)
                .rate(10)
                .side(Side.BUY)
                .amount(new BigDecimal(10))
                .createDate(ZonedDateTime.now())
                .executionDate(ZonedDateTime.now())
                .build();

        orderManagementProcessor.placeOrder(newOrder);
    }
}