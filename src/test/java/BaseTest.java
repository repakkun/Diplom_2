import helpers.user.UserCreateClient;
import helpers.user.UserDeleteClient;
import helpers.user.UserChecks;
import org.junit.After;
import org.junit.Before;
import io.restassured.response.ValidatableResponse;
import pojo.user.UserCreate;

public class BaseTest {

    protected String token;
    protected final UserCreateClient clientUser = new UserCreateClient();
    protected final UserChecks checkUser = new UserChecks();

    @Before
    public void setUp() {
        // Здесь можно добавить общие действия, которые нужно выполнить перед каждым тестом, если такие появятся
    }

    @After
    public void tearDown() {
        if (token != null) {
            UserDeleteClient deleteClient = new UserDeleteClient();
            deleteClient.userDelete(token);
        }
    }

    protected void createUserAndSetToken(UserCreate user) {
        ValidatableResponse createUserResponse = clientUser.userCreate(user);
        token = checkUser.createdUserSuccessfully(createUserResponse);
    }
}
