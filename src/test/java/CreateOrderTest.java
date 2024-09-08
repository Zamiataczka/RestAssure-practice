import AllureStepsAPI.OrderSteps;
import AllureStepsAPI.UserSteps;
import SerializationAPI.OrderData;
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

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
        userSteps.createUser("hehe2024@gmail.com", "123456", "Gosling");
        ValidatableResponse responseLogin = userSteps.userLogin("hehe2024@gmail.com", "123456");
        accessToken = userSteps.getAuthToken(responseLogin);
    }
    @Test
    @DisplayName("Positive create order")
    @Description("Create order with authorization")
    public void createOrderTest() {
        orderData = new OrderData(List.of("61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa6f"));
        ValidatableResponse responseCreateOrder = orderSteps.createOrderWithAuthToken(accessToken, orderData);
        userSteps.checkPositiveResponse(responseCreateOrder);
    }

    @Test
    @DisplayName("Negative create order")
    @Description("Create order without authorization")
    public void createOrderWithoutAuthTest() {
        orderData = new OrderData(List.of("61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa6f"));
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
