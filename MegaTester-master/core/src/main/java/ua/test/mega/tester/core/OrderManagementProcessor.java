package ua.test.mega.tester.core;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import ua.test.mega.tester.core.api.AccountAdapter;
import ua.test.mega.tester.core.api.LoggedInUserAdapter;
import ua.test.mega.tester.core.api.NotificationAdapter;
import ua.test.mega.tester.core.api.OrderAdapter;
import ua.test.mega.tester.core.api.PositionAdapter;
import ua.test.mega.tester.core.api.model.Account;
import ua.test.mega.tester.core.api.model.Order;
import ua.test.mega.tester.core.api.model.Position;
import ua.test.mega.tester.core.api.model.Side;
import ua.test.mega.tester.core.exceptions.InconsistentOrderAmount;
import ua.test.mega.tester.core.exceptions.InconsistentOrderParameters;
import ua.test.mega.tester.core.exceptions.NotEnoughMoney;

@Component
public class OrderManagementProcessor {

    private static final double COMMISSION = 0.05;

    private OrderAdapter orderAdapter;
    private PositionAdapter positionAdapter;
    private AccountAdapter accountAdapter;
    private NotificationAdapter notificationAdapter;
    private LoggedInUserAdapter loggedInUserAdapter;

    @Autowired
    public OrderManagementProcessor(
            OrderAdapter orderAdapter,
            PositionAdapter positionAdapter,
            AccountAdapter accountAdapter,
            NotificationAdapter notificationAdapter,
            LoggedInUserAdapter loggedInUserAdapter) {

        this.orderAdapter = orderAdapter;
        this.positionAdapter = positionAdapter;
        this.accountAdapter = accountAdapter;
        this.notificationAdapter = notificationAdapter;
        this.loggedInUserAdapter = loggedInUserAdapter;
    }

    public Order placeOrder(Order order) {
        validateInOrder(order);

        Order orderWithAccountId = prepareOrderWithAccountId(order);

        //create
        Order savedOrder = orderAdapter.create(orderWithAccountId);
        System.out.println(savedOrder);

        //run calculation of the position and  account balance
        recalculateAccountBalanceAsynchronously(savedOrder);

        return savedOrder;
    }

    private void validateInOrder(Order order) {
        if (0 == order.getAccountId()
                || null == order.getBaseCurrency()
                || null == order.getQuoteCurrency()
                || null == order.getSide()
                || null == order.getAmount()) {
            throw new InconsistentOrderParameters();
        }

        if (order.getAmount().compareTo(BigDecimal.ZERO) <= 0) {//TODO fixed >=
            throw new InconsistentOrderAmount();
        }

    }

    private Order prepareOrderWithAccountId(Order order) {
        long accountId = loggedInUserAdapter.getLoggedInUser().getAccountId();

        return order.toBuilder()
                .accountId(accountId)
                .build();
    }

    private void recalculateAccountBalanceAsynchronously(Order savedOrder) {
        Flux.just(savedOrder)
                .delayElements(Duration.ofMillis(100))
                .map(this::createPosition)

                .delayElements(Duration.ofMillis(100))
                .doOnNext(this::calculateMoneyMovementAndSave)

                .delayElements(Duration.ofMillis(100))
                .doOnNext(this::calculateCommissionAndSave)

                .delayElements(Duration.ofMillis(100))
                .subscribe(
                        position -> notificationAdapter.register(NotificationFactory.newPositionCreated(savedOrder, position)),
                        error -> notificationAdapter.register(NotificationFactory.errorOnOrderProcessing(error, savedOrder))
                );
    }

    private Position createPosition(Order savedOrder) {
        Position newPosition = newPosition(savedOrder);
        return positionAdapter.create(newPosition);
    }

    private void calculateMoneyMovementAndSave(Position position) {

        long accountId = position.getAccountId();
        Order order = orderAdapter.find(position.getOrderId());
        double priceInUSD = Side.BUY == order.getSide()
                ? -position.getPriceInUSD()
                : position.getPriceInUSD();

        updateAccoutnBalance(new BigDecimal(priceInUSD), accountId);
    }

    private void updateAccoutnBalance(BigDecimal priceInUSD, long accountId) {
        Account account = accountAdapter.find(accountId);

        BigDecimal previousBalanceInUSD = account.getBalanceInUSD();
        BigDecimal newBalanceInUSD = previousBalanceInUSD.add(priceInUSD);

        validateBalance(newBalanceInUSD);

        Account updatedAccount = account.toBuilder()
                .balanceInUSD(newBalanceInUSD)
                .build();

        accountAdapter.update(updatedAccount);
    }

    private void validateBalance(BigDecimal newBalanceInUSD) {
        if (newBalanceInUSD.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NotEnoughMoney();
        }
    }

    private void calculateCommissionAndSave(Position position) {
        long accountId = position.getAccountId();
        double priceInUSD = position.getPriceInUSD();
        double commissionInUSD = -priceInUSD * COMMISSION;

        updateAccoutnBalance(new BigDecimal(commissionInUSD), accountId);
    }

    private Position newPosition(Order order) {
        return Position.builder()
                .orderId(order.getOrderId())
                .accountId(order.getAccountId())
                .executionDate(ZonedDateTime.now())
                .priceInUSD(order.getRate())
                .build();
    }
}
