import helpers.user.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import pojo.user.UserChange;
import pojo.user.UserCreate;
import com.github.javafaker.Faker;

public class ChangeUserDataTest {

    private final UserChecks check = new UserChecks();
    private final UserChangeClient userChangeAuto = new UserChangeClient();
    private String token;
    static Faker faker = new Faker();

    @Test
    @DisplayName("Изменение данных для авт. пользователя")
    public void changeAutoUser() {
        final UserCreateClient client = new UserCreateClient();
        final UserCreate user = UserCreate.random();
        token = check.createdUserSuccessfully(client.userCreate(user));

        UserChange userChange = new UserChange("Ilya" + faker.name().firstName() + "@ya.ru", "Test1" + faker.name().lastName());
        Response responseUserChange = userChangeAuto.userChangeAuto(token, userChange);
        Assert.assertTrue(check.userAutoChange(responseUserChange));
    }

    @Test
    @DisplayName("Изменение данных для неавт пользователя")
    public void changeNotAutoUser() {
        UserChange userChange = new UserChange("Ilya" + faker.name().firstName() + "@mail.ru", "Test2" + faker.name().lastName());
        Response responseUserChange = userChangeAuto.userChangeNotAuto(userChange);
        Assert.assertEquals("You should be authorised", check.userNotAutoChange(responseUserChange));
    }

    @After
    public void after() {
        if (token != null) {
            UserDeleteClient deleteClient = new UserDeleteClient();
            deleteClient.userDelete(token);
        }
    }
}