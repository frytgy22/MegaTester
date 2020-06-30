package ua.test.mega.tester.rest.assured;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import io.restassured.response.Response;
import ua.test.mega.tester.MegaAuthRule;


public class AccountControllerTest {

	@Rule
	public final MegaAuthRule megaAuthRule = new MegaAuthRule();

	@BeforeClass
	public static void setUp() {
		baseURI = "http://localhost:8080/api/account/";

	}

	@MegaUser
	@Test
	public void shouldShowCurrentUserAccount1() {
		given()
				.contentType("application/json")
				.when()
				.get("/")
				.then()
				.statusCode(200)
				.body("accountId[0]", equalTo(1))
				.log().body();
	}

	@MegaAdmin
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

	@MegaAdmin
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

	@MegaAdmin
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

	@MegaUser
	@Test
	public void shouldAccessDenied() {
		given()
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

		assertEquals(response.body().print(), "2000");
	}

	@MegaUser
	@Test
	public void shouldAccessDenied2() {
		given()
				.contentType("application/json")
				.when()
				.get("/1/deposit/1000")
				.then()
				.statusCode(403)
				.log().body();
	}

	@Test
	public void shouldBeWithStatus401() {
		given()
				.contentType("application/json")
				.when()
				.get("/1/deposit/1000")
				.then()
				.statusCode(401)
				.log().body();
	}

	@MegaAdmin
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

		assertEquals(response.body().print(), "1000");
	}

	@MegaAdmin(login = "123",password ="123" )
	@Test
	public void shouldAccessDenied3() {
		given()
				.contentType("application/json")
				.when()
				.get("/1/withdrawal/1000")
				.then()
				.statusCode(401)
				.log().body();
	}
}
