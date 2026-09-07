package com.framework.pages;

import com.framework.config.ConfigManager;
import com.framework.models.RegistrationData;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ParaBankRegistrationPage extends BasePage {

    private static final By FIRST_NAME = By.name("customer.firstName");
    private static final By LAST_NAME = By.name("customer.lastName");
    private static final By STREET = By.name("customer.address.street");
    private static final By CITY = By.name("customer.address.city");
    private static final By STATE = By.name("customer.address.state");
    private static final By ZIP_CODE = By.name("customer.address.zipCode");
    private static final By PHONE = By.name("customer.phoneNumber");
    private static final By SSN = By.name("customer.ssn");
    private static final By USERNAME = By.name("customer.username");
    private static final By PASSWORD = By.name("customer.password");
    private static final By CONFIRM_PASSWORD = By.name("repeatedPassword");
    private static final By REGISTER = By.cssSelector("input[value='Register']");
    private static final By SUCCESS_MESSAGE = By.cssSelector("#rightPanel p");
    private static final By LOG_OUT = By.linkText("Log Out");

    public ParaBankRegistrationPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open the ParaBank registration page")
    public ParaBankRegistrationPage open() {
        open(ConfigManager.get().paraBankBaseUrl() + "/register.htm");
        return this;
    }

    @Step("Register a new ParaBank customer")
    public void register(RegistrationData data) {
        type(FIRST_NAME, data.firstName());
        type(LAST_NAME, data.lastName());
        type(STREET, data.street());
        type(CITY, data.city());
        type(STATE, data.state());
        type(ZIP_CODE, data.zipCode());
        type(PHONE, data.phone());
        type(SSN, data.ssn());
        type(USERNAME, data.username());
        type(PASSWORD, data.password());
        type(CONFIRM_PASSWORD, data.password());
        click(REGISTER);
    }

    public String getSuccessMessage() {
        return getText(SUCCESS_MESSAGE);
    }

    @Step("Log out of ParaBank")
    public ParaBankLoginPage logOut() {
        click(LOG_OUT);
        return new ParaBankLoginPage(driver);
    }

    public boolean isRegistrationFormLoaded() {
        return isDisplayed(FIRST_NAME) && isDisplayed(REGISTER);
    }
}