@ui @regression @orangehrm @orangehrm-dashboard
Feature: OrangeHRM dashboard
  As an OrangeHRM user
  I want to view the dashboard after signing in
  So that I can access the application workspace

  Background:
    Given the OrangeHRM login page is open
    When I log in to OrangeHRM with username "Admin" and password "admin123"

  @smoke
  Scenario: Dashboard layout is displayed after login
    Then the OrangeHRM dashboard layout should be visible