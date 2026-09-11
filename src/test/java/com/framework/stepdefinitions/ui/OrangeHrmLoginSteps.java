package com.framework.stepdefinitions.ui;

import com.framework.core.context.TestContext;
import com.framework.core.driver.DriverManager;
import com.framework.pages.OrangeHrmDashboardPage;
import com.framework.pages.OrangeHrmLoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class OrangeHrmLoginSteps {

    private OrangeHrmLoginPage loginPage;
    private OrangeHrmDashboardPage dashboardPage;

    public OrangeHrmLoginSteps(TestContext context) {
    }

    @Given("the OrangeHRM login page is open")
    public void theOrangeHrmLoginPageIsOpen() {
        loginPage = new OrangeHrmLoginPage(DriverManager.get()).open();
        assertThat(loginPage.isLoaded()).as("OrangeHRM login page loaded").isTrue();
    }

    @When("I log in to OrangeHRM with username {string} and password {string}")
    public void iLogInToOrangeHrmWithUsernameAndPassword(String username, String password) {
        dashboardPage = loginPage.login(username, password);
    }

    @Then("the OrangeHRM dashboard should be displayed")
    public void theOrangeHrmDashboardShouldBeDisplayed() {
        assertThat(dashboardPage.isLoaded()).as("OrangeHRM dashboard loaded").isTrue();
        assertThat(dashboardPage.getHeading()).as("OrangeHRM dashboard heading").isEqualTo("Dashboard");
    }

    @Then("the OrangeHRM login error should contain {string}")
    public void theOrangeHrmLoginErrorShouldContain(String expected) {
        assertThat(loginPage.getErrorMessage()).as("OrangeHRM login error").contains(expected);
    }
}