package ua.test.mega.tester.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.test.mega.tester.core.AccountProcessor;
import ua.test.mega.tester.core.OrderManagementProcessor;
import ua.test.mega.tester.core.OrderProcessor;
import ua.test.mega.tester.core.api.model.Account;
import ua.test.mega.tester.core.api.model.Currency;
import ua.test.mega.tester.core.api.model.Order;
import ua.test.mega.tester.core.api.model.Side;
import ua.test.mega.tester.rest.api.CurrencyDTO;
import ua.test.mega.tester.rest.api.OrderDTO;
import ua.test.mega.tester.rest.api.SideDTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order/")
public class OrderRestController {

    private final OrderManagementProcessor orderManagementProcessor;
    private final AccountProcessor accountProcessor; // TODO added to get accountId
    private final OrderProcessor orderProcessor;

    @Autowired
    public OrderRestController(OrderManagementProcessor orderManagementProcessor, AccountProcessor accountProcessor, OrderProcessor orderProcessor) {
        this.orderManagementProcessor = orderManagementProcessor;
        this.accountProcessor = accountProcessor;
        this.orderProcessor = orderProcessor;
    }

    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OrderDTO> getOrders() {

        List<Order> orders = orderProcessor.findOrdersForLoffedinUser();

        return orders.stream()
                .map(this::toOrderDTO)
                .collect(Collectors.toList());
    }

    private OrderDTO toOrderDTO(Order order) {
        return OrderDTO.builder()
                .orderId(order.getOrderId())
                .baseCurrency(toCurrencyDTO(order.getBaseCurrency()))
                .quoteCurrency(toCurrencyDTO(order.getQuoteCurrency()))
                .rate(order.getOrderId())
                .side(toSideDTO(order.getSide()))
                .amount(order.getAmount())
                .createDate(order.getCreateDate())// TODO addede execution  (nonNull field)
                .executionDate(order.getExecutionDate())
                .build();
    }

    private CurrencyDTO toCurrencyDTO(Currency quoteCurrency) {
        switch (quoteCurrency) {
            case EUR:
                return CurrencyDTO.EUR;
            case USD:
                return CurrencyDTO.USD;
            case BTC:
                return CurrencyDTO.BTC;
            case ETH:
                return CurrencyDTO.ETH;
            default:
                throw new IllegalArgumentException("Unexacting currency was received.");
        }
    }

    private SideDTO toSideDTO(Side side) {
        switch (side) {
            case BUY:
                return SideDTO.BUY;
            case SELL:
                return SideDTO.SELL;
            default:
                throw new IllegalArgumentException("Unexacting state was received.");
        }
    }

    @PostMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderDTO placeOrder(@RequestBody OrderDTO order) {
        Account account = accountProcessor.loadLoggedInUserAccountInfo();//return info current user

        Order orderModel = toOrderModel(order, account);

        Order savedOrder = orderManagementProcessor.placeOrder(orderModel);

        return toOrderDTO(savedOrder);
    }

    private Order toOrderModel(OrderDTO order, Account account) {//TODO added account
        return Order.builder()
                .orderId(order.getOrderId())
                .accountId(account.getAccountId())//TODO added this field (nonNull)
                .baseCurrency(toCurrency(order.getBaseCurrency()))
                .quoteCurrency(toCurrency(order.getQuoteCurrency()))
                .rate(order.getRate())
                .side(toSide(order.getSide()))
                .amount(order.getAmount())
                .createDate(order.getCreateDate())// TODO added execution  (nonNull field)
                .executionDate(order.getExecutionDate())
                .build();
    }

    private Currency toCurrency(CurrencyDTO baseCurrency) {
        switch (baseCurrency) {
            case EUR:
                return Currency.EUR;
            case USD:
                return Currency.USD;
            case BTC:
                return Currency.BTC;
            case ETH:
                return Currency.ETH;
            default:
                throw new IllegalArgumentException("Unexacting currency was received.");
        }
    }

    private Side toSide(SideDTO side) {
        switch (side) {
            case BUY:
                return Side.BUY;
            case SELL:
                return Side.SELL;
            default:
                throw new IllegalArgumentException("Unexacting state was received.");
        }
    }
}
