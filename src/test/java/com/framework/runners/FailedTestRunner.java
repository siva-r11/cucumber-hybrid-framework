package com.framework.runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectFile;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

/**
 * Reruns ONLY the scenarios that failed in the previous run.
 *
 * <p>The main {@link TestRunner} writes a {@code rerun} file listing failed scenarios; this
 * runner consumes it via {@code @SelectFile}. Typical CI flow:
 * <pre>
 *   mvn test                              # full run, produces failed-scenarios.txt
 *   mvn test -Dtest=FailedTestRunner      # second pass over failures only
 * </pre>
 * This is the standard, deterministic way to distinguish flaky failures from real ones without
 * re-running the entire (possibly slow) suite.
 */
@Suite
@IncludeEngines("cucumber")
@SelectFile("target/rerun/failed-scenarios.txt")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME,
        value = "com.framework.stepdefinitions, com.framework.hooks")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,
        value = "pretty,"
                + "html:target/cucumber-reports/rerun.html,"
                + "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm")
public class FailedTestRunner {
}
