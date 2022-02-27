package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;


public class CreateUserTest {
    private UserMethods userMethods;

    @Before
    public void setUp() {
        userMethods = new UserMethods();
    }

    @Test
    @DisplayName("Проверка регистрации пользователя")
    @Description("/api/auth/register")
    public void checkUserCanBeCreated(){
        CreateUser createUser = CreateUser.getRandom();

        ValidatableResponse validatableResponse = userMethods.create(createUser);

        validatableResponse.assertThat().statusCode(200);
        validatableResponse.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Проверка регистрации зарегистрированного пользователя")
    @Description("/api/auth/register")
    public void checkUserCannotIdenticalTwiceCreated(){
        CreateUser createUser = CreateUser.getRandom();

        userMethods.create(createUser);
        ValidatableResponse validatableResponse = userMethods.create(createUser);

        validatableResponse.assertThat().statusCode(403);
        validatableResponse.assertThat().body("success", equalTo(false));
        validatableResponse.assertThat().body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Проверка регистрации пользователя без пароля")
    @Description("/api/auth/register")
    public void checkCreateUserWithoutPassword() {
        CreateUser user = CreateUser.getRandom(true,false, true);

        ValidatableResponse validatableResponse = userMethods.create(user);

        validatableResponse.assertThat().statusCode(403);
        validatableResponse.assertThat().body("success", equalTo(false));
        validatableResponse.assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Проверка регистрации пользователя без имени")
    @Description("/api/auth/register")
    public void checkCreateUserWithoutName() {
        CreateUser createUser = CreateUser.getRandom(true,true, false);

        ValidatableResponse validatableResponse = userMethods.create(createUser);

        validatableResponse.assertThat().statusCode(403);
        validatableResponse.assertThat().body("success", equalTo(false));
        validatableResponse.assertThat().body("message", equalTo("Email, password and name are required fields"));
    }
}
