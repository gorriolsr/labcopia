Feature: Student Registration

  Scenario: Successful registration
    When I register a student with:
      | name  | email                 |
      | John  | john@tecnocampus.cat  |
    Then the registration status should be "201"

  Scenario: Registration with invalid email
    When I register a student with:
      | name | email |
      | John | john@gmail.com |
    Then the registration status should be "422"
