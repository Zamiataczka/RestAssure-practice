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

public class GetListOrdersTest {
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
        orderData = new OrderData(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa72"));
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
