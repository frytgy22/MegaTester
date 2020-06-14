package ua.test.mega.tester.rest.assured;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import ua.test.mega.tester.rest.api.CurrencyDTO;
import ua.test.mega.tester.rest.api.OrderDTO;
import ua.test.mega.tester.rest.api.SideDTO;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.preemptive;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
public class OrderControllerTest {

    @Before
    public void setURI() {

        baseURI = "http://localhost:8080/api/order/";
        authentication = preemptive().basic("user1", "user1");
    }


    @Test
    public void shouldCreateOrder() {
        OrderDTO order = OrderDTO.builder()
                .baseCurrency(CurrencyDTO.EUR)
                .quoteCurrency(CurrencyDTO.EUR)
                .rate(1000.33)
                .side(SideDTO.BUY)
                .amount(new BigDecimal(10))
                .createDate(ZonedDateTime.of(2019, 3, 22, 2, 2, 2,
                        3, ZoneId.of("UTC")))
                .executionDate(ZonedDateTime.of(2019, 3, 22, 2, 2, 2,
                        3, ZoneId.of("UTC")))
                .build();


        given()
                .contentType("application/json")
                .body(order)
                .when()
                .post("/")
                .then()
                .statusCode(200)
                .log();
    }

    @Test
    public void shouldShowAllOrders() {
        given()
                .contentType("application/json")
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .body("baseCurrency[0]", equalTo("EUR"))
                .body("quoteCurrency[0]", equalTo("EUR"))
                .body("side[0]", equalTo("BUY"))
                .body("amount[0]", equalTo(10))
                .log().body();
    }

}
