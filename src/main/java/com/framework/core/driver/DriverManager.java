package com.framework.core.driver;

import org.openqa.selenium.WebDriver;

/**
 * Owns the WebDriver lifecycle per thread so that parallel scenarios never share a browser.
 *
 * <p>Cucumber's JUnit Platform engine runs scenarios on a pool of threads. A {@link ThreadLocal}
 * ensures each thread has its own driver instance, which is the standard pattern for safe
 * parallel UI execution. Hooks are responsible for calling {@link #set}/{@link #quit} around
 * each scenario.
 */
public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void set(WebDriver driver) {
        DRIVER.set(driver);
    }

    public static WebDriver get() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException(
                    "No WebDriver bound to the current thread. Ensure a @Before UI hook created it.");
        }
        return driver;
    }

    public static boolean isInitialised() {
        return DRIVER.get() != null;
    }

    /**
     * Quits the driver and detaches it from the current thread. Safe to call multiple times.
     */
    public static void quit() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                DRIVER.remove();
            }
        }
    }
}
