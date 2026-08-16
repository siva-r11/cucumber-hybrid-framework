package com.framework.stepdefinitions.api;

import com.framework.core.api.AuthManager;
import com.framework.core.context.TestContext;
import com.framework.models.User;
import com.framework.services.UserService;
import com.framework.utils.SchemaValidator;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for the user management API feature.
 *
 * <p>Steps delegate all HTTP concerns to {@link UserService} and share the latest {@link Response}
 * (and any created ids) through the injected {@link TestContext}. Contract checks route through
 * {@link SchemaValidator}. Context keys are namespaced to avoid clashes across features.
 */
public class UserApiSteps {

    /** Context keys shared across API/hybrid steps. */
    public static final String KEY_RESPONSE = "api.lastResponse";
    public static final String KEY_TOKEN = "api.authToken";
    public static final String KEY_CREATED_USER_ID = "api.createdUserId";

    private final TestContext context;
    private final UserService userService = new UserService();

    public UserApiSteps(TestContext context) {
        this.context = context;
    }

    @Given("an authenticated API session")
    public void an_authenticated_api_session() {
        String token = AuthManager.getToken();
        context.getScenarioContext().set(KEY_TOKEN, token);
        assertThat(token).as("auth token").isNotBlank();
    }

    @When("I request the user with id {string}")
    public void i_request_the_user_with_id(String id) {
        store(userService.getUser(id));
    }

    @When("I create a user named {string} with job {string}")
    public void i_create_a_user(String name, String job) {
        String token = requireToken();
        User user = User.builder().name(name).job(job).build();
        Response response = userService.createUser(user, token);
        store(response);
        context.getScenarioContext().set(KEY_CREATED_USER_ID, response.jsonPath().getString("id"));
    }

    @Then("the API response status should be {int}")
    public void the_api_response_status_should_be(int expected) {
        assertThat(lastResponse().statusCode())
                .as("HTTP status code")
                .isEqualTo(expected);
    }

    @And("the response should match the {string} schema")
    public void the_response_should_match_schema(String schemaFile) {
        SchemaValidator.validate(lastResponse().asString(), schemaFile);
    }

    @And("the user's email should be {string}")
    public void the_users_email_should_be(String expected) {
        assertThat(lastResponse().jsonPath().getString("data.email"))
                .as("user email")
                .isEqualTo(expected);
    }

    @And("the created user's name should be {string}")
    public void the_created_users_name_should_be(String expected) {
        assertThat(lastResponse().jsonPath().getString("name"))
                .as("created user name")
                .isEqualTo(expected);
    }

    // ---------- helpers ----------

    private void store(Response response) {
        context.getScenarioContext().set(KEY_RESPONSE, response);
    }

    private Response lastResponse() {
        Response response = context.getScenarioContext().get(KEY_RESPONSE);
        assertThat(response).as("a request must have been made first").isNotNull();
        return response;
    }

    private String requireToken() {
        return context.getScenarioContext()
                .<String>getOptional(KEY_TOKEN)
                .orElseGet(AuthManager::getToken);
    }
}
