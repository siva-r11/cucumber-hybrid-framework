package com.framework.services;

import com.framework.core.api.RequestSpecFactory;
import com.framework.models.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * API client for the {@code /users} resource.
 *
 * <p>Encapsulates every user endpoint behind intention-revealing methods. Tests never build
 * URLs or specs directly — they call these methods, which keeps endpoint knowledge in one place
 * and makes an API change a single-file edit.
 */
public class UserService extends BaseService {

    private static final String USERS = "/users";
    private static final String USER_BY_ID = "/users/{id}";

    /** GET a single user by id. */
    @Step("Get user by id: {id}")
    public Response getUser(String id) {
        log.info("GET {}/{}", USERS, id);
        return logResponse(
                given(RequestSpecFactory.base())
                        .pathParam("id", id)
                        .when()
                        .get(USER_BY_ID));
    }

    /** GET a page of users. */
    @Step("List users on page {page}")
    public Response listUsers(int page) {
        log.info("GET {} page={}", USERS, page);
        return logResponse(
                given(RequestSpecFactory.base())
                        .queryParam("page", page)
                        .when()
                        .get(USERS));
    }

    /** POST create a user (authenticated). */
    @Step("Create user: {user.name}")
    public Response createUser(User user, String token) {
        log.info("POST {} name={}", USERS, user.getName());
        return logResponse(
                given(RequestSpecFactory.authenticated(token))
                        .body(user)
                        .when()
                        .post(USERS));
    }

    /** PUT update a user (authenticated). */
    @Step("Update user {id}")
    public Response updateUser(String id, User user, String token) {
        log.info("PUT {}/{}", USERS, id);
        return logResponse(
                given(RequestSpecFactory.authenticated(token))
                        .pathParam("id", id)
                        .body(user)
                        .when()
                        .put(USER_BY_ID));
    }

    /** DELETE a user (authenticated). */
    @Step("Delete user {id}")
    public Response deleteUser(String id, String token) {
        log.info("DELETE {}/{}", USERS, id);
        return logResponse(
                given(RequestSpecFactory.authenticated(token))
                        .pathParam("id", id)
                        .when()
                        .delete(USER_BY_ID));
    }
}
