package ua.test.mega.tester.rest.assured;

import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

@MegaAdmin
@RunWith(SpringRunner.class)
public class AccountControllerTest {

    @BeforeClass
    public static void setUp() {
        baseURI = "http://localhost:8080/api/account/";

        TestAnnotationRole annotationRole = new TestAnnotationRole();
        annotationRole.getRole(AccountControllerTest.class);
    }


    @Test
    public void shouldShowCurrentUserAccount1() {
        given()
                .auth().basic("user1", "user1")
                .contentType("application/json")
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .body("accountId[0]", equalTo(1))
                .log().body();
    }

    @Test
    public void shouldShowCurrentUserAccount2() {

        given()
                .contentType("application/json")
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .body("accountId[0]", equalTo(-1))
                .log().body();
    }

    @Test
    public void shouldShowUserAccount1() {
        given()
                .contentType("application/json")
                .when()
                .get("/1")
                .then()
                .statusCode(200)
                .body("accountId[0]", equalTo(1))
                .log().body();
    }


    @Test
    public void shouldShowUserAccount2() {
        given()
                .contentType("application/json")
                .when()
                .get("/2")
                .then()
                .statusCode(200)
                .body("accountId[0]", equalTo(2))
                .log().body();
    }

    @Test
    public void shouldAccessDenied() {
        given()
                .auth().preemptive().basic("user1", "user1")
                .contentType("application/json")
                .when()
                .get("/2")
                .then()
                .statusCode(403)
                .log().body();
    }

    @Test
    public void shouldIncreaseDeposit() {
        Response response =
                given()
                        .auth().preemptive().basic("admin", "password")
                        .contentType("application/json")
                        .when()
                        .get("/1/deposit/1000")
                        .then()
                        .statusCode(200)
                        .log().body()
                        .extract()
                        .response();

        assertEquals(response.body().print(), "1000");
    }

    @Test
    public void shouldAccessDenied2() {
        given()
                .auth().preemptive().basic("user1", "user1")
                .contentType("application/json")
                .when()
                .get("/1/deposit/1000")
                .then()
                .statusCode(403)
                .log().body();
    }

    @Test
    public void shouldWithdrawal() {
        Response response = given()
                .contentType("application/json")
                .when()
                .get("/1/withdrawal/1000")
                .then()
                .statusCode(200)
                .log().body()
                .extract()
                .response();

        assertEquals(response.body().print(), "0");
    }

    @Test
    public void shouldAccessDenied3() {
        given()
                .auth().preemptive().basic("123", "123")
                .contentType("application/json")
                .when()
                .get("/1/withdrawal/1000")
                .then()
                .statusCode(401)
                .log().body();
    }

}
