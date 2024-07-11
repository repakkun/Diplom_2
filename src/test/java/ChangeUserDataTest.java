import helpers.user.UserChangeClient;
import helpers.user.UserChecks;
import helpers.user.UserCreateClient;
import helpers.user.UserDeleteClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import pojo.user.UserChange;
import pojo.user.UserCreate;
import com.github.javafaker.Faker;

public class ChangeUserDataTest extends BaseTest {

    private final UserChangeClient userChangeAuto = new UserChangeClient();
    private static final Faker faker = new Faker();

    @Test
    @DisplayName("Изменение данных для авт. пользователя")
    public void changeAutoUser() {
        UserCreate user = UserCreate.random();
        createUserAndSetToken(user);

        UserChange userChange = new UserChange("Ilya" + faker.name().firstName() + "@ya.ru", "Test1" + faker.name().lastName());
        Response responseUserChange = userChangeAuto.userChangeAuto(token, userChange);
        Assert.assertTrue(checkUser.userAutoChange(responseUserChange));
    }

    @Test
    @DisplayName("Изменение данных для неавт пользователя")
    public void changeNotAutoUser() {
        UserChange userChange = new UserChange("Ilya" + faker.name().firstName() + "@mail.ru", "Test2" + faker.name().lastName());
        Response responseUserChange = userChangeAuto.userChangeNotAuto(userChange);
        Assert.assertEquals("You should be authorised", checkUser.userNotAutoChange(responseUserChange));
    }
}
