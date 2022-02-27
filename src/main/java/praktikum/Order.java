package praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class Order extends RestAssuredClient{
    private static final String ORDER_PATH = "api/orders";

    @Step("Создание заказа")
    public ValidatableResponse createOrder (String token, Ingredients ingredients){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token.replace("Bearer ", ""))
                .body(ingredients)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step ("Список заказов")
    public ValidatableResponse userOrders (){
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH + "/all")
                .then();
    }

    @Step ("Список заказов пользователя")
    public ValidatableResponse userOrdersList (String token){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token.replace("Bearer ", ""))
                .when()
                .get(ORDER_PATH)
                .then();
    }
}

