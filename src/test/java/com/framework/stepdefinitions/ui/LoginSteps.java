package com.framework.stepdefinitions.ui;

import com.framework.core.context.TestContext;
import com.framework.core.driver.DriverManager;
import com.framework.pages.LoginPage;
import com.framework.pages.SecureAreaPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for the UI login feature.
 *
 * <p>PicoContainer injects the shared {@link TestContext}. Page Objects are created lazily from
 * the thread-local driver so these steps stay declarative and free of Selenium plumbing. Pages
 * are stashed on the context so subsequent steps (and hybrid flows) can reuse them.
 */
public class LoginSteps {

    private LoginPage loginPage;
    private SecureAreaPage secureAreaPage;

    public LoginSteps(TestContext context) {
    }

    @Given("the login page is open")
    public void the_login_page_is_open() {
        loginPage = new LoginPage(DriverManager.get()).open();
        assertThat(loginPage.isLoaded()).as("login page loaded").isTrue();
    }

    @When("I log in with username {string} and password {string}")
    public void i_log_in_with(String username, String password) {
        // The login page may already exist (standard flow) or the browser may have been
        // session-seeded by a hybrid step; handle both.
        if (loginPage == null) {
            loginPage = new LoginPage(DriverManager.get()).open();
        }
        secureAreaPage = loginPage.login(username, password);
    }

    @Then("I should land on the secure area")
    public void i_should_land_on_the_secure_area() {
        assertThat(secureAreaPage.isLoaded()).as("secure area loaded").isTrue();
    }

    @And("the confirmation banner should contain {string}")
    public void the_confirmation_banner_should_contain(String expected) {
        assertThat(secureAreaPage.getSuccessMessage())
                .as("secure area confirmation banner")
                .contains(expected);
    }

    @Then("the login error banner should contain {string}")
    public void the_login_error_banner_should_contain(String expected) {
        assertThat(loginPage.getFlashMessage())
                .as("login error banner")
                .contains(expected);
    }
}
