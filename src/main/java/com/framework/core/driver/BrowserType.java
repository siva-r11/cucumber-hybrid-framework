package com.framework.core.driver;

import java.util.Arrays;

/**
 * Supported browsers for cross-browser execution. Parsing is case-insensitive so that
 * {@code -Dbrowser=Chrome} and {@code -Dbrowser=chrome} both resolve correctly.
 */
public enum BrowserType {
    CHROME,
    FIREFOX,
    EDGE;

    public static BrowserType from(String value) {
        return Arrays.stream(values())
                .filter(b -> b.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported browser '" + value + "'. Supported: " + Arrays.toString(values())));
    }
}
