package com.framework.core.context;

import lombok.Getter;

/**
 * The composition root that PicoContainer injects into hooks and step definitions.
 *
 * <p>Rather than scattering many small injectables, we expose one {@code TestContext} that owns
 * the shared, scenario-scoped collaborators. Step-def classes declare {@code TestContext} in
 * their constructor and pull what they need. Keeping this lightweight (it holds the
 * {@link ScenarioContext} and lazily-built page/service factories) keeps wiring simple while
 * preserving per-scenario isolation for parallel runs.
 */
@Getter
public class TestContext {

    private final ScenarioContext scenarioContext;

    public TestContext() {
        this.scenarioContext = new ScenarioContext();
    }
}
