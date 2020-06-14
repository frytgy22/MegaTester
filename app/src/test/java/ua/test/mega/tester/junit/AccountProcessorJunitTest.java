package ua.test.mega.tester.junit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import ua.test.mega.tester.byCategory.categories.AccountProcessorCategory;
import ua.test.mega.tester.core.AccountProcessor;
import ua.test.mega.tester.core.api.model.Account;

import java.math.BigDecimal;

@Category(AccountProcessorCategory.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountProcessorJunitTest {
    private static final String ERROR_MESSAGE = "doesn't match to ";

    @Autowired
    private AccountProcessor accountProcessor;

    @WithMockUser(username = "user1")
    @Test
    public void loadLoggedInUserAccountInfo1() {

        //given

        Account expected = Account.builder()
                .accountId(1L)
                .balanceInUSD(new BigDecimal(2500))
                .build();

        Account actual = accountProcessor.loadLoggedInUserAccountInfo();
        Assert.assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin")
    @Test
    public void loadLoggedInUserAccountInfo2() {
        //given
        Account expected = Account.builder()
                .accountId(-1L)
                .balanceInUSD(new BigDecimal(2500))
                .build();

        //when
        Account actual = accountProcessor.loadLoggedInUserAccountInfo();

        //then
        Assert.assertEquals(expected + ERROR_MESSAGE + actual, expected, actual);
    }

    @WithAnonymousUser
    @Test(expected = NullPointerException.class)
    public void loadLoggedInUserAccountInfo3() {
        accountProcessor.loadLoggedInUserAccountInfo();
    }

    @Test
    public void loadAccountInfo1() {
        //given
        Account expected = Account.builder()
                .accountId(1L)
                .balanceInUSD(new BigDecimal(500))
                .build();

        //when
        Account actual = accountProcessor.loadAccountInfo(1L);

        //then
        Assert.assertEquals(expected + ERROR_MESSAGE + actual, expected, actual);
    }

    @Test
    public void loadAccountInfo2() {
        //given
        Account expected = Account.builder()
                .accountId(-1L)
                .balanceInUSD(new BigDecimal(1000))
                .build();

        //when
        Account actual = accountProcessor.loadAccountInfo(-1L);

        //then
        Assert.assertEquals(expected + ERROR_MESSAGE + actual, expected, actual);
    }

    @Test
    public void loadAccountInfo3() {
        //when
        Account actual = accountProcessor.loadAccountInfo(-5L);

        //then
        Assert.assertNull(actual + "must be null", actual);
    }

    @Test
    public void deposit1() {
        //when
        BigDecimal actual = accountProcessor.deposit(1L, new BigDecimal(2000));

        //then
        Assert.assertEquals(actual + ERROR_MESSAGE + new BigDecimal(2500),
                new BigDecimal(2500), actual);
    }

    @Test
    public void deposit2() {
        //when
        BigDecimal actual = accountProcessor.deposit(-1L, new BigDecimal(2000));

        //then
        Assert.assertEquals(actual + ERROR_MESSAGE + new BigDecimal(3000),
                new BigDecimal(3000), actual);
    }

    @Test(expected = NullPointerException.class)
    public void deposit3() {
        accountProcessor.deposit(-5L, new BigDecimal(2000));
    }

    @Test
    public void withdrawal() {
        //when
        BigDecimal actual = accountProcessor.withdrawal(1L, new BigDecimal(500));

        //then
        Assert.assertEquals(new BigDecimal(500), actual);
    }

    @Test
    public void withdrawal2() {
        //when
        BigDecimal actual = accountProcessor.withdrawal(-1L, new BigDecimal(500));

        //then
        Assert.assertEquals(actual + ERROR_MESSAGE + new BigDecimal(2500),
                new BigDecimal(2500), actual);

    }

    @Test(expected = NullPointerException.class)
    public void withdrawal3() {
        accountProcessor.withdrawal(-5L, new BigDecimal(500));
    }
}
