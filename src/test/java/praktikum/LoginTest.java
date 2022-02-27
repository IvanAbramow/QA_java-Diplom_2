package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LoginTest {
    private UserMethods userMethods;
    private CreateUser createUser;

    @Before
    public void setUp() {
        userMethods = new UserMethods();
        createUser = CreateUser.getRandom();
    }

    @After
    public void tearDown() {
        userMethods.delete();
    }

    @Test
    @DisplayName("Проверка авторизации пользователя")
    @Description("/api/auth/login")
    public void checkUserLogin(){
        userMethods.create(createUser);

        ValidatableResponse validatableResponse = userMethods.login(UserCreds.from(createUser));
        String updateToken = validatableResponse.extract().path("updateToken");

        assertThat("Courier ID incorrect", updateToken, is(not(0)));
        validatableResponse.assertThat().statusCode(200);
        validatableResponse.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Проверка авторизации без email")
    @Description("/api/auth/login")
    public void checkUserLoginWithoutUserName(){
        userMethods.create(createUser);

        ValidatableResponse validatableResponse = userMethods.login(new UserCreds(null, createUser.password));

        validatableResponse.assertThat().statusCode(401);
        validatableResponse.assertThat().body("success", equalTo(false));
        validatableResponse.assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Проверка авторизации без пароля")
    @Description("/api/auth/login")
    public void checkUserLoginNull(){
        userMethods.create(createUser);

        ValidatableResponse validatableResponse = userMethods.login(new UserCreds(createUser.email,null));

        validatableResponse.assertThat().statusCode(401);
        validatableResponse.assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Проверка авторизации с некорректным логином")
    @Description("/api/auth/login")
    public void checkLoginWithInvalidEmail(){
        userMethods.create(createUser);

        ValidatableResponse validatableResponse = userMethods.login(new UserCreds("111", createUser.password));

        validatableResponse.assertThat().statusCode(401);
        validatableResponse.assertThat().body("success", equalTo(false));
        validatableResponse.assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Проверка авторизации с некорректным паролем")
    @Description("/api/auth/login")
    public void checkLoginWithInvalidPassword(){
        userMethods.create(createUser);

        ValidatableResponse validatableResponse = userMethods.login(new UserCreds(createUser.email,"111"));

        validatableResponse.assertThat().statusCode(401);
        validatableResponse.assertThat().body("message", equalTo("email or password are incorrect"));
    }
}
