package com.framework.core.api;

import com.framework.config.ConfigManager;
import com.framework.config.FrameworkConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Central builder for REST Assured {@link RequestSpecification}s.
 *
 * <p>Every request built here shares: the configured base URI, JSON content negotiation,
 * connection/socket timeouts, request/response logging, and the {@link AllureRestAssured}
 * filter which records full request/response detail into the Allure report. Centralising this
 * means individual service classes stay focused on <em>endpoints</em>, not transport concerns.
 */
public final class RequestSpecFactory {

    private RequestSpecFactory() {
    }

    /** Base spec: base URI, JSON, timeouts, logging, Allure. No auth. */
    public static RequestSpecification base() {
        FrameworkConfig config = ConfigManager.get();

        RestAssuredConfig raConfig = RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", config.apiTimeoutMs())
                        .setParam("http.socket.timeout", config.apiTimeoutMs()));

        return new RequestSpecBuilder()
                .setBaseUri(config.apiBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setConfig(raConfig)
                .addFilter(new AllureRestAssured())
                .log(io.restassured.filter.log.LogDetail.METHOD)
                .log(io.restassured.filter.log.LogDetail.URI)
                .build();
    }

    /** Base spec plus a bearer token in the Authorization header. */
    public static RequestSpecification authenticated(String token) {
        return new RequestSpecBuilder()
                .addRequestSpecification(base())
                .addHeader("Authorization", "Bearer " + token)
                .build();
    }
}
