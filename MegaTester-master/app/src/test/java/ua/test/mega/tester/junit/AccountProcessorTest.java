package ua.test.mega.tester.junit;


import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import ua.test.mega.tester.adapters.AccountAdapterInMemory;
import ua.test.mega.tester.adapters.LoggedInUserAdapterForSpringSecurity;
import ua.test.mega.tester.adapters.UserAdapterInMemory;
import ua.test.mega.tester.core.AccountProcessor;
import ua.test.mega.tester.core.api.AccountAdapter;
import ua.test.mega.tester.core.api.LoggedInUserAdapter;
import ua.test.mega.tester.core.api.UserAdapter;
import ua.test.mega.tester.core.api.model.Account;

import java.math.BigDecimal;


@RunWith(SpringRunner.class)
public class AccountProcessorTest {
    private static Account actualFirstAccount;
    private static Account actualSecondAccount;

    private static UserAdapter userAdapter;
    private AccountProcessor accountProcessor;


    @BeforeClass
    public static void init() {
        userAdapter = new UserAdapterInMemory();

        actualFirstAccount = Account.builder()
                .accountId(1L)
                .balanceInUSD(new BigDecimal(1000))
                .build();

        actualSecondAccount = Account.builder()
                .accountId(-1L)
                .balanceInUSD(new BigDecimal(1000))
                .build();
    }

    @Before
    public void setUp() {
        LoggedInUserAdapter loggedInUserAdapter = new LoggedInUserAdapterForSpringSecurity(userAdapter);
        AccountAdapter accountAdapter = new AccountAdapterInMemory(userAdapter);

        accountProcessor = new AccountProcessor(loggedInUserAdapter, accountAdapter);
    }

    @WithMockUser(username = "user1")
    @Test
    public void loadLoggedInUserAccountInfo1() {
        Account expected = accountProcessor.loadLoggedInUserAccountInfo();
        Assert.assertEquals(actualFirstAccount, expected);
    }

    @WithMockUser(username = "admin")
    @Test
    public void loadLoggedInUserAccountInfo2() {
        Account expected = accountProcessor.loadLoggedInUserAccountInfo();
        Assert.assertEquals(actualSecondAccount, expected);
    }

    @WithAnonymousUser
    @Test(expected = NullPointerException.class)
    public void loadLoggedInUserAccountInfo3() {
        accountProcessor.loadLoggedInUserAccountInfo();
    }

    @Test
    public void loadAccountInfo1() {
        Account expected = accountProcessor.loadAccountInfo(1L);
        Assert.assertEquals(actualFirstAccount, expected);
    }

    @Test
    public void loadAccountInfo2() {
        Account expected = accountProcessor.loadAccountInfo(-1L);
        Assert.assertEquals(actualSecondAccount, expected);
    }

    @Test
    public void loadAccountInfo3() {
        Account expected = accountProcessor.loadAccountInfo(-5L);
        Assert.assertNull(expected);
    }

    @Test
    public void deposit1() {
        BigDecimal expected = accountProcessor.deposit(1L, new BigDecimal(2000));
        Assert.assertEquals(new BigDecimal(3000), expected);
    }

    @Test
    public void deposit2() {
        BigDecimal expected = accountProcessor.deposit(-1L, new BigDecimal(2000));
        Assert.assertEquals(new BigDecimal(3000), expected);
    }

    @Test(expected = NullPointerException.class)
    public void deposit3() {
        accountProcessor.deposit(-5L, new BigDecimal(2000));
    }

    @Test
    public void withdrawal() {
        BigDecimal expected = accountProcessor.withdrawal(1L, new BigDecimal(500));
        Assert.assertEquals(new BigDecimal(500), expected);
    }

    @Test
    public void withdrawal2() {
        BigDecimal expected = accountProcessor.withdrawal(-1L, new BigDecimal(500));
        Assert.assertEquals(new BigDecimal(500), expected);
    }

    @Test(expected = NullPointerException.class)
    public void withdrawal3() {
        accountProcessor.withdrawal(-5L, new BigDecimal(500));
    }
}
