import helpers.user.UserLoginClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Test;
import pojo.user.UserCreate;
import pojo.user.UserLogin;

public class LoginUserTest extends BaseTest {

    private final UserLoginClient clientLogin = new UserLoginClient();

    @Test
    @DisplayName("Логин уникального пользователя")
    public void loginUniqueUserTest() {
        UserCreate user = UserCreate.random();
        createUserAndSetToken(user);

        String userEmail = user.getEmail();
        String userPassword = user.getPassword();
        UserLogin userLogin = new UserLogin(userEmail, userPassword);

        ValidatableResponse loginResponse = clientLogin.userLogin(userLogin);
        Assert.assertTrue(checkUser.loginSuccessfully(loginResponse));
    }

    @Test
    @DisplayName("Неправильный логин пользователя")
    public void failLoginUniqueUserTest() {
        UserLogin userLogin = new UserLogin("null", "null");
        ValidatableResponse loginResponse = clientLogin.userLogin(userLogin);

        Assert.assertFalse(checkUser.loginFailed(loginResponse));
    }
}
