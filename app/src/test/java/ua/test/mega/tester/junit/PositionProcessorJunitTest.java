package ua.test.mega.tester.junit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ua.test.mega.tester.adapters.PositionAdapterInMemory;
import ua.test.mega.tester.byCategory.categories.PositionProcessorCategory;
import ua.test.mega.tester.core.PositionProcessor;
import ua.test.mega.tester.core.api.LoggedInUserAdapter;
import ua.test.mega.tester.core.api.PositionAdapter;
import ua.test.mega.tester.core.api.model.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Category(PositionProcessorCategory.class)
@RunWith(JUnit4.class)
public class PositionProcessorJunitTest {

    private PositionProcessor positionProcessor;
    private PositionAdapter positionAdapter;

    @Before
    public void setUp() {
        LoggedInUserAdapter loggedInUserAdapter = () -> User.builder()
                .accountId(1)
                .username("admin")
                .password("password")
                .roles(Collections.singletonList("ADMIN"))
                .build();

        positionAdapter = new PositionAdapterInMemory();
        positionProcessor = new PositionProcessor(loggedInUserAdapter, positionAdapter);
    }

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

    @Test
    public void findPositions3() {
        //when
        List<Position> actual = positionProcessor.findPositions();

        //then
        Assert.assertEquals(new ArrayList<Position>(), actual);
    }
}