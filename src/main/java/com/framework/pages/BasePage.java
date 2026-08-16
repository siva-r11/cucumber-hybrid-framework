package com.framework.pages;

import com.framework.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Foundation for all Page Objects.
 *
 * <p>Concrete pages hold locators and expose business-level methods (e.g. {@code login()}). The
 * mechanical Selenium actions (type, click, read text) live here and are routed through
 * {@link WaitUtils} so every interaction is implicitly synchronised. This keeps page classes
 * declarative and free of raw waits, and gives us a single place to add resilience (ret/JS
 * fallbacks) later.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected BasePage(WebDriver driver) {
        this.driver = driver;
    }

    // ---------- Common actions ----------

    @Step("Type '{text}' into {locator}")
    protected void type(By locator, String text) {
        WebElement el = WaitUtils.visible(driver, locator);
        el.clear();
        el.sendKeys(text);
    }

    @Step("Click {locator}")
    protected void click(By locator) {
        WaitUtils.clickable(driver, locator).click();
    }

    @Step("Read text from {locator}")
    protected String getText(By locator) {
        return WaitUtils.visible(driver, locator).getText();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return WaitUtils.visible(driver, locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void open(String url) {
        log.info("Navigating to {}", url);
        driver.get(url);
    }

    /** Inject a value into localStorage — used for token-based session seeding. */
    protected void setLocalStorage(String key, String value) {
        ((JavascriptExecutor) driver)
                .executeScript("window.localStorage.setItem(arguments[0], arguments[1]);", key, value);
    }

    protected String getTitle() {
        return driver.getTitle();
    }
}
