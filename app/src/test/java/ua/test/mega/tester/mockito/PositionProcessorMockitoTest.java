package ua.test.mega.tester.mockito;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import ua.test.mega.tester.byCategory.categories.PositionProcessorCategory;
import ua.test.mega.tester.core.PositionProcessor;
import ua.test.mega.tester.core.api.LoggedInUserAdapter;
import ua.test.mega.tester.core.api.PositionAdapter;
import ua.test.mega.tester.core.api.model.Position;
import ua.test.mega.tester.core.api.model.User;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@Category(PositionProcessorCategory.class)
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class PositionProcessorMockitoTest {

    @InjectMocks
    private PositionProcessor positionProcessor;

    @Mock
    private PositionAdapter positionAdapter;

    @Mock
    private LoggedInUserAdapter loggedInUserAdapter;

    @WithMockUser(username = "user1")
    @Test
    public void findPositions1() {

        List<Position> expected = Collections.singletonList(Position.builder()
                .positionId(1)
                .accountId(1)
                .orderId(1)
                .executionDate(ZonedDateTime.of(2020, 3, 5, 1, 1, 1,
                        1, ZoneId.of("UTC")))
                .priceInUSD(100)
                .build());

        when(loggedInUserAdapter.getLoggedInUser()).thenReturn(
                User.builder()
                        .username("user1")
                        .password("user1")
                        .accountId(1)
                        .roles(Collections.singletonList("USER"))
                        .build());

        when(positionAdapter.findAll(1)).thenReturn(expected);

        List<Position> actual = positionProcessor.findPositions();

        Assert.assertEquals(expected, actual);
    }
}