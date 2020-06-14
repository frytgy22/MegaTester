package ua.test.mega.tester.junit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import ua.test.mega.tester.adapters.*;
import ua.test.mega.tester.byCategory.categories.OrderManagementProcessorCategory;
import ua.test.mega.tester.core.OrderManagementProcessor;
import ua.test.mega.tester.core.api.*;
import ua.test.mega.tester.core.api.model.Currency;
import ua.test.mega.tester.core.api.model.Order;
import ua.test.mega.tester.core.api.model.Side;
import ua.test.mega.tester.core.exceptions.InconsistentOrderAmount;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Category(OrderManagementProcessorCategory.class)
@RunWith(SpringRunner.class)
public class OrderManagementProcessorJunitTest {
    private static UserAdapter userAdapter;
    private OrderManagementProcessor orderManagementProcessor;
    private Order firstExpectedOrder;
    private Order secondExpectedOrder;

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

        firstExpectedOrder = Order.builder()
                .accountId(1L)
                .baseCurrency(Currency.UAH)
                .quoteCurrency(Currency.EUR)
                .rate(10)
                .side(Side.BUY)
                .amount(new BigDecimal(10))
                .createDate(ZonedDateTime.now())
                .executionDate(ZonedDateTime.now())
                .build();

        secondExpectedOrder = Order.builder()
                .orderId(1)
                .accountId(firstExpectedOrder.getAccountId())
                .baseCurrency(firstExpectedOrder.getBaseCurrency())
                .quoteCurrency(firstExpectedOrder.getQuoteCurrency())
                .rate(firstExpectedOrder.getRate())
                .side(firstExpectedOrder.getSide())
                .amount(firstExpectedOrder.getAmount())
                .createDate(firstExpectedOrder.getCreateDate())
                .executionDate(firstExpectedOrder.getExecutionDate())
                .build();
    }

    @WithMockUser(username = "user1")
    @Test
    public void placeOrder1() {
        //when
        Order actual = orderManagementProcessor.placeOrder(firstExpectedOrder);

        //then
        Assert.assertEquals(secondExpectedOrder, actual);
    }

    @WithMockUser(username = "admin")
    @Test
    public void placeOrder2() {
        //when
        Order actual = orderManagementProcessor.placeOrder(firstExpectedOrder);

        //then
        Assert.assertNotSame(secondExpectedOrder, actual);
    }

    @WithAnonymousUser
    @Test(expected = NullPointerException.class)
    public void placeOrder3() {
        orderManagementProcessor.placeOrder(firstExpectedOrder);
    }

    @WithMockUser(username = "user1")
    @Test(expected = InconsistentOrderAmount.class)
    public void placeOrder4() {
        //when
        Order expected = Order.builder()
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
        orderManagementProcessor.placeOrder(expected);
    }

    @WithMockUser(username = "user1")
    @Test()
    public void placeOrder5() {
        //when
        Order expected = Order.builder()
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
        Order actual = orderManagementProcessor.placeOrder(expected);

        //then
        assertThat(expected).isEqualToComparingOnlyGivenFields(actual);
    }
}