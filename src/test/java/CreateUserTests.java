import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Test;
import pojo.user.UserCreate;

public class CreateUserTests extends BaseTest {

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUniqueUserTest() {
        UserCreate user = UserCreate.random();
        ValidatableResponse createUserResponse = clientUser.userCreate(user);

        token = checkUser.createdUserSuccessfully(createUserResponse);
        Assert.assertNotNull(token);
    }

    @Test
    @DisplayName("Создание уже зарегистрированного пользователя")
    public void createDuplicateUserTest() {
        UserCreate user = UserCreate.random();
        createUserAndSetToken(user);

        String email = user.getEmail();
        UserCreate userDuplicate = new UserCreate(email, "Password", "Name");

        ValidatableResponse createDuplicateResponse = clientUser.userCreate(userDuplicate);
        String messageExists = checkUser.createdExistsUser(createDuplicateResponse);

        Assert.assertEquals("User already exists", messageExists);
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void createNotNameUserTest() {
        UserCreate user = new UserCreate("email", "password", null);
        ValidatableResponse createResponse = clientUser.userCreate(user);
        String notNameUserMessage = checkUser.createdNotNameUser(createResponse);
        Assert.assertEquals("Email, password and name are required fields", notNameUserMessage);
    }
}
