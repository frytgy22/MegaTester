//package ua.test.mega.tester.rest.assured;
//
//import io.restassured.http.ContentType;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.reactive.server.WebTestClient;
//import org.springframework.test.web.servlet.MockMvc;
//import ua.test.mega.tester.core.NotificationProcessor;
//import ua.test.mega.tester.rest.api.CurrencyDTO;
//import ua.test.mega.tester.rest.api.NotificationDTO;
//import ua.test.mega.tester.rest.api.OrderDTO;
//import ua.test.mega.tester.rest.api.SideDTO;
//
//import java.math.BigDecimal;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//
//import static io.restassured.RestAssured.given;
//import static org.awaitility.Awaitility.await;
//import static org.hamcrest.Matchers.equalTo;
//
//
//@RunWith(SpringRunner.class)
//@WebFluxTest(NotificationDTO.class)
//public class NotificationControllerTest {
//
//    @Autowired
//    NotificationProcessor notificationProcessor;
//
//    public void test() {
//
//        OrderDTO order = OrderDTO.builder()
//                .baseCurrency(CurrencyDTO.EUR)
//                .quoteCurrency(CurrencyDTO.EUR)
//                .rate(1000.33)
//                .side(SideDTO.BUY)
//                .amount(new BigDecimal(10))
//                .createDate(ZonedDateTime.of(2019, 3, 22, 2, 2, 2,
//                        3, ZoneId.of("UTC")))
//                .executionDate(ZonedDateTime.of(2019, 3, 22, 2, 2, 2,
//                        3, ZoneId.of("UTC")))
//                .build();
//
//
//        given()
//                .auth().preemptive().basic("admin", "password")
//                .contentType("application/json")
//                .body(order)
//                .when()
//                .post("http://localhost:8080/api/order/")
//                .then()
//                .statusCode(200)
//                .log();
//
//
//        given()
//                .auth().preemptive().basic("admin", "password")
//                .when()
//                .get("http://localhost:8080/api/account/api/notification/all");
//
//
//        await()
//                .until(notificationProcessor::provideAllNotifications, equalTo("FluxProcessor"));
//    }
//
//}
