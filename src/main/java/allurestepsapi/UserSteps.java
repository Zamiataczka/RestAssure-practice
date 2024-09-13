package allurestepsapi;

import serializationapi.AccountData;
import serializationapi.UserData;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;

import static constantsapi.EndPointsURL.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class UserSteps extends SpecificationSetUp {
    @Step("Create user")
    public ValidatableResponse createUser (String email, String password, String name) {
        UserData userData = new UserData(email, password, name);
        return given()
                .spec(getSpec())
                .body(userData)
                .when()
                .post(AUTH_REGISTER_URL)
                .then();
    }

    @Step("Login user")
    public ValidatableResponse userLogin (String email, String password) {
        AccountData accountData = new AccountData(email, password);
        return given()
                .spec(getSpec())
                .body(accountData)
                .when()
                .post(AUTH_LOGIN_URL)
                .then();
    }

    @Step("Get access token")
    public String getAuthToken(ValidatableResponse validatableResponse) {
        return validatableResponse.extract().path("accessToken");
    }

    @Step("Delete user")
    public void deleteUser(String accessToken) {
        given()
                .header("authorization", accessToken)
                .spec(getSpec())
                .when()
                .delete(AUTH_USER_URL);
    }

    @Step ("Delete user")
    public void deleteUserFromNegativeTests(String accessToken) {
        if (accessToken != null) {
            deleteUser(accessToken);
        } else {
            given().spec(getSpec())
                    .when()
                    .delete(AUTH_USER_URL);
        }
    }

    @Step("Authorization with an access token")
    public ValidatableResponse authorizationWithAuthToken (String accessToken, String email, String password, String name) {
        UserData user = new UserData(email, password, name);
        return given()
                .header("authorization", accessToken)
                .spec(getSpec())
                .body(user)
                .when()
                .patch(AUTH_USER_URL)
                .then();
    }

    @Step("Authorization without an access token")
    public ValidatableResponse authorizationWithoutAuthToken(String email, String password, String name) {
        UserData user = new UserData(email, password, name);
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .patch(AUTH_USER_URL)
                .then();
    }

    @Step("Response check after creating already existing user")
    public void checkResponseCreatingAlreadyExistingUser (ValidatableResponse validatableResponse) {
        validatableResponse.assertThat()
                .body("success", is(false))
                .and()
                .statusCode(403);
        String actualMessage = validatableResponse.extract().path("message").toString();
        Assert.assertEquals("User already exists", actualMessage);
    }

    @Step("Response check when creating user without required fields")
    public void checkResponseCreatingUserWithoutRequiredFields (ValidatableResponse validatableResponse) {
        validatableResponse.assertThat()
                .body("success", is(false))
                .and()
                .statusCode(403);
        String actualMessage = validatableResponse.extract().path("message").toString();
        Assert.assertEquals("Email, password and name are required fields", actualMessage);
    }

    @Step("Response check after login with wrong user data")
    public void checkResponseWithWrongData(ValidatableResponse validatableResponse) {
        validatableResponse.assertThat()
                .body("success", is(false))
                .and()
                .statusCode(401);
        String actualMessage = validatableResponse.extract().path("message").toString();
        Assert.assertEquals("email or password are incorrect", actualMessage);
    }

    @Step("Response check after changing user data without auth token")
    public void checkResponseWithoutAuthToken(ValidatableResponse validatableResponse) {
        validatableResponse.assertThat()
                .body("success", is(false))
                .and().statusCode(401);
        String actualMessage = validatableResponse.extract().path("message").toString();
        Assert.assertEquals("You should be authorised", actualMessage);
    }

    @Step("Check positive response after interactions with user endpoints - 200")
    public void checkPositiveResponse(ValidatableResponse validatableResponse) {
        validatableResponse
                .body("success", is(true))
                .statusCode(200);
    }
}
