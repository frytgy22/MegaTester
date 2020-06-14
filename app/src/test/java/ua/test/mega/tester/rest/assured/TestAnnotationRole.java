package ua.test.mega.tester.rest.assured;

import io.restassured.RestAssured;

import static io.restassured.RestAssured.basic;

public class TestAnnotationRole {
    public void getRole(Class<?> aClass)  {

        if (aClass.isAnnotationPresent(MegaAdmin.class)) {
            MegaAdmin admin = aClass.getDeclaredAnnotation(MegaAdmin.class);
            String login = admin.login();
            String password = admin.password();

            RestAssured.authentication = basic(login, password);
        }
    }
}
