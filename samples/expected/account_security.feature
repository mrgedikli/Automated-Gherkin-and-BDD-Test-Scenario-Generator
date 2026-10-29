Feature: Account security

  Background:
    Given the account is active

  @REQ-7 @positive
  Scenario: The user enters a wrong password
    When the user enters a wrong password
    Then the system displays an error message

  @REQ-7 @negative
  Scenario: The user enters a wrong password - negative
    When the condition "the user enters a wrong password" is not met
    Then the system does not display an error message

  @REQ-9 @positive
  Scenario: The user enters a wrong password three times
    When the user enters a wrong password three times
    Then the system locks the account

  @REQ-9 @negative
  Scenario: The user enters a wrong password three times - negative
    When the condition "the user enters a wrong password three times" is not met
    Then the system does not lock the account

  @REQ-9 @boundary
  Scenario Outline: The user enters a wrong password <count> times
    When the user enters a wrong password <count> times
    Then the system <outcome>

    Examples:
      | count | outcome                   |
      | 2     | does not lock the account |
      | 3     | locks the account         |
      | 4     | locks the account         |
