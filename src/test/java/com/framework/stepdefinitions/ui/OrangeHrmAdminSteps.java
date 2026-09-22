package com.framework.stepdefinitions.ui;

import com.framework.core.context.TestContext;
import com.framework.core.driver.DriverManager;
import com.framework.pages.OrangeHrmAdminPage;
import com.framework.pages.OrangeHrmDashboardPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class OrangeHrmAdminSteps {

    private OrangeHrmAdminPage adminPage;

    public OrangeHrmAdminSteps(TestContext context) {
    }

    @When("I open the OrangeHRM Admin module")
    public void iOpenTheOrangeHrmAdminModule() {
        adminPage = new OrangeHrmDashboardPage(DriverManager.get()).openAdmin();
    }

    @Then("the OrangeHRM Admin page should be displayed")
    public void theOrangeHrmAdminPageShouldBeDisplayed() {
        assertThat(adminPage.isLoaded()).as("OrangeHRM Admin page loaded").isTrue();
    }

    @When("I search Admin users with username {string}, role {string}, and status {string}")
    public void iSearchAdminUsers(String username, String role, String status) {
        adminPage.searchUsers(username, role, status);
    }

    @When("I reset the Admin user search")
    public void iResetTheAdminUserSearch() {
        adminPage.resetSearch();
    }

    @Then("the Admin results should contain a user named {string}")
    public void theAdminResultsShouldContainUser(String username) {
        assertThat(adminPage.getUserRowCount()).as("Admin result count").isGreaterThan(0);
        assertThat(adminPage.getFirstUserRowText()).contains(username);
    }

    @Then("the Admin results should be empty")
    public void theAdminResultsShouldBeEmpty() {
        assertThat(adminPage.getUserRowCount()).as("Admin result count").isZero();
    }

    @Then("the Admin user search form should be cleared")
    public void theAdminUserSearchFormShouldBeCleared() {
        assertThat(adminPage.isSearchFormCleared()).as("Admin search form cleared").isTrue();
    }

    @When("I open the Add User form")
    public void iOpenTheAddUserForm() {
        adminPage.openAddUser();
    }

    @Then("the Add User form should be displayed")
    public void theAddUserFormShouldBeDisplayed() {
        assertThat(adminPage.isAddUserFormDisplayed()).as("Add User form displayed").isTrue();
    }

    @When("I submit the empty Add User form")
    public void iSubmitTheEmptyAddUserForm() {
        adminPage.submitEmptyUserForm();
    }

    @Then("the Add User form should show required field errors")
    public void theAddUserFormShouldShowRequiredFieldErrors() {
        assertThat(adminPage.getRequiredErrorCount()).as("Add User required errors").isGreaterThan(0);
    }

    @When("I cancel the Add User form")
    public void iCancelTheAddUserForm() {
        adminPage.cancelAddUser();
    }

    @Then("the Admin user search page should be displayed")
    public void theAdminUserSearchPageShouldBeDisplayed() {
        assertThat(adminPage.isLoaded()).as("Admin user search page loaded").isTrue();
    }
}