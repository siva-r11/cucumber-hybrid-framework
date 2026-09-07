package com.framework.config;

import org.aeonbits.owner.Config;

/**
 * Strongly-typed configuration contract backed by the Owner library.
 *
 * <p>Sources are resolved top-down (first match wins), which enables layered overrides:
 * <ol>
 *   <li><b>System properties</b> — e.g. {@code -Denv=prod}, ideal for CI overrides.</li>
 *   <li><b>Environment variables</b> — useful for secrets injected by the pipeline.</li>
 *   <li><b>Environment-specific file</b> — {@code config/{env}.properties}. The {@code ${env}}
 *       placeholder is itself resolved from the sources above (defaulting to {@code test}).</li>
 *   <li><b>Common file</b> — {@code config/common.properties} for env-agnostic defaults.</li>
 * </ol>
 *
 * This means a value defined in {@code prod.properties} can still be overridden at runtime
 * with {@code -Dweb.base.url=...} without touching any file.
 */
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "system:env",
        "classpath:config/${env}.properties",
        "classpath:config/common.properties"
})
public interface FrameworkConfig extends Config {

    // ---------- Environment selector ----------
    @Key("env")
    @DefaultValue("test")
    String env();

    // ---------- Web / UI ----------
    @Key("web.base.url")
    String webBaseUrl();

    @Key("parabank.base.url")
    @DefaultValue("https://parabank.parasoft.com/parabank")
    String paraBankBaseUrl();

    @Key("browser")
    @DefaultValue("chrome")
    String browser();

    @Key("headless")
    @DefaultValue("true")
    boolean headless();

    @Key("implicit.wait.seconds")
    @DefaultValue("0")
    int implicitWaitSeconds();

    @Key("explicit.wait.seconds")
    @DefaultValue("15")
    int explicitWaitSeconds();

    @Key("page.load.timeout.seconds")
    @DefaultValue("30")
    int pageLoadTimeoutSeconds();

    @Key("remote.enabled")
    @DefaultValue("false")
    boolean remoteEnabled();

    @Key("remote.url")
    @DefaultValue("http://localhost:4444/wd/hub")
    String remoteUrl();

    // ---------- API ----------
    @Key("api.base.url")
    String apiBaseUrl();

    @Key("api.timeout.ms")
    @DefaultValue("30000")
    int apiTimeoutMs();

    @Key("api.relaxed.ssl")
    @DefaultValue("false")
    boolean apiRelaxedSsl();

    @Key("api.key")
    @DefaultValue("")
    String apiKey();

    // ---------- Auth ----------
    @Key("auth.username")
    String authUsername();

    @Key("auth.password")
    String authPassword();

    @Key("auth.token.endpoint")
    String authTokenEndpoint();

    // ---------- Retry ----------
    @Key("retry.count")
    @DefaultValue("1")
    int retryCount();
}
