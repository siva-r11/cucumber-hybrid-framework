package com.framework.services;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all API service/client classes.
 *
 * <p>The service layer is the API-side analogue of the Page Object Model: each concrete service
 * encapsulates one resource's endpoints so tests speak in domain terms
 * ({@code userService.create(user)}) rather than raw HTTP. Shared cross-cutting behaviour
 * (logging, response peeking) lives here so subclasses stay tiny.
 */
public abstract class BaseService {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Logs and returns the response. Kept as a distinct step so the Allure timeline shows a
     * clear "received response" node with status and latency.
     */
    @Step("Received response: status {response.statusCode}")
    protected Response logResponse(Response response) {
        log.info("Response status={} time={}ms",
                response.statusCode(), response.time());
        return response;
    }

    protected RequestSpecification withBody(RequestSpecification spec, Object body) {
        return spec.body(body);
    }
}
