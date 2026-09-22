package com.framework.pages;

import com.framework.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class OrangeHrmAdminPage extends BasePage {

    private static final By ADMIN_HEADING = By.cssSelector(".oxd-topbar-header-breadcrumb h6");
    private static final By USERNAME_FILTER = By.cssSelector("input[placeholder='Username']");
    private static final By EMPLOYEE_FILTER = By.cssSelector("input[placeholder='Type for hints...']");
    private static final By USER_ROLE_FILTER = selectForLabel("User Role");
    private static final By STATUS_FILTER = selectForLabel("Status");
    private static final By SEARCH_BUTTON = By.xpath("//button[normalize-space()='Search']");
    private static final By RESET_BUTTON = By.xpath("//button[normalize-space()='Reset']");
    private static final By ADD_BUTTON = By.xpath("//button[normalize-space()='Add']");
    private static final By USER_ROWS = By.cssSelector(".oxd-table-body .oxd-table-card");
    private static final By SAVE_BUTTON = By.xpath("//button[normalize-space()='Save']");
    private static final By CANCEL_BUTTON = By.xpath("//button[normalize-space()='Cancel']");
    private static final By REQUIRED_ERRORS = By.cssSelector(".oxd-input-group .oxd-input-field-error-message");
    private static final By EMPLOYEE_SUGGESTION = By.cssSelector(".oxd-autocomplete-option");

    public OrangeHrmAdminPage(WebDriver driver) {
        super(driver);
    }

    private static By selectForLabel(String label) {
        return By.xpath("//label[normalize-space()='" + label + "']/ancestor::div[contains(@class,'oxd-input-group')]"
                + "//div[contains(@class,'oxd-select-text')]");
    }

    private static By inputForLabel(String label) {
        return By.xpath("//label[normalize-space()='" + label + "']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    }

    public boolean isLoaded() {
        return isDisplayed(ADMIN_HEADING) && getHeading().equals("Admin") && isDisplayed(SEARCH_BUTTON);
    }

    public String getHeading() {
        return getText(ADMIN_HEADING);
    }

    @Step("Search Admin users")
    public OrangeHrmAdminPage searchUsers(String username, String role, String status) {
        if (!username.isBlank()) {
            type(USERNAME_FILTER, username);
        }
        if (!role.isBlank()) {
            select(USER_ROLE_FILTER, role);
        }
        if (!status.isBlank()) {
            select(STATUS_FILTER, status);
        }
        click(SEARCH_BUTTON);
        return this;
    }

    @Step("Reset Admin user search")
    public OrangeHrmAdminPage resetSearch() {
        click(RESET_BUTTON);
        return this;
    }

    public int getUserRowCount() {
        return driver.findElements(USER_ROWS).size();
    }

    public String getFirstUserRowText() {
        return WaitUtils.visible(driver, USER_ROWS).getText();
    }

    public boolean isSearchFormCleared() {
        return driver.findElement(USERNAME_FILTER).getAttribute("value").isEmpty()
                && driver.findElement(EMPLOYEE_FILTER).getAttribute("value").isEmpty();
    }

    @Step("Open Add User form")
    public OrangeHrmAdminPage openAddUser() {
        click(ADD_BUTTON);
        return this;
    }

    public boolean isAddUserFormDisplayed() {
        return isDisplayed(inputForLabel("Username")) && isDisplayed(SAVE_BUTTON);
    }

    public OrangeHrmAdminPage submitEmptyUserForm() {
        click(SAVE_BUTTON);
        return this;
    }

    public int getRequiredErrorCount() {
        return driver.findElements(REQUIRED_ERRORS).size();
    }

    public OrangeHrmAdminPage cancelAddUser() {
        click(CANCEL_BUTTON);
        return this;
    }

    public void select(By locator, String value) {
        click(locator);
        click(By.xpath("//div[contains(@class,'oxd-select-option')]//span[normalize-space()='" + value + "']"));
    }

    public OrangeHrmAdminPage chooseEmployee(String employeeName) {
        type(EMPLOYEE_FILTER, employeeName);
        click(EMPLOYEE_SUGGESTION);
        return this;
    }
}