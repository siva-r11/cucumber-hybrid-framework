package com.framework.constants;

/**
 * Centralised, immutable framework constants. Keeping magic strings/paths here avoids
 * duplication and makes global changes a one-line edit.
 */
public final class FrameworkConstants {

    private FrameworkConstants() {
    }

    // ---------- Filesystem paths ----------
    public static final String USER_DIR = System.getProperty("user.dir");
    public static final String RESOURCES_PATH = USER_DIR + "/src/test/resources";
    public static final String TEST_DATA_PATH = RESOURCES_PATH + "/testdata";
    public static final String SCHEMA_PATH = RESOURCES_PATH + "/schemas";
    public static final String SCREENSHOT_DIR = USER_DIR + "/target/screenshots";
    public static final String ALLURE_RESULTS_DIR = USER_DIR + "/target/allure-results";

    // ---------- Auth context keys ----------
    public static final String AUTH_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    // ---------- Timeouts ----------
    public static final int POLLING_MILLIS = 250;
}
