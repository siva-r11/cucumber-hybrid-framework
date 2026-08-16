@api @regression
Feature: User management API
  As an API consumer
  I want to manage users over REST
  So that user data stays correct and contracts hold

  @smoke
  Scenario: Retrieve an existing user and validate the response contract
    When I request the user with id "2"
    Then the API response status should be 200
    And the response should match the "single-user-schema.json" schema
    And the user's email should be "janet.weaver@reqres.in"

  @smoke
  Scenario: Create a new user
    Given an authenticated API session
    When I create a user named "Ada Lovelace" with job "Automation Engineer"
    Then the API response status should be 201
    And the response should match the "create-user-schema.json" schema
    And the created user's name should be "Ada Lovelace"

  Scenario: Requesting a non-existent user returns 404
    When I request the user with id "999"
    Then the API response status should be 404
