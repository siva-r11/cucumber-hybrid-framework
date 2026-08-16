@ui @regression
Feature: Secure area login
  As a registered user
  I want to log in through the web UI
  So that I can access the secure area

  Background:
    Given the login page is open

  @smoke
  Scenario: Successful login with valid credentials
    When I log in with username "tomsmith" and password "SuperSecretPassword!"
    Then I should land on the secure area
    And the confirmation banner should contain "You logged into a secure area!"

  Scenario Outline: Login is rejected for invalid credentials
    When I log in with username "<username>" and password "<password>"
    Then the login error banner should contain "<message>"

    Examples:
      | username   | password   | message                  |
      | tomsmith   | wrongpass  | Your password is invalid |
      | wronguser  | whatever   | Your username is invalid |
