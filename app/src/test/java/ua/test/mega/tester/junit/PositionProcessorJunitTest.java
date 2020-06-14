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
import ua.test.mega.tester.adapters.LoggedInUserAdapterForSpringSecurity;
import ua.test.mega.tester.adapters.PositionAdapterInMemory;
import ua.test.mega.tester.adapters.UserAdapterInMemory;
import ua.test.mega.tester.byCategory.categories.PositionProcessorCategory;
import ua.test.mega.tester.core.PositionProcessor;
import ua.test.mega.tester.core.api.LoggedInUserAdapter;
import ua.test.mega.tester.core.api.PositionAdapter;
import ua.test.mega.tester.core.api.UserAdapter;
import ua.test.mega.tester.core.api.model.Currency;
import ua.test.mega.tester.core.api.model.Order;
import ua.test.mega.tester.core.api.model.Position;
import ua.test.mega.tester.core.api.model.Side;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Category(PositionProcessorCategory.class)
@RunWith(SpringRunner.class)
public class PositionProcessorJunitTest {
    private static UserAdapter userAdapter;
    private PositionProcessor positionProcessor;
    private PositionAdapter positionAdapter;

    @BeforeClass
    public static void init() {
        userAdapter = new UserAdapterInMemory();
    }

    @Before
    public void setUp() {
        LoggedInUserAdapter loggedInUserAdapter = new LoggedInUserAdapterForSpringSecurity(userAdapter);
        positionAdapter = new PositionAdapterInMemory();
        positionProcessor = new PositionProcessor(loggedInUserAdapter, positionAdapter);
    }

    @WithMockUser(username = "user1")
    @Test
    public void findPositions1() {
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

        Position position = Position.builder()
                .accountId(1)
                .orderId(order.getOrderId())
                .executionDate(ZonedDateTime.now())
                .priceInUSD(100)
                .build();

        //when
        positionAdapter.create(position);

        List<Position> actual = positionProcessor.findPositions();

        //then
        List<Position> expected = Collections.singletonList(Position.builder()
                .positionId(1)
                .accountId(position.getAccountId())
                .orderId(position.getOrderId())
                .executionDate(position.getExecutionDate())
                .priceInUSD(position.getPriceInUSD())
                .build());

        Assert.assertEquals(expected, actual);
    }

    @WithAnonymousUser
    @Test(expected = NullPointerException.class)
    public void findPositions2() {
        positionProcessor.findPositions();
    }

    @WithMockUser(username = "admin")
    @Test
    public void findPositions3() {
        //when
        List<Position> actual = positionProcessor.findPositions();

        //then
        Assert.assertEquals(new ArrayList<Position>(), actual);
    }
}