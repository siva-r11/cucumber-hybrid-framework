package com.framework.utils;

import com.framework.constants.FrameworkConstants;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Captures screenshots for UI failures and attaches them to the Allure report.
 *
 * <p>The byte[] is both persisted to {@code target/screenshots} (for CI artifact upload) and
 * streamed into Allure so the failure is visible directly in the report.
 */
public final class ScreenshotUtils {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static final DateTimeFormatter TS =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtils() {
    }

    /**
     * Take a screenshot, save it to disk, and attach it to Allure.
     *
     * @param driver      active driver
     * @param scenarioName sanitised scenario/step name used in the file name
     */
    public static void captureAndAttach(WebDriver driver, String scenarioName) {
        if (!(driver instanceof TakesScreenshot ts)) {
            log.warn("Driver does not support screenshots; skipping capture.");
            return;
        }
        byte[] png = ts.getScreenshotAs(OutputType.BYTES);

        // Attach to Allure (visible in report)
        Allure.addAttachment(scenarioName + " - screenshot",
                "image/png", new ByteArrayInputStream(png), "png");

        // Persist to disk (CI artifact)
        String safeName = scenarioName.replaceAll("[^a-zA-Z0-9-_]", "_");
        String fileName = safeName + "_" + LocalDateTime.now().format(TS) + ".png";
        try {
            Path dir = Path.of(FrameworkConstants.SCREENSHOT_DIR);
            Files.createDirectories(dir);
            Path file = dir.resolve(fileName);
            Files.write(file, png);
            log.info("Screenshot saved: {}", file);
        } catch (IOException e) {
            log.error("Failed to persist screenshot", e);
        }
    }
}
