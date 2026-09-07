package com.framework.stepdefinitions.ui;

import com.framework.core.context.TestContext;
import com.framework.core.driver.DriverManager;
import com.framework.models.RegistrationData;
import com.framework.pages.ParaBankRegistrationPage;
import com.framework.utils.TestDataProvider;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class ParaBankRegistrationSteps {

    private ParaBankRegistrationPage registrationPage;
    private RegistrationData registrationData;

    public ParaBankRegistrationSteps(TestContext context) {
    }

    @Given("the ParaBank registration page is open")
    public void theParaBankRegistrationPageIsOpen() {
        registrationPage = new ParaBankRegistrationPage(DriverManager.get()).open();
        assertThat(registrationPage.isRegistrationFormLoaded())
                .as("ParaBank registration form loaded")
                .isTrue();
    }

    @When("I register a new ParaBank customer with generated dummy data")
    public void iRegisterANewParaBankCustomerWithGeneratedDummyData() {
        registrationData = TestDataProvider.generateRegistrationData();
        registrationPage.register(registrationData);
    }

    @Then("the ParaBank account should be created")
    public void theParaBankAccountShouldBeCreated() {
        assertThat(registrationPage.getSuccessMessage())
                .as("ParaBank registration confirmation for %s", registrationData.username())
                .contains("Your account was created successfully");
    }
}