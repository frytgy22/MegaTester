//package ua.test.mega.tester.junit;
//
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit4.SpringRunner;
//import reactor.core.publisher.Flux;
//import reactor.test.StepVerifier;
//import ua.test.mega.tester.adapters.LoggedInUserAdapterForSpringSecurity;
//import ua.test.mega.tester.adapters.NotificationReactiveStreamAdapter;
//import ua.test.mega.tester.adapters.UserAdapterInMemory;
//import ua.test.mega.tester.core.NotificationFactory;
//import ua.test.mega.tester.core.NotificationProcessor;
//import ua.test.mega.tester.core.api.LoggedInUserAdapter;
//import ua.test.mega.tester.core.api.NotificationAdapter;
//import ua.test.mega.tester.core.api.UserAdapter;
//import ua.test.mega.tester.core.api.model.*;
//
//import java.math.BigDecimal;
//import java.time.ZonedDateTime;
//
//@RunWith(SpringRunner.class)
//public class NotificationProcessorTest {
//
//    private static UserAdapter userAdapter;
//    private NotificationProcessor notificationProcessor;
//    private NotificationAdapter notificationAdapter;
//
//    @BeforeClass
//    public static void init() {
//        userAdapter = new UserAdapterInMemory();
//    }
//
//    @Before
//    public void setUp() {
//        LoggedInUserAdapter loggedInUserAdapter = new LoggedInUserAdapterForSpringSecurity(userAdapter);
//        notificationAdapter = new NotificationReactiveStreamAdapter();
//
//        notificationProcessor = new NotificationProcessor(loggedInUserAdapter, notificationAdapter);
//    }
//
//    @WithMockUser(username = "admin")
//    @Test
//    public void provideNotificationsForLoggedinUser() {//TODO fix it
//
//        Order order = Order.builder()
//                .accountId(-1L)
//                .baseCurrency(Currency.UAH)
//                .quoteCurrency(Currency.EUR)
//                .rate(10)
//                .side(Side.BUY)
//                .amount(new BigDecimal(100))
//                .createDate(ZonedDateTime.now())
//                .executionDate(ZonedDateTime.now())
//                .build();
//
//        Position position = Position.builder()
//                .accountId(-1)
//                .orderId(order.getOrderId())
//                .executionDate(ZonedDateTime.now())
//                .priceInUSD(100)
//                .build();
//
//        notificationAdapter.register(NotificationFactory.newPositionCreated(order, position));
//
//        Flux<Notification> notificationFlux = notificationProcessor.provideNotificationsForLoggedinUser();
//        //notificationFlux.subscribe(s -> System.out.println(s.getOrderId()));
//
//       // Flux<Notification> notification = notificationProcessor.provideAllNotifications();
//
//        StepVerifier
//                .create(notificationFlux)
//                .expectSubscription()
//                .expectComplete()
//                .verify();
//
//    }
//
//    @WithMockUser(username = "user1")
//    @Test
//    public void provideAllNotifications() {
//        Order order = Order.builder()
//                .accountId(1L)
//                .baseCurrency(Currency.UAH)
//                .quoteCurrency(Currency.EUR)
//                .rate(10)
//                .side(Side.BUY)
//                .amount(new BigDecimal(100))
//                .createDate(ZonedDateTime.now())
//                .executionDate(ZonedDateTime.now())
//                .build();
//
//        Position position = Position.builder()
//                .accountId(1)
//                .orderId(order.getOrderId())
//                .executionDate(ZonedDateTime.now())
//                .priceInUSD(100)
//                .build();
//
//        Notification notification = Notification.builder()
//                .message("123")
//                .type(NotificationType.USER)
//                .userId(1L)
//                .error(new Throwable())
//                .orderId(order.getOrderId())
//                .positionId(position.getPositionId())
//                .build();
//
//        notificationAdapter.register(notification);
//
//        Flux<Notification> notificationFlux = notificationProcessor.provideAllNotifications();
//        notificationFlux.subscribe(s -> System.out.println(s.getOrderId()));
//
//    }
//}
