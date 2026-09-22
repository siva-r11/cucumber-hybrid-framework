@ui @regression @orangehrm @orangehrm-admin
Feature: OrangeHRM Admin user management
  As an OrangeHRM administrator
  I want to manage system users
  So that I can control application access

  Background:
    Given the OrangeHRM login page is open
    When I log in to OrangeHRM with username "Admin" and password "admin123"
    And I open the OrangeHRM Admin module

  @smoke
  Scenario: Admin user management page is displayed
    Then the OrangeHRM Admin page should be displayed

  Scenario: Search for an existing Admin user
    When I search Admin users with username "Admin", role "Admin", and status "Enabled"
    Then the Admin results should contain a user named "Admin"

  Scenario: Search returns no users for an unknown username
    When I search Admin users with username "user-that-does-not-exist-999", role "", and status ""
    Then the Admin results should be empty

  Scenario: Reset Admin user search filters
    When I search Admin users with username "Admin", role "", and status ""
    And I reset the Admin user search
    Then the Admin user search form should be cleared

  Scenario: Add User form shows required field validation
    When I open the Add User form
    Then the Add User form should be displayed
    When I submit the empty Add User form
    Then the Add User form should show required field errors

  Scenario: Cancel Add User returns to user search
    When I open the Add User form
    And I cancel the Add User form
    Then the Admin user search page should be displayed