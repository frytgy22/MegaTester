package ua.test.mega.tester;

import static io.restassured.RestAssured.basic;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import io.restassured.RestAssured;
import ua.test.mega.tester.rest.assured.MegaAdmin;
import ua.test.mega.tester.rest.assured.MegaUser;

public class MegaAuthRule extends TestWatcher {

    @Override
    protected void starting(Description description) {
        MegaAdmin megaAdminOnMethod = description.getAnnotation(MegaAdmin.class);
        MegaAdmin megaAdminOnClass = description.getTestClass().getDeclaredAnnotation(MegaAdmin.class);
        MegaAdmin currentMegaAdmin = null == megaAdminOnMethod ? megaAdminOnClass : megaAdminOnMethod;

        MegaUser megaUserOnMethod = description.getAnnotation(MegaUser.class);
        MegaUser megaUserOnClass = description.getTestClass().getDeclaredAnnotation(MegaUser.class);
        MegaUser currentMegaUser = null == megaUserOnMethod ? megaUserOnClass : megaUserOnMethod;

        if (null != currentMegaAdmin) {
            String login = currentMegaAdmin.login();
            String password = currentMegaAdmin.password();

            RestAssured.authentication = RestAssured.basic(login, password);
        }

		if (null != currentMegaUser) {
			String login = currentMegaUser.login();
			String password = currentMegaUser.password();

			RestAssured.authentication = RestAssured.basic(login, password);
		}
    }

    @Override
    protected void finished(Description description) {
        RestAssured.authentication = RestAssured.DEFAULT_AUTH;
    }
}
