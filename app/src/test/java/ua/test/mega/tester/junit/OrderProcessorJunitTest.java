package ua.test.mega.tester.junit;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ua.test.mega.tester.adapters.OrderAdapterInMemory;
import ua.test.mega.tester.byCategory.categories.OrderProcessorCategory;
import ua.test.mega.tester.core.OrderProcessor;
import ua.test.mega.tester.core.api.LoggedInUserAdapter;
import ua.test.mega.tester.core.api.OrderAdapter;
import ua.test.mega.tester.core.api.model.Currency;
import ua.test.mega.tester.core.api.model.Order;
import ua.test.mega.tester.core.api.model.Side;
import ua.test.mega.tester.core.api.model.User;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Category(OrderProcessorCategory.class)
@RunWith(JUnit4.class)
public class OrderProcessorJunitTest {
    private OrderProcessor orderProcessor;
    private OrderAdapter orderAdapter;

    @Before
    public void setUp() {
        LoggedInUserAdapter loggedInUserAdapter = () -> User.builder()
                .accountId(1)
                .username("admin")
                .password("password")
                .roles(Collections.singletonList("ADMIN"))
                .build();

        orderAdapter = new OrderAdapterInMemory();
        orderProcessor = new OrderProcessor(loggedInUserAdapter, orderAdapter);
    }

    @Test
    public void findOrdersForLoffedinUser1() {
        //given
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

        //when
        orderAdapter.create(order);

        List<Order> actual = orderProcessor.findOrdersForLoffedinUser();
        log.info("actual order: {}", order);

        //then
        List<Order> expected = Collections.singletonList(Order.builder()
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

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void findOrdersForLoffedinUser3() {
        //when
        List<Order> actual = orderProcessor.findOrdersForLoffedinUser();

        //then
        Assert.assertEquals(new ArrayList<Order>(), actual);
    }
}