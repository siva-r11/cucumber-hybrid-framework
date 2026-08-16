package com.framework.hooks;

import com.framework.config.ConfigManager;
import com.framework.core.driver.BrowserType;
import com.framework.core.driver.DriverFactory;
import com.framework.core.driver.DriverManager;
import com.framework.utils.ScreenshotUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lifecycle hooks scoped to UI scenarios only (tag {@code @ui}).
 *
 * <p>Cucumber runs {@code @Before}/{@code @After} around every matching scenario, giving us the
 * setup/teardown "fixture" behaviour: a fresh, thread-local browser per scenario and guaranteed
 * cleanup. Ordering ({@code order = 10}) keeps UI setup ahead of generic hooks. Because the
 * driver lives in {@link DriverManager}'s ThreadLocal, parallel scenarios never collide.
 */
public class UiHooks {

    private static final Logger log = LoggerFactory.getLogger(UiHooks.class);

    @Before(value = "@ui", order = 10)
    public void startBrowser(Scenario scenario) {
        String browser = ConfigManager.get().browser();
        log.info("[UI SETUP] '{}' -> launching {}", scenario.getName(), browser);
        DriverManager.set(DriverFactory.create(BrowserType.from(browser)));
    }

    @After(value = "@ui", order = 10)
    public void stopBrowser(Scenario scenario) {
        try {
            if (scenario.isFailed() && DriverManager.isInitialised()) {
                log.warn("[UI TEARDOWN] '{}' FAILED -> capturing screenshot", scenario.getName());
                ScreenshotUtils.captureAndAttach(DriverManager.get(), scenario.getName());
            }
        } finally {
            DriverManager.quit();
            log.info("[UI TEARDOWN] '{}' -> browser closed", scenario.getName());
        }
    }
}
