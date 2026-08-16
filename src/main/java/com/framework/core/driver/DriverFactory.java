package com.framework.core.driver;

import com.framework.config.ConfigManager;
import com.framework.config.FrameworkConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * Creates configured {@link WebDriver} instances for local or remote (Selenium Grid) execution.
 *
 * <p>The factory only knows how to <em>build</em> a driver; lifecycle/threading is owned by
 * {@link DriverManager}. This separation keeps browser-option logic isolated and testable.
 */
public final class DriverFactory {

    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);

    private DriverFactory() {
    }

    public static WebDriver create(BrowserType browserType) {
        FrameworkConfig config = ConfigManager.get();
        boolean headless = config.headless();

        MutableCapabilities options = switch (browserType) {
            case CHROME -> chromeOptions(headless);
            case FIREFOX -> firefoxOptions(headless);
            case EDGE -> edgeOptions(headless);
        };

        WebDriver driver = config.remoteEnabled()
                ? createRemote(config.remoteUrl(), options)
                : createLocal(browserType, options);

        applyTimeouts(driver, config);
        log.info("Created {} driver (headless={}, remote={})",
                browserType, headless, config.remoteEnabled());
        return driver;
    }

    private static WebDriver createLocal(BrowserType browserType, MutableCapabilities options) {
        return switch (browserType) {
            case CHROME -> {
                WebDriverManager.chromedriver().setup();
                yield new org.openqa.selenium.chrome.ChromeDriver((ChromeOptions) options);
            }
            case FIREFOX -> {
                WebDriverManager.firefoxdriver().setup();
                yield new org.openqa.selenium.firefox.FirefoxDriver((FirefoxOptions) options);
            }
            case EDGE -> {
                WebDriverManager.edgedriver().setup();
                yield new org.openqa.selenium.edge.EdgeDriver((EdgeOptions) options);
            }
        };
    }

    private static WebDriver createRemote(String remoteUrl, MutableCapabilities options) {
        try {
            return new RemoteWebDriver(new URL(remoteUrl), options);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Invalid remote WebDriver URL: " + remoteUrl, e);
        }
    }

    private static ChromeOptions chromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--remote-allow-origins=*",
                "--disable-notifications", "--disable-gpu");
        if (headless) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }
        return options;
    }

    private static FirefoxOptions firefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless", "--width=1920", "--height=1080");
        }
        return options;
    }

    private static EdgeOptions edgeOptions(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized", "--remote-allow-origins=*");
        if (headless) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }
        return options;
    }

    private static void applyTimeouts(WebDriver driver, FrameworkConfig config) {
        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(config.implicitWaitSeconds()));
        driver.manage().timeouts()
                .pageLoadTimeout(Duration.ofSeconds(config.pageLoadTimeoutSeconds()));
    }
}
