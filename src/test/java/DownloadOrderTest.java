import helpers.ingredients.IngredientsClient;
import helpers.order.OrderChecks;
import helpers.order.OrderCreateClient;
import helpers.order.OrderDownloadClient;
import helpers.user.UserChecks;
import helpers.user.UserCreateClient;
import helpers.user.UserDeleteClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import pojo.order.OrderCreate;
import pojo.user.UserCreate;
import java.util.ArrayList;
import java.util.Random;

public class DownloadOrderTest {

    private String token;
    private final UserCreateClient clientUser = new UserCreateClient();
    private final OrderDownloadClient clientOrderDownload = new OrderDownloadClient();
    private final OrderCreateClient clientOrderCreate = new OrderCreateClient();
    private final UserChecks checkUser = new UserChecks();
    private final OrderChecks checkOrder = new OrderChecks();
    private final IngredientsClient clientIngredients = new IngredientsClient();
    public final ArrayList<String> ingredients = new ArrayList<>();
    Random random = new Random();

    @Test
    @DisplayName("Получение заказа авторизованного пользователя")
    public void downloadOrderAutoUser(){
        UserCreate user = UserCreate.random();
        ValidatableResponse createUserResponse = clientUser.userCreate(user);
        token = checkUser.createdUserSuccessfully(createUserResponse);

        Response downloadIngredients = clientIngredients.DownloadIngredients();
        ArrayList<String> listHashIngredients = downloadIngredients.path("data._id");
        ingredients.add(listHashIngredients.get(random.nextInt(listHashIngredients.size())));
        ingredients.add(listHashIngredients.get(random.nextInt(listHashIngredients.size())));
        OrderCreate orderCreate = new OrderCreate(ingredients);
        Response createOrderResponse = clientOrderCreate.orderAutoCreate(token, orderCreate);

        Response response = clientOrderDownload.orderAutoDownload(token);
        Assert.assertEquals(1, checkOrder.downloadOrderAutoUser(response).size());
    }

    @Test
    @DisplayName("Получение заказа не авторизованного пользователя")
    public void downloadOrderNotAutoUser(){
        Response response = clientOrderDownload.orderNotAutoDownload();
        Assert.assertFalse(checkOrder.downloadOrderNotAutoUser(response));
    }

    @After
    public void after() {
        if (token != null) {
            UserDeleteClient deleteClient = new UserDeleteClient();
            deleteClient.userDelete(token);
        }
    }
}
