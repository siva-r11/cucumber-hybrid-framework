package com.framework.stepdefinitions.hybrid;

import com.framework.core.api.AuthManager;
import com.framework.core.context.TestContext;
import com.framework.core.driver.DriverManager;
import com.framework.models.User;
import com.framework.pages.LoginPage;
import com.framework.services.UserService;
import com.framework.stepdefinitions.api.UserApiSteps;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Hybrid steps demonstrating the core value of a unified UI+API framework: use the fast, stable
 * API to arrange test state, then verify behaviour through the UI, with a single auth token
 * shared across both layers.
 *
 * <p>This class only adds the steps unique to the hybrid flow. The generic
 * {@code Given an authenticated API session}, the login steps, and the assertions are reused
 * verbatim from {@link UserApiSteps} and the UI {@code LoginSteps} because Cucumber pools all
 * step definitions and shares the same {@link TestContext} instance across them for a scenario.
 */
public class UserOnboardingSteps {

    private final TestContext context;
    private final UserService userService = new UserService();

    public UserOnboardingSteps(TestContext context) {
        this.context = context;
    }

    @When("I create a user named {string} with job {string} via API")
    public void i_create_a_user_via_api(String name, String job) {
        String token = context.getScenarioContext()
                .<String>getOptional(UserApiSteps.KEY_TOKEN)
                .orElseGet(AuthManager::getToken);

        User user = User.builder().name(name).job(job).build();
        Response response = userService.createUser(user, token);

        assertThat(response.statusCode()).as("API setup: create user").isEqualTo(201);
        context.getScenarioContext().set(UserApiSteps.KEY_RESPONSE, response);
        context.getScenarioContext()
                .set(UserApiSteps.KEY_CREATED_USER_ID, response.jsonPath().getString("id"));
    }

    @Then("the created user id is stored for later steps")
    public void the_created_user_id_is_stored() {
        String id = context.getScenarioContext().get(UserApiSteps.KEY_CREATED_USER_ID);
        assertThat(id).as("created user id carried in scenario context").isNotBlank();
    }

    @And("I open the app and seed the browser session with the shared token")
    public void seed_browser_with_shared_token() {
        String token = context.getScenarioContext().get(UserApiSteps.KEY_TOKEN);
        assertThat(token).as("shared token must exist from the API phase").isNotBlank();

        // Reuse the SAME token that authenticated the API calls to seed the UI session.
        LoginPage loginPage = new LoginPage(DriverManager.get());
        loginPage.seedSession(token);
    }
}
