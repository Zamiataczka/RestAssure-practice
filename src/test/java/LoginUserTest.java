import AllureStepsAPI.UserSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginUserTest {
    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        ValidatableResponse responseCreateUser = userSteps.createUser("hehe2024@gmail.com", "123456", "Gosling");
        accessToken = userSteps.getAuthToken(responseCreateUser);
    }

    @Test
    @DisplayName("Positive check user login")
    @Description("User login")
    public void userLoginTest () {
        ValidatableResponse responseLoginUser = userSteps.userLogin("hehe2024@gmail.com", "123456");
        userSteps.checkPositiveResponse(responseLoginUser);
    }

    @Test
    @DisplayName("Negative check user login")
    @Description("User login with wrong email")
    public void userLoginWithWrongEmailTest () {
        ValidatableResponse responseLoginUser = userSteps.userLogin("hehe2023@gmail.com", "123456");
        userSteps.checkResponseWithWrongData(responseLoginUser);
    }

    @Test
    @DisplayName("Negative check user login")
    @Description("User login with wrong password")
    public void userLoginWithWrongPasswordTest () {
        ValidatableResponse responseLoginUser = userSteps.userLogin("hehe2023@gmail.com", "12345");
        userSteps.checkResponseWithWrongData(responseLoginUser);
    }

    @After
    public void shutDown() {
        userSteps.deleteUser(accessToken);
    }
}
