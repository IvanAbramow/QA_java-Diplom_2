package praktikum;

public class Token {

    public static String accessToken;
    public static String updateToken;

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        Token.accessToken = accessToken;
    }

    public static String getUpdateToken() {
        return updateToken;
    }

    public static void setUpdateToken(String updateToken) {
        Token.updateToken = updateToken;
    }
}

