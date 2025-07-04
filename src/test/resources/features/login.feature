@login
Feature: Login Functionality
  As a user of SauceDemo application
  I want to be able to login with valid credentials
  So that I can access the application features

  Background:
    Given I am on the SauceDemo login page
    And the login page elements are displayed correctly

  @positive @smoke
  Scenario: Successful login with standard user
    When I enter username "standard_user"
    And I enter password "secret_sauce"
    And I click the login button
    Then I should be redirected to the inventory page
    And the page title should be "Swag Labs"
    And I should see the products page with inventory items

  @positive
  Scenario: Successful login with problem user
    When I enter username "problem_user"
    And I enter password "secret_sauce"
    And I click the login button
    Then I should be redirected to the inventory page
    And the page title should be "Swag Labs"
    And I should see the products page with inventory items

  @positive
  Scenario: Successful login with performance glitch user
    When I enter username "performance_glitch_user"
    And I enter password "secret_sauce"
    And I click the login button
    Then I should be redirected to the inventory page
    And the page title should be "Swag Labs"
    And I should see the products page with inventory items

  @positive
  Scenario: Successful login with error user
    When I enter username "error_user"
    And I enter password "secret_sauce"
    And I click the login button
    Then I should be redirected to the inventory page
    And the page title should be "Swag Labs"
    And I should see the products page with inventory items

  @positive
  Scenario: Successful login with visual user
    When I enter username "visual_user"
    And I enter password "secret_sauce"
    And I click the login button
    Then I should be redirected to the inventory page
    And the page title should be "Swag Labs"
    And I should see the products page with inventory items

  @negative
  Scenario: Login with locked out user
    When I enter username "locked_out_user"
    And I enter password "secret_sauce"
    And I click the login button
    Then I should see an error message "Epic sadface: Sorry, this user has been locked out."
    And I should remain on the login page
    And the error message should be displayed prominently

  @negative
  Scenario: Login with invalid username
    When I enter username "invalid_user"
    And I enter password "secret_sauce"
    And I click the login button
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service"
    And I should remain on the login page

  @negative
  Scenario: Login with invalid password
    When I enter username "standard_user"
    And I enter password "wrong_password"
    And I click the login button
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service"
    And I should remain on the login page

  @negative @boundary
  Scenario: Login with empty username
    When I enter username ""
    And I enter password "secret_sauce"
    And I click the login button
    Then I should see an error message "Epic sadface: Username is required"
    And I should remain on the login page

  @negative @boundary
  Scenario: Login with empty password
    When I enter username "standard_user"
    And I enter password ""
    And I click the login button
    Then I should see an error message "Epic sadface: Password is required"
    And I should remain on the login page

  @negative @boundary
  Scenario: Login with both username and password empty
    When I enter username ""
    And I enter password ""
    And I click the login button
    Then I should see an error message "Epic sadface: Username is required"
    And I should remain on the login page

  @negative
  Scenario: Login with username containing special characters
    When I enter username "user@#$%"
    And I enter password "secret_sauce"
    And I click the login button
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service"
    And I should remain on the login page

  @negative
  Scenario: Login with case sensitive username
    When I enter username "STANDARD_USER"
    And I enter password "secret_sauce"
    And I click the login button
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service"
    And I should remain on the login page

  @negative
  Scenario: Login with case sensitive password
    When I enter username "standard_user"
    And I enter password "SECRET_SAUCE"
    And I click the login button
    Then I should see an error message "Epic sadface: Username and password do not match any user in this service"
    And I should remain on the login page

  @positive @ui_validation
  Scenario: Verify login page UI elements
    Then the login logo should be displayed
    And the username field should be present and enabled
    And the password field should be present and enabled
    And the login button should be present and enabled
    And the accepted usernames should be displayed
    And the password information should be displayed

  @positive @functionality
  Scenario: Verify error message can be closed
    When I enter username "invalid_user"
    And I enter password "secret_sauce"
    And I click the login button
    Then I should see an error message
    When I click the error close button
    Then the error message should be dismissed

  @positive @functionality
  Scenario: Verify fields can be cleared
    When I enter username "test_user"
    And I enter password "test_password"
    And I clear the username field
    And I clear the password field
    Then the username field should be empty
    And the password field should be empty

  @data_driven
  Scenario Outline: Login with different user types
    When I enter username "<username>"
    And I enter password "<password>"
    And I click the login button
    Then I should see the expected result "<expected_result>"
    And the login outcome should be "<outcome>"

    Examples:
      | username                | password      | expected_result                                                               | outcome |
      | standard_user          | secret_sauce  | inventory_page                                                               | success |
      | problem_user           | secret_sauce  | inventory_page                                                               | success |
      | performance_glitch_user| secret_sauce  | inventory_page                                                               | success |
      | error_user             | secret_sauce  | inventory_page                                                               | success |
      | visual_user            | secret_sauce  | inventory_page                                                               | success |
      | locked_out_user        | secret_sauce  | Epic sadface: Sorry, this user has been locked out.                         | failure |
      | invalid_user           | secret_sauce  | Epic sadface: Username and password do not match any user in this service   | failure |
      | standard_user          | wrong_pass    | Epic sadface: Username and password do not match any user in this service   | failure |
      |                        | secret_sauce  | Epic sadface: Username is required                                           | failure |
      | standard_user          |               | Epic sadface: Password is required                                           | failure |

  @security
  Scenario: Verify password field masks input
    When I enter password "secret_sauce"
    Then the password field should mask the input
    And the password should not be visible as plain text

  @usability
  Scenario: Verify login button is accessible via keyboard
    When I enter username "standard_user"
    And I enter password "secret_sauce"
    And I press Enter on the password field
    Then I should be redirected to the inventory page

  @performance
  Scenario: Verify login performance with performance glitch user
    When I enter username "performance_glitch_user"
    And I enter password "secret_sauce"
    And I click the login button
    Then I should be redirected to the inventory page within acceptable time
    And the page load should complete successfully despite performance issues

  @browser_validation
  Scenario: Verify login form validation
    When I try to submit the form without entering credentials
    Then appropriate browser validation should occur
    And the form should not be submitted with empty required fields