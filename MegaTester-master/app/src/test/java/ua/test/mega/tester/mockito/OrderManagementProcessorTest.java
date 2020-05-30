package ua.test.mega.tester.mockito;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import ua.test.mega.tester.core.OrderManagementProcessor;
import ua.test.mega.tester.core.api.LoggedInUserAdapter;
import ua.test.mega.tester.core.api.OrderAdapter;
import ua.test.mega.tester.core.api.model.Currency;
import ua.test.mega.tester.core.api.model.Order;
import ua.test.mega.tester.core.api.model.Side;
import ua.test.mega.tester.core.api.model.User;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class OrderManagementProcessorTest {

    @InjectMocks
    private OrderManagementProcessor orderManagementProcessor;

    @Mock
    private OrderAdapter orderAdapter;

    @Mock
    private LoggedInUserAdapter loggedInUserAdapter;

    @WithMockUser(username = "user1")
    @Test
    public void placeOrder() {

        Order order = Order.builder()
                .accountId(1L)
                .baseCurrency(Currency.UAH)
                .quoteCurrency(Currency.EUR)
                .rate(10)
                .side(Side.BUY)
                .amount(new BigDecimal(10))
                .createDate(ZonedDateTime.now())
                .executionDate(ZonedDateTime.now())
                .build();

        when(loggedInUserAdapter.getLoggedInUser()).thenReturn(
                User.builder()
                        .username("user1")
                        .password("user1")
                        .accountId(1)
                        .roles(Collections.singletonList("USER"))
                        .build());


        when(orderAdapter.create(order)).thenReturn(order);

        Order expected = orderManagementProcessor.placeOrder(order);

        Assert.assertEquals(order, expected);
    }

}