package com.framework.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.framework.constants.FrameworkConstants;
import com.framework.models.User;
import net.datafaker.Faker;

import java.nio.file.Path;

/**
 * Single entry point for test data.
 *
 * <p>Two complementary strategies are supported:
 * <ul>
 *   <li><b>Static fixtures</b> — deterministic data read from {@code testdata/*.json}, ideal
 *       when a test asserts against known values.</li>
 *   <li><b>Generated data</b> — unique, realistic values from Datafaker, ideal for create flows
 *       where uniqueness matters and the exact value is irrelevant.</li>
 * </ul>
 */
public final class TestDataProvider {

    private static final Faker FAKER = new Faker();
    private static final String USERS_FILE =
            Path.of(FrameworkConstants.TEST_DATA_PATH, "users.json").toString();

    private TestDataProvider() {
    }

    /** Load a named user object from the users.json fixture. */
    public static User userFixture(String key) {
        JsonNode root = JsonUtils.fromFile(USERS_FILE, JsonNode.class);
        JsonNode node = root.get(key);
        if (node == null) {
            throw new IllegalArgumentException("No user fixture named '" + key + "' in users.json");
        }
        return JsonUtils.fromString(node.toString(), User.class);
    }

    /** Generate a unique, realistic user for create scenarios. */
    public static User randomUser() {
        return User.builder()
                .name(FAKER.name().fullName())
                .job(FAKER.job().title())
                .email(FAKER.internet().emailAddress())
                .build();
    }
}
