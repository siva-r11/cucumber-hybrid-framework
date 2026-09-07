package com.framework.stepdefinitions.ui;

import com.framework.core.context.TestContext;
import com.framework.core.driver.DriverManager;
import com.framework.models.RegistrationData;
import com.framework.pages.ParaBankLoginPage;
import com.framework.pages.ParaBankRegistrationPage;
import com.framework.utils.TestDataProvider;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class ParaBankLoginSteps {

    private ParaBankLoginPage loginPage;
    private RegistrationData registrationData;

    public ParaBankLoginSteps(TestContext context) {
    }

    @Given("a newly registered ParaBank customer is on the login page")
    public void aNewlyRegisteredParaBankCustomerIsOnTheLoginPage() {
        ParaBankRegistrationPage registrationPage =
                new ParaBankRegistrationPage(DriverManager.get()).open();
        registrationData = TestDataProvider.generateRegistrationData();
        registrationPage.register(registrationData);

        assertThat(registrationPage.getSuccessMessage())
                .as("ParaBank registration setup for %s", registrationData.username())
                .contains("Your account was created successfully");

        loginPage = registrationPage.logOut();
        assertThat(loginPage.isLoaded()).as("ParaBank login page loaded").isTrue();
    }

    @When("I log in with the newly registered ParaBank credentials")
    public void iLogInWithTheNewlyRegisteredParaBankCredentials() {
        loginPage.login(registrationData);
    }

    @Then("the ParaBank accounts overview should be displayed")
    public void theParaBankAccountsOverviewShouldBeDisplayed() {
        assertThat(loginPage.getAccountOverviewHeading())
                .as("ParaBank account overview for %s", registrationData.username())
                .isEqualTo("Accounts Overview");
    }
}