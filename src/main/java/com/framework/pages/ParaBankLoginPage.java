package com.framework.pages;

import com.framework.models.RegistrationData;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ParaBankLoginPage extends BasePage {

    private static final By USERNAME = By.name("username");
    private static final By PASSWORD = By.name("password");
    private static final By LOGIN = By.cssSelector("#loginPanel input[value='Log In']");
    private static final By ACCOUNTS_OVERVIEW = By.cssSelector("#rightPanel h1.title");

    public ParaBankLoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Log in with the newly registered ParaBank customer")
    public void login(RegistrationData data) {
        type(USERNAME, data.username());
        type(PASSWORD, data.password());
        click(LOGIN);
    }

    public boolean isLoaded() {
        return isDisplayed(USERNAME) && isDisplayed(LOGIN);
    }

    public String getAccountOverviewHeading() {
        return getText(ACCOUNTS_OVERVIEW);
    }
}