package com.framework.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.framework.constants.FrameworkConstants;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * JSON Schema validation for API responses — the Java counterpart to Ajv/Zod in the JS world.
 *
 * <p>We use Draft 2020-12 schemas stored under {@code src/test/resources/schemas}. Validation
 * failures raise an {@link AssertionError} with a readable, aggregated list of violations so a
 * failing contract test points straight at the offending field(s).
 */
public final class SchemaValidator {

    private static final Logger log = LoggerFactory.getLogger(SchemaValidator.class);
    private static final JsonSchemaFactory FACTORY =
            JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);

    private SchemaValidator() {
    }

    /**
     * Validate a response body against a schema file located in the schemas directory.
     *
     * @param responseBody    raw JSON response
     * @param schemaFileName  file name (e.g. {@code user-schema.json})
     */
    @SuppressWarnings("null")
    public static void validate(String responseBody, String schemaFileName) {
        JsonNode responseNode = readTree(responseBody);
        JsonSchema schema = loadSchema(schemaFileName);

        Set<ValidationMessage> errors = schema.validate(responseNode);
        if (!errors.isEmpty()) {
            String details = errors.stream()
                    .map(ValidationMessage::getMessage)
                    .collect(Collectors.joining("\n  - ", "  - ", ""));
            throw new AssertionError(
                    "Response failed schema validation against '" + schemaFileName + "':\n" + details);
        }
        log.info("Response conforms to schema '{}'", schemaFileName);
    }

    /**
     * Non-throwing variant returning whether the body is valid. Useful for soft checks.
     */
    public static boolean isValid(String responseBody, String schemaFileName) {
        JsonNode responseNode = readTree(responseBody);
        return loadSchema(schemaFileName).validate(responseNode).isEmpty();
    }

    private static JsonNode readTree(String body) {
        try {
            return JsonUtils.mapper().readTree(body);
        } catch (Exception e) {
            throw new AssertionError("Response body is not valid JSON:\n" + body, e);
        }
    }

    private static JsonSchema loadSchema(String schemaFileName) {
        Path schemaPath = Path.of(FrameworkConstants.SCHEMA_PATH, schemaFileName);
        try {
            // Prefer classpath (works when packaged in a jar), fall back to filesystem path.
            InputStream cp = SchemaValidator.class.getClassLoader()
                    .getResourceAsStream("schemas/" + schemaFileName);
            if (cp != null) {
                return FACTORY.getSchema(cp);
            }
            return FACTORY.getSchema(Files.readString(schemaPath));
        } catch (Exception e) {
            throw new IllegalStateException("Unable to load schema: " + schemaFileName, e);
        }
    }
}
