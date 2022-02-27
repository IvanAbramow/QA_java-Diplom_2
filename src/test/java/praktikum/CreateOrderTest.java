package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CreateOrderTest {
    private CreateUser createUser;
    private UserMethods userMethods;
    private Ingredients ingredients;
    public Order order;
    String bearerToken;

    @Before
    public void setUp() {
        createUser = CreateUser.getRandom();
        userMethods = new UserMethods();
        ingredients = Ingredients.getRandomBurger();
        order = new Order();
    }

    @Test
    @DisplayName("Проверка создания заказа для зарегистрированного пользователя")
    @Description("/api/orders")
    public void orderCanBeCreatedRegisteredUser (){
        ValidatableResponse userResponse = userMethods.create(createUser);
        bearerToken = userResponse.extract().path("accessToken");

        ValidatableResponse orderResponse = order.createOrder(bearerToken, ingredients);
        int orderNumber = orderResponse.extract().path("order.number");

        orderResponse.assertThat().statusCode(200);
        orderResponse.assertThat().body("success", equalTo(true));
        assertThat("The order number is missing", orderNumber, is(not(0)));
    }

    @Test
    @DisplayName("Проверка создания заказа для незарегистрированного пользователя")
    @Description("/api/orders")
    public void orderCanBeCreatedNonRegisteredUser (){
        bearerToken = "";
        ValidatableResponse orderResponse = order.createOrder(bearerToken,ingredients);
        int orderNumber = orderResponse.extract().path("order.number");

        orderResponse.assertThat().statusCode(200);
        orderResponse.assertThat().body("success", equalTo(true));
        assertThat("The order number is missing", orderNumber, is(not(0)));
    }

    @Test
    @DisplayName ("Проверка создания заказа без ингредиентов")
    @Description("/api/orders")
    public void orderCanNotBeCreatedWithOutIngredients (){
        ValidatableResponse userResponse = userMethods.create(createUser);
        bearerToken = userResponse.extract().path("accessToken");

        ValidatableResponse orderResponse = order.createOrder(bearerToken,Ingredients.getNullIngredients());

        orderResponse.assertThat().statusCode(400);
        orderResponse.assertThat().body("success", equalTo(false));
        orderResponse.assertThat().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName ("Проверка создания заказа с некорректными ингредиентами")
    @Description("/api/orders")
    public void orderCanNotBeCreatedWithIncorrectIngredients (){
        ValidatableResponse userResponse = userMethods.create(createUser);
        bearerToken = userResponse.extract().path("accessToken");

        ValidatableResponse orderResponse = order.createOrder(bearerToken,Ingredients.getIncorrectIngredients());

        orderResponse.assertThat().statusCode(500);
    }
}
