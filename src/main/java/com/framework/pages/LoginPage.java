package com.framework.pages;

import com.framework.config.ConfigManager;
import com.framework.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for the login screen.
 *
 * <p>Demonstrates two authentication paths that share the same {@link com.framework.core.api.AuthManager}
 * philosophy:
 * <ol>
 *   <li>{@link #login(String, String)} — a normal UI login through the form.</li>
 *   <li>{@link #seedSession(String)} — injects a pre-fetched token into the browser to bypass
 *       the form entirely (the UI equivalent of API "storage state reuse").</li>
 * </ol>
 *
 * <p>Locators below target <a href="https://the-internet.herokuapp.com/login">the-internet</a>,
 * a stable public practice site, but the structure is app-agnostic.
 */
public class LoginPage extends BasePage {

    private static final By USERNAME = By.id("username");
    private static final By PASSWORD = By.id("password");
    private static final By SUBMIT = By.cssSelector("button[type='submit']");
    private static final By FLASH_MESSAGE = By.id("flash");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open the login page")
    public LoginPage open() {
        open(ConfigManager.get().webBaseUrl() + "/login");
        return this;
    }

    @Step("Log in as '{username}'")
    public SecureAreaPage login(String username, String password) {
        type(USERNAME, username);
        type(PASSWORD, password);
        click(SUBMIT);
        return new SecureAreaPage(driver);
    }

    /**
     * Seed a session from a pre-generated token instead of using the form. In a real app you
     * would set the auth cookie / localStorage key your front-end reads. This models the shared
     * auth flow between UI and API layers.
     */
    @Step("Seed browser session from token")
    public void seedSession(String token) {
        // Front-end must be loaded before localStorage is writable.
        open(ConfigManager.get().webBaseUrl());
        setLocalStorage("auth_token", token);
        log.info("Seeded browser session with pre-fetched auth token");
    }

    public String getFlashMessage() {
        return getText(FLASH_MESSAGE);
    }

    public boolean isLoaded() {
        return WaitUtils.visible(driver, USERNAME).isDisplayed();
    }
}
