package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UpdateUserDataTest {
    private CreateUser createUser;
    private UserMethods userMethods;
    String bearerToken;

    @Before
    public void setUp() {
        createUser = CreateUser.getRandom();
        userMethods = new UserMethods();
    }

    @Test
    @DisplayName("Проверка редактирования данных у авторизованного пользователя")
    @Description("Смена пароля")
    public void userInfoCanBeChangePasswordTest() {
        userMethods.create(createUser);
        ValidatableResponse login = userMethods.login(UserCreds.from(createUser));
        bearerToken = login.extract().path("accessToken");

        ValidatableResponse info = userMethods.userInfoChange(bearerToken, UserCreds.getUserWithRandomPassword());

        info.assertThat().statusCode(200);
        info.assertThat().body("success", equalTo(true));

    }

    @Test
    @DisplayName("Проверка редактирования данных у авторизованного пользователя")
    @Description("Смена email")
    public void userInfoCanBeChangeEmailTest() {
        userMethods.create(createUser); // Создание пользователя
        ValidatableResponse login = userMethods.login(UserCreds.from(createUser));
        bearerToken = login.extract().path("accessToken");

        ValidatableResponse info = userMethods.userInfoChange(bearerToken, UserCreds.getUserWithRandomEmail());

        info.assertThat().statusCode(200);
        info.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Проверка замены email на уже зарегистрированный email")
    @Description("Зарегистрированный email")
    public void userInfoCanNotBeChangeWithSameEmailTest (){
        userMethods.create(createUser); // Создание пользователя
        ValidatableResponse login = userMethods.login(UserCreds.from(createUser));
        bearerToken = login.extract().path("accessToken");

        ValidatableResponse info = userMethods.userInfoChange(bearerToken, UserCreds.getUserWithEmail(createUser));

        info.assertThat().statusCode(403);
        info.assertThat().body("success", equalTo(false));
        info.assertThat().body("message", equalTo("User with such email already exists"));
    }

    @Test
    @DisplayName("Проверка редактирование данных у неавторизованного пользователя")
    @Description("Изменение пароля")
    public void userInfoCanNotBeChangePasswordTest() {
        bearerToken = "";

        ValidatableResponse info = userMethods.userInfoChange(bearerToken, UserCreds.getUserWithRandomPassword());

        info.assertThat().statusCode(401);
        info.assertThat().body("success", equalTo(false));
        info.assertThat().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Редактирование данных у неавторизованного пользователя")
    @Description("Изменение email")
    public void userInfoCanNotBeChangeEmailTest() {
        bearerToken = "";

        ValidatableResponse info = userMethods.userInfoChange(bearerToken, UserCreds.getUserWithRandomEmail());

        info.assertThat().statusCode(401);
        info.assertThat().body("success", equalTo(false));
        info.assertThat().body("message", equalTo("You should be authorised"));
    }
}
