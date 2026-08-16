package com.framework.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for the authenticated "Secure Area" landing page shown after a successful login.
 * Exposes assertions the step layer needs (banner text, logout availability) without leaking
 * locators upward.
 */
public class SecureAreaPage extends BasePage {

    private static final By FLASH_MESSAGE = By.id("flash");
    private static final By HEADER = By.cssSelector(".example h2");
    private static final By LOGOUT_BUTTON = By.cssSelector("a[href='/logout']");

    public SecureAreaPage(WebDriver driver) {
        super(driver);
    }

    @Step("Read secure-area confirmation banner")
    public String getSuccessMessage() {
        return getText(FLASH_MESSAGE);
    }

    public String getHeader() {
        return getText(HEADER);
    }

    public boolean isLoaded() {
        return isDisplayed(LOGOUT_BUTTON);
    }
}
