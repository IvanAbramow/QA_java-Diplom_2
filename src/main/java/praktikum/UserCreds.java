package praktikum;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.javafaker.Faker;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCreds {
    public static Faker faker = new Faker();
    public String email;
    public String password;

    public UserCreds() {}

    public UserCreds(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserCreds from(CreateUser user) {
        return new UserCreds(user.email, user.password);
    }

    public UserCreds setEmail (String email){
        this.email = email;
        return this;
    }

    public UserCreds setPassword(String password) {
        this.password = password;
        return this;
    }

    public static UserCreds getUserWithEmail (CreateUser user) {
        return new UserCreds().setEmail(user.email);
    }

    public static UserCreds getUserWithRandomEmail () {
        return new UserCreds().setEmail(faker.internet().emailAddress());
    }

    public static UserCreds getUserWithRandomPassword() {
        return new UserCreds().setPassword(faker.internet().password());
    }

    @Override
    public String toString() {
        return String.format("Пользователь { Email:%s, Пароль:%s }", this.email, this.password);
    }
}
