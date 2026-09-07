@ui @regression @parabank
Feature: ParaBank customer registration
  As a new customer
  I want to register for ParaBank
  So that I can create a new account

  Scenario: Register with generated dummy data
    Given the ParaBank registration page is open
    When I register a new ParaBank customer with generated dummy data
    Then the ParaBank account should be created
