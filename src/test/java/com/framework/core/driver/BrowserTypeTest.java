package com.framework.core.driver;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BrowserTypeTest {

    @Test
    void parsesSupportedBrowsersWithoutCaseSensitivity() {
        assertThat(BrowserType.from("chrome")).isEqualTo(BrowserType.CHROME);
        assertThat(BrowserType.from("FiReFoX")).isEqualTo(BrowserType.FIREFOX);
        assertThat(BrowserType.from("EDGE")).isEqualTo(BrowserType.EDGE);
    }

    @Test
    void rejectsUnsupportedBrowser() {
        assertThatThrownBy(() -> BrowserType.from("safari"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unsupported browser 'safari'");
    }
}