package praktikum;
import org.apache.commons.lang3.RandomStringUtils;

public class CreateUser {
    public final String email;
    public final String password;
    public final String name;

    public CreateUser(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static CreateUser getRandom() {
        return CreateUser.getRandom(true, true, true);
    }

    public static CreateUser getRandom(boolean useEmail, boolean usePassword, boolean useName) {

        String password = "";
        String email = "";
        String name = "";

        if (useEmail) {
            email = RandomStringUtils.randomAlphabetic(10)+"@yandex.ru";
        }
        if (usePassword) {
            password = RandomStringUtils.randomAlphabetic(10);
        }
        if (useName) {
            name = RandomStringUtils.randomAlphabetic(10);
        }
        return new CreateUser(email, password, name);
    }

    @Override
    public String toString() {
        return String.format("Пользователь: { Имя:%s, Email:%s, Пароль:%s }", this.name, this.email, this.password);
    }
}
