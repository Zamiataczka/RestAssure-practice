package allurestepsapi;

import serializationapi.OrderData;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;

import static constantsapi.EndPointsURL.INGREDIENTS_URL;
import static constantsapi.EndPointsURL.ORDERS_URL;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class OrderSteps extends SpecificationSetUp {
    @Step("Create order with auth token")
    public ValidatableResponse createOrderWithAuthToken(String accessToken, OrderData orderData) {
        return given()
                .header("authorization", accessToken)
                .spec(getSpec())
                .body(orderData)
                .when()
                .post(ORDERS_URL)
                .then();
    }

    @Step("Create order without auth token")
    public ValidatableResponse createOrderWithoutAuthToken(OrderData orderData) {
        return given()
                .spec(getSpec())
                .body(orderData)
                .when()
                .post(ORDERS_URL)
                .then();
    }

    @Step("List of orders with auth token")
    public ValidatableResponse listOfOrdersWithAuthToken(String accessToken) {
        return given()
                .header("authorization", accessToken)
                .spec(getSpec())
                .body("")
                .when()
                .get(ORDERS_URL)
                .then();
    }

    @Step("List of orders without auth token")
    public ValidatableResponse listOfOrdersWithoutAuthToken() {
        return given()
                .spec(getSpec())
                .body("")
                .when()
                .get(ORDERS_URL)
                .then();
    }

    @Step("Check response when receiving orders from a specific user without authorized")
    public void getOrderListWithoutAuth(ValidatableResponse validatableResponse) {
        validatableResponse.assertThat()
                .body("success", is(false))
                .and().statusCode(401);
        String actualMessage = validatableResponse.extract().path("message").toString();
        Assert.assertEquals("You should be authorised", actualMessage);
    }

    @Step("Checking response when creating an order without ingredients")
    public void checkResponseInOrderWithOutIngredients(ValidatableResponse validatableResponse) {
        validatableResponse
                .body("success", is(false))
                .statusCode(400);
        String actualMessage = validatableResponse.extract().path("message").toString();
        Assert.assertEquals("Ingredient ids must be provided", actualMessage);
    }

    @Step("Checking response when creating an order with the a wrong ingredient hash")
    public void checkAnswerWithWrongIngredientHash(ValidatableResponse validatableResponse) {
        validatableResponse
                .statusCode(500);
    }

    @Step("Get ingredients from stash")
    public ValidatableResponse getIngredients() {
        return given()
                .spec(getSpec())
                .when()
                .get(INGREDIENTS_URL)
                .then();
    }

}
