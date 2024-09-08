import AllureStepsAPI.UserSteps;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChangeUserDataTest {
    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
    }

    @Test
    @DisplayName("Change user data with authorization")
    @Description("Change user data with authToken")
    public void changeDataWithAuthTokenTest() {
        ValidatableResponse responseCreateUser = userSteps.createUser("hehe2024@gmail.com", "123456", "Gosling");
        userSteps.checkPositiveResponse(responseCreateUser);
        ValidatableResponse responseLoginUser = userSteps.userLogin("hehe2024@gmail.com","123456");
        userSteps.checkPositiveResponse(responseLoginUser);
        accessToken = userSteps.getAuthToken(responseLoginUser);
        ValidatableResponse responseChangeWithAuthToken = userSteps.authorizationWithAuthToken(accessToken, "hehe2024Gosling@gmail.com", "123456G", "GoslingR");
        userSteps.checkPositiveResponse(responseChangeWithAuthToken);
    }

    @Test
    @DisplayName("Change user data without authorization")
    @Description("Change user data without authToken")
    public void changeDataWithoutAuthTokenTest() {
        ValidatableResponse responseCreateUser = userSteps.createUser("hehe2024@gmail.com", "123456", "Gosling");
        userSteps.checkPositiveResponse(responseCreateUser);
        ValidatableResponse responseLoginUser = userSteps.userLogin("hehe2024@gmail.com","123456");
        userSteps.checkPositiveResponse(responseLoginUser);
        accessToken = userSteps.getAuthToken(responseLoginUser);
        ValidatableResponse responseChangeWithoutAuthToken = userSteps.authorizationWithoutAuthToken("hehe2024Gosling@gmail.com", "123456G", "GoslingR");
        userSteps.checkResponseWithoutAuthToken(responseChangeWithoutAuthToken);
    }
    @After
    public void shutDown() {
        userSteps.deleteUser(accessToken);
    }
}
