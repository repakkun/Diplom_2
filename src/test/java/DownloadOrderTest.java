import helpers.ingredients.IngredientsClient;
import helpers.order.OrderChecks;
import helpers.order.OrderCreateClient;
import helpers.order.OrderDownloadClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Test;
import pojo.order.OrderCreate;
import pojo.user.UserCreate;

import java.util.ArrayList;
import java.util.Random;

public class DownloadOrderTest extends BaseTest {

    private final OrderDownloadClient clientOrderDownload = new OrderDownloadClient();
    private final OrderCreateClient clientOrderCreate = new OrderCreateClient();
    private final OrderChecks checkOrder = new OrderChecks();
    private final IngredientsClient clientIngredients = new IngredientsClient();
    public final ArrayList<String> ingredients = new ArrayList<>();
    Random random = new Random();

    @Test
    @DisplayName("Получение заказа авторизованного пользователя")
    public void downloadOrderAutoUser() {
        UserCreate user = UserCreate.random();
        createUserAndSetToken(user);

        Response downloadIngredients = clientIngredients.DownloadIngredients();
        ArrayList<String> listHashIngredients = downloadIngredients.path("data._id");
        ingredients.add(listHashIngredients.get(random.nextInt(listHashIngredients.size())));
        ingredients.add(listHashIngredients.get(random.nextInt(listHashIngredients.size())));
        OrderCreate orderCreate = new OrderCreate(ingredients);
        clientOrderCreate.orderAutoCreate(token, orderCreate);

        Response response = clientOrderDownload.orderAutoDownload(token);
        Assert.assertEquals(1, checkOrder.downloadOrderAutoUser(response).size());
    }

    @Test
    @DisplayName("Получение заказа не авторизованного пользователя")
    public void downloadOrderNotAutoUser() {
        Response response = clientOrderDownload.orderNotAutoDownload();
        Assert.assertFalse(checkOrder.downloadOrderNotAutoUser(response));
    }
}
