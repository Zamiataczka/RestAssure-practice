import allurestepsapi.OrderSteps;
import allurestepsapi.UserSteps;
import serializationapi.OrderData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class GetListOrdersTest {
    private UserSteps userSteps;
    private OrderData orderData;
    private OrderSteps orderSteps;
    private String accessToken;
    private String bun;
    private String filling;
    private String sauce;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
        userSteps.createUser("hehe2024@gmail.com", "123456", "Gosling");
        ValidatableResponse responseLogin = userSteps.userLogin("hehe2024@gmail.com", "123456");
        accessToken = userSteps.getAuthToken(responseLogin);
        ValidatableResponse response = orderSteps.getIngredients();
        List<String> ingredients = response.extract().path("data._id");
        bun = ingredients.get(0);
        filling = ingredients.get(1);
        sauce = ingredients.get(4);
        orderData = new OrderData(List.of(bun, filling, sauce));
        orderSteps.createOrderWithAuthToken(accessToken, orderData);
    }

    @Test
    @DisplayName("Positive check get list of orders")
    @Description("Getting list of orders with auth")
    public void getListOfOrdersWithAuthTest () {
        ValidatableResponse responseGetListOrders = orderSteps.listOfOrdersWithAuthToken(accessToken);
        userSteps.checkPositiveResponse(responseGetListOrders);
    }

    @Test
    @DisplayName("Negative check get list of orders")
    @Description("Getting list of orders without auth")
    public void getListOfOrdersWithoutAuthTest () {
        ValidatableResponse responseGetListOrders = orderSteps.listOfOrdersWithoutAuthToken();
        orderSteps.getOrderListWithoutAuth(responseGetListOrders);
    }

    @After
    public void shutDown() {
        userSteps.deleteUser(accessToken);
    }
}
