@hybrid @regression
Feature: Hybrid UI + API user onboarding
  As a QA engineer
  I want to prepare state via the API and verify it through the UI
  So that UI tests are fast, reliable, and independent of UI-based setup

  @smoke
  Scenario: Seed a user via API, then log into the UI with a shared session
    # --- API setup phase: create data and obtain a shared auth token ---
    Given an authenticated API session
    When I create a user named "Grace Hopper" with job "Compiler Pioneer" via API
    Then the created user id is stored for later steps

    # --- UI verification phase: reuse the SAME token to enter the app ---
    When I open the app and seed the browser session with the shared token
    And I log in with username "tomsmith" and password "SuperSecretPassword!"
    Then I should land on the secure area
    And the confirmation banner should contain "You logged into a secure area!"
