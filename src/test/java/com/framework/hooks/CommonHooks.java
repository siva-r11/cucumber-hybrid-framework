package com.framework.hooks;

import com.framework.core.context.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Hooks that apply to EVERY scenario (UI, API and hybrid).
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Establish per-scenario logging context (scenario name in MDC) for traceable logs.</li>
 *   <li>Enable REST Assured's URL-encoding safety globally, once.</li>
 *   <li>Reset the shared {@link ScenarioContext} at scenario end.</li>
 * </ul>
 *
 * <p>{@code TestContext} is constructor-injected by PicoContainer, so the very same instance is
 * shared with the step definitions of this scenario.
 */
public class CommonHooks {

    private static final Logger log = LoggerFactory.getLogger(CommonHooks.class);
    private final TestContext context;

    public CommonHooks(TestContext context) {
        this.context = context;
    }

    @Before(order = 0)
    public void beforeAny(Scenario scenario) {
        MDC.put("scenario", scenario.getName());
        RestAssured.urlEncodingEnabled = true;
        log.info("========== START: {} ==========", scenario.getName());
    }

    @After(order = 0)
    public void afterAny(Scenario scenario) {
        log.info("========== END: {} -> {} ==========",
                scenario.getName(), scenario.getStatus());
        context.getScenarioContext().clear();
        MDC.clear();
    }
}
