import allure_steps_api.OrderSteps;
import allure_steps_api.UserSteps;
import serialization_api.OrderData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CreateOrderTest {
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
    }

    @Test
    @DisplayName("Positive create order")
    @Description("Create order with authorization")
    public void createOrderTest() {
        orderData = new OrderData();
        orderData.setIngredients(List.of(bun, filling, sauce));
        ValidatableResponse responseCreateOrder = orderSteps.createOrderWithAuthToken(accessToken, orderData);
        userSteps.checkPositiveResponse(responseCreateOrder);
    }

    @Test
    @DisplayName("Negative create order")
    @Description("Create order without authorization")
    public void createOrderWithoutAuthTest() {
        orderData = new OrderData();
        orderData.setIngredients(List.of(bun, filling, sauce));
        ValidatableResponse responseCreateOrder = orderSteps.createOrderWithoutAuthToken(orderData);
        userSteps.checkPositiveResponse(responseCreateOrder);
    }

    @Test
    @DisplayName("Negative create order")
    @Description("Create order with wrong ingredient hash")
    public void createOrderWrongIngredientHashTest() {
        orderData = new OrderData(List.of("61123xx","6234cvb"));
        ValidatableResponse responseCreateOrder = orderSteps.createOrderWithAuthToken(accessToken, orderData);
        orderSteps.checkAnswerWithWrongIngredientHash(responseCreateOrder);
    }

    @Test
    @DisplayName("Negative create order")
    @Description("Create order without ingredient hash")
    public void createOrderWithoutIngredientHashTest() {
        orderData = new OrderData();
        ValidatableResponse responseCreateOrder = orderSteps.createOrderWithAuthToken(accessToken, orderData);
        orderSteps.checkResponseInOrderWithOutIngredients(responseCreateOrder);
    }

    @After
    public void shutDown() {
        userSteps.deleteUser(accessToken);
    }
}
