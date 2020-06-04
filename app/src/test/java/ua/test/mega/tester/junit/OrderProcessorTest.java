package ua.test.mega.tester.junit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import ua.test.mega.tester.adapters.LoggedInUserAdapterForSpringSecurity;
import ua.test.mega.tester.adapters.OrderAdapterInMemory;
import ua.test.mega.tester.adapters.UserAdapterInMemory;
import ua.test.mega.tester.core.OrderProcessor;
import ua.test.mega.tester.core.api.LoggedInUserAdapter;
import ua.test.mega.tester.core.api.OrderAdapter;
import ua.test.mega.tester.core.api.UserAdapter;
import ua.test.mega.tester.core.api.model.Currency;
import ua.test.mega.tester.core.api.model.Order;
import ua.test.mega.tester.core.api.model.Side;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RunWith(SpringRunner.class)
public class OrderProcessorTest {
    private static UserAdapter userAdapter;
    private OrderProcessor orderProcessor;
    private OrderAdapter orderAdapter;

    @BeforeClass
    public static void init() {
        userAdapter = new UserAdapterInMemory();
    }

    @Before
    public void setUp() {
        LoggedInUserAdapter loggedInUserAdapter = new LoggedInUserAdapterForSpringSecurity(userAdapter);
        orderAdapter = new OrderAdapterInMemory();
        orderProcessor = new OrderProcessor(loggedInUserAdapter, orderAdapter);
    }

    @WithMockUser(username = "user1")
    @Test
    public void findOrdersForLoffedinUser1() {
        Order order = Order.builder()
                .accountId(1L)
                .baseCurrency(Currency.UAH)
                .quoteCurrency(Currency.EUR)
                .rate(10)
                .side(Side.BUY)
                .amount(new BigDecimal(100))
                .createDate(ZonedDateTime.now())
                .executionDate(ZonedDateTime.now())
                .build();

        orderAdapter.create(order);

        List<Order> expected = orderProcessor.findOrdersForLoffedinUser();
        List<Order> actual = Collections.singletonList(Order.builder()
                .orderId(1)
                .accountId(order.getAccountId())
                .baseCurrency(order.getBaseCurrency())
                .quoteCurrency(order.getQuoteCurrency())
                .rate(order.getRate())
                .side(order.getSide())
                .amount(order.getAmount())
                .createDate(order.getCreateDate())
                .executionDate(order.getExecutionDate())
                .build());

        Assert.assertEquals(actual, expected);
    }

    @WithAnonymousUser
    @Test(expected = NullPointerException.class)
    public void findOrdersForLoffedinUser2() {
        orderProcessor.findOrdersForLoffedinUser();
    }


    @WithMockUser(username = "admin")
    @Test
    public void findOrdersForLoffedinUser3() {
        List<Order> expected = orderProcessor.findOrdersForLoffedinUser();
        Assert.assertEquals(new ArrayList<Order>(), expected);
    }
}