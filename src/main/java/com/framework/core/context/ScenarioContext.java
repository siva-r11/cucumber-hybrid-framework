package com.framework.core.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A per-scenario key/value bag for sharing state between step definitions.
 *
 * <p>Cucumber + PicoContainer creates a fresh instance of every step-definition class per
 * scenario and injects shared dependencies through constructors. Because this class is
 * constructor-injected into each step-def, all steps in a scenario see the <em>same</em>
 * context, while different (possibly parallel) scenarios get isolated contexts. This is the
 * idiomatic Cucumber alternative to static state and is what makes hybrid UI+API flows possible
 * (e.g. an API step stores a created user id that a later UI step reads).
 */
public class ScenarioContext {

    private final Map<String, Object> store = new HashMap<>();

    public void set(String key, Object value) {
        store.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) store.get(key);
    }

    public <T> Optional<T> getOptional(String key) {
        return Optional.ofNullable(get(key));
    }

    public boolean contains(String key) {
        return store.containsKey(key);
    }

    public void clear() {
        store.clear();
    }
}
