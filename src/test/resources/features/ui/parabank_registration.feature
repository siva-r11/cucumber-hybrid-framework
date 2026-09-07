@ui @regression @parabank
Feature: ParaBank customer registration
  As a new customer
  I want to register for ParaBank
  So that I can access the login page with my new account

  Scenario: Register with generated dummy data and navigate to login
    Given the ParaBank registration page is open
    When I register a new ParaBank customer with generated dummy data
    Then the ParaBank account should be created
    When I navigate to the ParaBank login page
    Then the ParaBank login page should be displayed