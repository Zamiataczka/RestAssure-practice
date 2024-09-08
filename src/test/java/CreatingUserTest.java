import AllureStepsAPI.UserSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreatingUserTest {
    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
    }

    @Test
    @DisplayName("Positive check create user")
    @Description("Positive test create user")
    public void createUserTest() {
        ValidatableResponse responseCreateUser = userSteps.createUser("hehe2024@gmail.com", "123456", "Gosling");
        userSteps.checkPositiveResponse(responseCreateUser);
        accessToken = userSteps.getAuthToken(responseCreateUser);
    }

    @Test
    @DisplayName("Negative check create user")
    @Description("Create user who has been already created")
    public void createSameUserTest() {
        ValidatableResponse responseCreateUser = userSteps.createUser("hehe2024@gmail.com", "123456", "Gosling");
        accessToken = userSteps.getAuthToken(responseCreateUser);
        ValidatableResponse responseCreateSameUser = userSteps.createUser("hehe2024@gmail.com", "123456", "Gosling");
        userSteps.checkResponseCreatingAlreadyExistingUser(responseCreateSameUser);
    }

    @Test
    @DisplayName("Negative check create user")
    @Description("Create user without email")
    public void createUserWithoutEmailTest() {
        ValidatableResponse responseCreateWithoutEmailUser = userSteps.createUser("", "123456", "Gosling");
        userSteps.checkResponseCreatingUserWithoutRequiredFields(responseCreateWithoutEmailUser);
    }

    @Test
    @DisplayName("Negative check create user")
    @Description("Create user without password")
    public void createUserWithoutPasswordTest() {
        ValidatableResponse responseCreateWithoutPasswordUser = userSteps.createUser("hehe2024@gmail.com", "", "Gosling");
        userSteps.checkResponseCreatingUserWithoutRequiredFields(responseCreateWithoutPasswordUser);
    }

    @Test
    @DisplayName("Negative check create user")
    @Description("Create user without name")
    public void createUserWithoutNameTest() {
        ValidatableResponse responseCreateWithoutNameUser = userSteps.createUser("hehe2024@gmail.com", "123456", "");
        userSteps.checkResponseCreatingUserWithoutRequiredFields(responseCreateWithoutNameUser);
    }

    @After
    public void shutDown() {
        userSteps.deleteUserFromNegativeTests(accessToken);
    }
}