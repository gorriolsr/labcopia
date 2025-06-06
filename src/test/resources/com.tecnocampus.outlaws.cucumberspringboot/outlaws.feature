Feature: Manage Outlaws

  Scenario: Register a new outlaw successfully
    When I create an outlaw with:
      | name        | bounty | status |
      | Jesse James | 5000   | FREE   |
    Then the response status should be "201"
    And the response body should contain:
      | name        | bounty | status |
      | Jesse James | 5000   | FREE   |
    And the response body should have a generated "id"
    And the response body should have a generated "createdAt"

  Scenario: Retrieve an outlaw
    Given an outlaw exists with:
      | name        | bounty | status |
      | Jesse James | 5000   | FREE   |
    And the outlaw ID is generated
    When I retrieve the outlaw by ID
    Then the response status should be "200"
    And the response body should contain:
      | name        | bounty | status |
      | Jesse James | 5000   | FREE   |
    When I delete the outlaw
    Then the response status should be "204"
    When I list all outlaws
    Then the response status should be "200"
    And the response should contain a list with 0 items

  Scenario: Retrieve a non-existing outlaw
    When I retrieve an outlaw with an invalid ID
    Then the response status should be "404"

  Scenario: List all outlaws
    Given outlaws exist:
      | name          | bounty | status   |
      | Jesse James   | 5000   | FREE     |
      | Billy the Kid | 7000   | CAPTURED |
    When I list all outlaws
    Then the response status should be "200"
    And the response should contain a list with 2 items

  Scenario: Update an outlaw
    Given an outlaw exists with:
      | name        | bounty | status |
      | Jesse James | 5000   | FREE   |
    When I update the outlaw with:
      | name        | bounty | status |
      | Jesse James | 10000  | FREE   |
    Then the response status should be "200"
    And the response body should contain:
      | name        | bounty | status |
      | Jesse James | 10000  | FREE   |

  Scenario: Fail to update an outlaw with invalid data
    Given an outlaw exists with:
      | name        | bounty | status |
      | Jesse James | 5000   | FREE   |
    When I update the outlaw with:
      | name | bounty | status |
      |      | -1000  | FREE   |
    Then the response status should be "422"


  Scenario: Set bounty for an outlaw
    Given an outlaw exists with:
      | name        | bounty | status |
      | Jesse James | 5000   | FREE   |
    And the outlaw ID is generated
    When I update the outlaw bounty with:
      | bounty |
      | 7000   |
    Then the response status should be "200"
    And the response body should contain:
      | name        | bounty | status |
      | Jesse James | 7000   | FREE   |

  Scenario: Fail to set a negative bounty
    Given an outlaw exists with:
      | name        | bounty | status |
      | Jesse James | 5000   | FREE   |
    And the outlaw ID is generated
    When I update the outlaw bounty with:
      | bounty |
      | -5000  |
    Then the response status should be "422"

  Scenario: Find the most wanted outlaw

    Given outlaws exist:
      | name          | bounty | status |
      | Billy the Kid | 10000  | FREE   |
      | Jesse James   | 10000  | FREE   |
      | Wild Bill     | 5000   | FREE   |

    When I request the most wanted outlaw
    Then the response status should be "200"
    And the response body should contain:
      | name          | bounty | status |
      | Billy the Kid | 10000  | FREE   |

  Scenario: No most wanted outlaw if all are captured or eliminated
    Given outlaws exist:
      | name          | bounty | status     |
      | Jesse James   | 10000  | CAPTURED   |
      | Billy the Kid | 10000  | ELIMINATED |
    When I request the most wanted outlaw
    Then the response status should be "404"

  Scenario: Find the most wanted outlaw when multiple have the same bounty
    Given outlaws exist:
      | name        | bounty | status |
      | Jesse James | 10000  | FREE   |
    And outlaws exist:
      | name          | bounty | status |
      | Billy the Kid | 10000  | FREE   |
    When I find the most wanted outlaw
    Then the response status should be "200"
    And the response body should contain:
      | name        | bounty |
      | Jesse James | 10000  |
