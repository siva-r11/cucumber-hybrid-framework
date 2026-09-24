package com.framework.pages;

import com.framework.config.ConfigManager;
import com.framework.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OrangeHrmLoginPage extends BasePage {

    private static final By USERNAME = By.name("username");
    private static final By PASSWORD = By.name("password");
    private static final By LOGIN = By.cssSelector("button[type='submit']");
    private static final By ERROR_MESSAGE = By.cssSelector(".oxd-alert-content-text");

    public OrangeHrmLoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open the OrangeHRM login page")
    public OrangeHrmLoginPage open() {
        open(ConfigManager.get().orangeHrmBaseUrl() + "/auth/login");
        return this;
    }

    @Step("Log in to OrangeHRM as '{username}'")
    public OrangeHrmDashboardPage login(String username, String password) {
        type(USERNAME, username);
        type(PASSWORD, password);
        click(LOGIN);
        // Wait a moment for navigation to complete
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return new OrangeHrmDashboardPage(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(USERNAME) && isDisplayed(LOGIN);
    }

    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }
}