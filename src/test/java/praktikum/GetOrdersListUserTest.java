package praktikum;

import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.*;

public class GetOrdersListUserTest {
    private CreateUser createUser;
    private UserMethods userMethods;
    public Order order;
    String bearerToken;

    @Before
    public void setUp() {
        createUser = CreateUser.getRandom();
        userMethods = new UserMethods();
        Ingredients ingredients = Ingredients.getRandomBurger();
        order = new Order();
    }

    @Test
    @Description("Проверка получения списка заказов авторизованного пользователя")
    public void orderUserInfoCanBeGetAuthUser (){
        userMethods.create(createUser);
        ValidatableResponse login = userMethods.login(UserCreds.from(createUser));
        bearerToken = login.extract().path("accessToken");

        ValidatableResponse orderInfo = order.userOrdersList(bearerToken);
        List<Map<String, Object>> ordersList = orderInfo.extract().path("orders");

        orderInfo.assertThat().statusCode(200);
        orderInfo.assertThat().body("success", equalTo(true));
        assertThat("Orders list empty", ordersList, is(not(0)));
    }

    @Test
    @Description("Проверка получения списка заказов неавторизованного пользователя")
    public void orderUserInfoCantBeGetNonAuthUser (){
        bearerToken = "";

        ValidatableResponse orderInfo = order.userOrdersList(bearerToken);

        orderInfo.assertThat().statusCode(401);
        orderInfo.assertThat().body("success", equalTo(false));
        orderInfo.assertThat().body("message", equalTo("You should be authorised"));
    }
}
