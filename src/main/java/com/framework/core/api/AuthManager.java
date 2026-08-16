package com.framework.core.api;

import com.framework.config.ConfigManager;
import com.framework.config.FrameworkConfig;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Central authentication authority shared by BOTH the API and UI layers.
 *
 * <p>Tokens are fetched once per user and cached (thread-safe) for the JVM run, mirroring the
 * "storage state reuse" pattern from tools like Playwright: authenticate once, reuse everywhere.
 * <ul>
 *   <li><b>API tests</b> call {@link #getToken()} and pass it to
 *       {@link RequestSpecFactory#authenticated(String)}.</li>
 *   <li><b>UI tests</b> can seed the browser's storage/cookies with the same token to skip the
 *       login screen (see {@code LoginPage#loginViaToken}), keeping auth consistent across layers.</li>
 * </ul>
 *
 * <p>The demo target ({@code reqres.in}) returns a static token; the code is written so a real
 * OAuth/JWT endpoint slots in by only changing the request/extraction below.
 */
public final class AuthManager {

    private static final Logger log = LoggerFactory.getLogger(AuthManager.class);

    /** token cache keyed by username so multiple identities can coexist. */
    private static final Map<String, String> TOKEN_CACHE = new ConcurrentHashMap<>();

    private AuthManager() {
    }

    /** Token for the default configured user. */
    public static String getToken() {
        FrameworkConfig config = ConfigManager.get();
        return getToken(config.authUsername(), config.authPassword());
    }

    /** Token for a specific identity, generated on first use then cached. */
    public static String getToken(String username, String password) {
        return TOKEN_CACHE.computeIfAbsent(username, u -> generateToken(u, password));
    }

    private static String generateToken(String username, String password) {
        FrameworkConfig config = ConfigManager.get();
        log.info("Generating auth token for user '{}'", username);

        Response response = RestAssured
                .given()
                .baseUri(config.apiBaseUrl())
                .contentType("application/json")
                .body(Map.of("email", username, "password", password))
                .post(config.authTokenEndpoint());

        if (response.statusCode() != 200) {
            throw new IllegalStateException(
                    "Token generation failed (HTTP " + response.statusCode() + "): " + response.asString());
        }
        String token = response.jsonPath().getString("token");
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("Auth endpoint returned no token: " + response.asString());
        }
        log.info("Auth token acquired for '{}'", username);
        return token;
    }

    /** Test-support hook to force a fresh token (e.g. after a simulated expiry). */
    public static void invalidate(String username) {
        TOKEN_CACHE.remove(username);
    }
}
