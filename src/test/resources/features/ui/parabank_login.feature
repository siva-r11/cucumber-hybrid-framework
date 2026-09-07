@ui @regression @parabank @parabank-login
Feature: ParaBank customer login
  As a registered customer
  I want to log in to ParaBank
  So that I can view my accounts

  Scenario: Log in with newly registered customer credentials
    Given a newly registered ParaBank customer is on the login page
    When I log in with the newly registered ParaBank credentials
    Then the ParaBank accounts overview should be displayed
