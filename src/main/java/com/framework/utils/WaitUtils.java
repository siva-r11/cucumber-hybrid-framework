package com.framework.utils;

import com.framework.config.ConfigManager;
import com.framework.constants.FrameworkConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Explicit-wait helpers. We deliberately favour explicit waits over implicit waits (implicit
 * is left at 0 in config) because mixing the two produces unpredictable timeouts. Every wait
 * here polls until the condition is met or the configured timeout elapses.
 */
public final class WaitUtils {

    private WaitUtils() {
    }

    private static WebDriverWait wait(WebDriver driver) {
        int timeout = ConfigManager.get().explicitWaitSeconds();
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        w.pollingEvery(Duration.ofMillis(FrameworkConstants.POLLING_MILLIS));
        return w;
    }

    public static WebElement visible(WebDriver driver, By locator) {
        return wait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement clickable(WebDriver driver, By locator) {
        return wait(driver).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement present(WebDriver driver, By locator) {
        return wait(driver).until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static List<WebElement> allVisible(WebDriver driver, By locator) {
        return wait(driver).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public static boolean invisible(WebDriver driver, By locator) {
        return wait(driver).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static boolean urlContains(WebDriver driver, String fragment) {
        return wait(driver).until(ExpectedConditions.urlContains(fragment));
    }

    public static boolean textPresent(WebDriver driver, By locator, String text) {
        return wait(driver).until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }
}
