package com.framework.utils;

import com.framework.constants.FrameworkConstants;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
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
        private static final ThreadLocal<List<byte[]>> STEP_SCREENSHOTS =
            ThreadLocal.withInitial(ArrayList::new);

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

    public static void startScenario() {
        STEP_SCREENSHOTS.get().clear();
    }

    public static void captureStep(WebDriver driver, Scenario scenario) {
        if (!(driver instanceof TakesScreenshot ts)) {
            return;
        }
        byte[] png = ts.getScreenshotAs(OutputType.BYTES);
        List<byte[]> screenshots = STEP_SCREENSHOTS.get();
        screenshots.add(png);
        String stepName = "step-" + String.format("%02d", screenshots.size());
        scenario.attach(png, "image/png", stepName);
        Allure.addAttachment(scenario.getName() + " - " + stepName,
                "image/png", new ByteArrayInputStream(png), "png");
    }

    public static void writeStepPdf(String scenarioName) {
        List<byte[]> screenshots = STEP_SCREENSHOTS.get();
        if (screenshots.isEmpty()) {
            STEP_SCREENSHOTS.remove();
            return;
        }

        String safeName = scenarioName.replaceAll("[^a-zA-Z0-9-_]", "_");
        Path pdfFile = Path.of(FrameworkConstants.STEP_PDF_DIR)
                .resolve(safeName + "_" + LocalDateTime.now().format(TS) + ".pdf");
        try {
            Files.createDirectories(pdfFile.getParent());
            try (PDDocument document = new PDDocument()) {
                for (byte[] png : screenshots) {
                    PDPage page = new PDPage(PDRectangle.LETTER);
                    document.addPage(page);
                    PDImageXObject image = PDImageXObject.createFromByteArray(document, png, safeName);
                    float margin = 20;
                    float maxWidth = page.getMediaBox().getWidth() - (margin * 2);
                    float maxHeight = page.getMediaBox().getHeight() - (margin * 2);
                    float scale = Math.min(maxWidth / image.getWidth(), maxHeight / image.getHeight());
                    float width = image.getWidth() * scale;
                    float height = image.getHeight() * scale;
                    float x = (page.getMediaBox().getWidth() - width) / 2;
                    float y = (page.getMediaBox().getHeight() - height) / 2;
                    try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                        content.drawImage(image, x, y, width, height);
                    }
                }
                document.save(pdfFile.toFile());
            }
            try (var pdfStream = Files.newInputStream(pdfFile)) {
                Allure.addAttachment(scenarioName + " - step screenshots",
                        "application/pdf", pdfStream, "pdf");
            }
            log.info("Step screenshot PDF saved: {}", pdfFile);
        } catch (IOException e) {
            log.error("Failed to create step screenshot PDF", e);
        } finally {
            STEP_SCREENSHOTS.remove();
        }
    }
}
