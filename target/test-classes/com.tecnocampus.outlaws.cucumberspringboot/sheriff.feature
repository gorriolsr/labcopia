Feature: Manage Sheriffs

  Scenario: Register a sheriff successfully
    When I create a sheriff with:
      | name       | salary | captures | eliminations |
      | Wyatt Earp | 1000   | 0        | 0            |
    Then the response status is "201"
    And the response body contains:
      | name       | salary | captures | eliminations |
      | Wyatt Earp | 1000   | 0        | 0            |
    And the response body should have "id"
    And the response body should have "createdAt"

  Scenario: Retrieve a sheriff
    Given a sheriff exists with:
      | name       | salary | captures | eliminations |
      | Wyatt Earp | 1000   | 0        | 0            |
    When I retrieve the sheriff by ID
    Then the response status is "200"
    And the response body contains:
      | name       | salary | captures | eliminations |
      | Wyatt Earp | 1000   | 0        | 0            |


  Scenario: Fail to update a sheriff with invalid salary
    Given a sheriff exists with:
      | name       | salary | captures | eliminations |
      | Wyatt Earp | 1000   | 0        | 0            |
    When I update the sheriff with:
      | name       | salary |
      | Wyatt Earp | -500   |
    Then the response status is "422"


  Scenario: Sheriff should not be retrievable after deletion
    Given a sheriff exists with:
      | name       | salary | captures | eliminations |
      | Wyatt Earp | 1000   | 0        | 0            |
    When I delete the sheriff
    Then the response status is "204"
    When I retrieve the sheriff by ID
    Then the response status is "404"

  Scenario: Fail to update a non-existing sheriff
    When I update a sheriff that does not exist with:
      | name       | salary |
      | Ghost Name | 2000   |
    Then the response status is "404"

  Scenario: Sheriff should not appear in the list after deletion
    Given a sheriff exists with:
      | name       | salary | captures | eliminations |
      | Pat Garrett| 1200   | 5        | 2            |
      | Wyatt Earp | 1000   | 0        | 0            |
    When I delete the sheriff with ID for Wyatt Earp
    Then the response status is "204"
    When I list all sheriffs
    Then the response status is "200"
    And the response should contain a list with 1 item


  Scenario: Capture an outlaw successfully
    Given an outlaw created with:
      | name        | bounty | status |
      | Jesse James | 5000   | FREE   |
    And a sheriff exists with:
      | name       | salary | captures | eliminations |
      | Wyatt Earp | 1000   | 0        | 0            |

    When the sheriff captures the outlaw
    Then the response status is "200"
    And the response body contains:
      | name        | bounty | status   |
      | Jesse James | 5000   | CAPTURED |
    When I retrieve the sheriff by ID
    And the response body should contain the sheriff resource with:
      | name       | salary | captures | eliminations |
      | Wyatt Earp | 6000   | 1        | 0            |
    When the sheriff eliminates the outlaw
    And the response body contains:
      | name        | bounty | status      |
      | Jesse James | 5000   | ELIMINATED |
    When I retrieve the sheriff by ID
    And the response body should contain the sheriff resource with:
      | name       | salary | captures | eliminations |
      | Wyatt Earp | 6000   | 1        | 1            |
    Then the response status is "200"

  Scenario: Eliminate an outlaw successfully
    Given an outlaw created with:
      | name        | bounty | status |
      | Jesse James | 5000   | FREE      |
    And a sheriff exists with:
      | name       | salary | captures | eliminations |
      | Wyatt Earp | 1000   | 0        | 0            |
    When the sheriff eliminates the outlaw
    Then the response status is "200"
    And the response body contains:
      | name        | bounty | status    |
      | Jesse James | 5000   | ELIMINATED  |
    When I retrieve the sheriff by ID
    And the response body should contain the sheriff resource with:
      | name       | salary | captures | eliminations |
      | Wyatt Earp | 1000   | 0        | 1            |
    When the sheriff captures the outlaw
    Then the response status is "400"

  Scenario: Prevent capturing an already captured outlaw
    Given an outlaw created with:
      | name        | bounty | status   |
      | Jesse James | 5000   | CAPTURED |
    And a sheriff exists with:
      | name       | salary | captures | eliminations |
      | Wyatt Earp | 1000   | 0        | 0            |
    When the sheriff captures the outlaw
    Then the response status is "400"

  Scenario: Prevent eliminating an already eliminated outlaw
    Given an outlaw created with:
      | name        | bounty | status     |
      | Jesse James | 5000   | ELIMINATED |
    And a sheriff exists with:
      | name       | salary | captures | eliminations |
      | Wyatt Earp | 1000   | 0        | 0            |
    When the sheriff eliminates the outlaw
    Then the response status is "400"

  Scenario: Prevent capturing an already eliminated outlaw
    Given an outlaw created with:
      | name        | bounty | status    |
      | Jesse James | 5000   | ELIMINATED  |
    And a sheriff exists with:
      | name       | salary | captures | eliminations |
      | Wyatt Earp | 1000   | 0        | 0            |
    When the sheriff captures the outlaw
    Then the response status is "400"


  Scenario: Prevent status change for non-existing outlaw
    Given a sheriff exists with:
      | name       | salary | captures | eliminations |
      | Wyatt Earp | 1000   | 0        | 0            |
    When the sheriff tries to capture a non-existing outlaw
    Then the response status is "404"

    When the sheriff tries to eliminate a non-existing outlaw
    Then the response status is "404"
