package praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserMethods extends RestAssuredClient {
    private static final String USER_PATH = "/api/auth";

    @Step("Создание пользователя")
    public ValidatableResponse create(CreateUser user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "/register")
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse login (UserCreds userCreds) {
        return given()
                .spec(getBaseSpec())
                .body(userCreds)
                .when()
                .post(USER_PATH + "/login")
                .then();
    }

    @Step("Разлогин пользователя")
    public ValidatableResponse exit (String refreshToken) {
        return given()
                .spec(getBaseSpec())
                .body(refreshToken)
                .when()
                .post(USER_PATH + "/logout")
                .then();
    }

    @Step ("Удаление пользователя")
    public void delete() {
        if (Token.getAccessToken() == null) {
            return;
        }
        given()
                .spec(getBaseSpec())
                .auth().oauth2(Token.getAccessToken())
                .when()
                .delete(USER_PATH)
                .then()
                .statusCode(202);
    }

    @Step ("Информация о пользователе")
    public ValidatableResponse userInfo(String token) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token.replace("Bearer ", ""))
                .when()
                .get(USER_PATH + "/user")
                .then();
    }

    @Step ("Изменение информации о пользователе")
    public ValidatableResponse userInfoChange(String token, UserCreds userCreds) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token.replace("Bearer ", ""))
                .body(userCreds)
                .when()
                .patch(USER_PATH + "/user")
                .then();
    }
}
