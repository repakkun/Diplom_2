package helpers.user;

import helpers.Client;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.user.UserChange;

public class UserChangeClient extends Client {
    public static final String USER_CHANGE_PATH = "auth/user";

    @Step("Change email authorization user")
    public Response userChangeAuto(String token, UserChange userChage) {
    return spec().auth().oauth2(token.replace("Bearer ","")).body(userChage)
            .patch(USER_CHANGE_PATH);
    }

    @Step("Change email not authorization user")
    public Response userChangeNotAuto(UserChange userChange) {
        return spec().body(userChange).patch(USER_CHANGE_PATH);
    }
}