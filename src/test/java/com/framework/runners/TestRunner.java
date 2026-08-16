package com.framework.runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

/**
 * Primary Cucumber runner driven by the JUnit 5 Platform (the modern replacement for the old
 * {@code @RunWith(Cucumber.class)} JUnit 4 runner).
 *
 * <p>Configuration precedence: values in {@code junit-platform.properties} apply by default and
 * can be overridden per-run via {@code -D} system properties wired in the Surefire plugin
 * (tags, parallelism, etc.). Reporting plugins emit Cucumber HTML/JSON <em>and</em> feed Allure.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME,
        value = "com.framework.stepdefinitions, com.framework.hooks")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,
        value = "pretty,"
                + "html:target/cucumber-reports/cucumber.html,"
                + "json:target/cucumber-reports/cucumber.json,"
                + "rerun:target/rerun/failed-scenarios.txt,"
                + "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm")
public class TestRunner {
    // Body intentionally empty — configuration is declarative via annotations.
}
