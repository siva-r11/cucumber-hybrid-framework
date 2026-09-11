@ui @regression @orangehrm @orangehrm-login
Feature: OrangeHRM login
  As an OrangeHRM user
  I want to log in through the web UI
  So that I can access the dashboard

  Background:
    Given the OrangeHRM login page is open

  @smoke
  Scenario: Successful login with valid OrangeHRM credentials
    When I log in to OrangeHRM with username "Admin" and password "admin123"
    Then the OrangeHRM dashboard should be displayed

  Scenario: Login is rejected for invalid OrangeHRM credentials
    When I log in to OrangeHRM with username "Admin" and password "wrongpass"
    Then the OrangeHRM login error should contain "Invalid credentials"