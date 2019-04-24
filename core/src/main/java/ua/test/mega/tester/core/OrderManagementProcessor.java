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

		Order orderWithAccountId = prepareOrderWithAccountId(order);

		//create
		Order savedOrder = orderAdapter.create(orderWithAccountId);

		//run calculation of the position and  account balance
		recalculateAccountBalanceAsynchronously(savedOrder);

		return savedOrder;
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
						position -> notificationAdapter.register(NotificationFactory.newPositionCreated(position)),
						error -> notificationAdapter.register(NotificationFactory.errorOnOrderProcessing(error, savedOrder))
				);
	}

	private Position createPosition(Order savedOrder) {
		Position newPosition = newPosition(savedOrder);
		return positionAdapter.create(newPosition);
	}

	private void calculateMoneyMovementAndSave(Position position) {

		Order order = orderAdapter.find(position.getOrderId());
		double priceInUSD = Side.BUY == order.getSide()
				? -position.getPriceInUSD()
				: position.getPriceInUSD();

		Account account = accountAdapter.find(position.getAccountId());

		BigDecimal previousBalanceInUSD = account.getBalanceInUSD();
		BigDecimal newBalanceInUSD = previousBalanceInUSD.add(new BigDecimal(priceInUSD));

		Account updatedAccount = account.toBuilder()
				.balanceInUSD(newBalanceInUSD)
				.build();

		accountAdapter.update(updatedAccount);
	}

	private void calculateCommissionAndSave(Position position) {

		double priceInUSD = position.getPriceInUSD();

		double commissionInUSD = -priceInUSD * COMMISSION;

		Account account = accountAdapter.find(position.getAccountId());

		BigDecimal previousBalanceInUSD = account.getBalanceInUSD();
		BigDecimal newBalanceInUSD = previousBalanceInUSD.add(new BigDecimal(commissionInUSD));

		Account updatedAccount = account.toBuilder()
				.balanceInUSD(newBalanceInUSD)
				.build();

		accountAdapter.update(updatedAccount);
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
