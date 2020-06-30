package ua.test.mega.tester;

import static io.restassured.RestAssured.basic;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import io.restassured.RestAssured;
import ua.test.mega.tester.rest.assured.MegaAdmin;

public class MegaAuthRule extends TestWatcher {

	@Override
	protected void starting(Description description) {
		MegaAdmin megaAdminOnMethod = description.getAnnotation(MegaAdmin.class);
		MegaAdmin megaAdminOnClass = description.getTestClass().getDeclaredAnnotation(MegaAdmin.class);

		MegaAdmin currentMegaAdmin = null == megaAdminOnMethod ? megaAdminOnClass : megaAdminOnMethod;

		if (null != currentMegaAdmin) {
			String login = currentMegaAdmin.login();
			String password = currentMegaAdmin.password();

			RestAssured.authentication = RestAssured.basic(login, password);
		}
	}

	@Override
	protected void finished(Description description) {
		RestAssured.authentication = RestAssured.DEFAULT_AUTH;
	}
}
