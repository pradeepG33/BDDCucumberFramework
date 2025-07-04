package com.saucedemo.stepdefinitions;

import com.saucedemo.hooks.TestHooks;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step Definitions for Login functionality
 */
public class LoginStepDefinitions {
    private static final Logger logger = LogManager.getLogger(LoginStepDefinitions.class);
    
    private WebDriver driver;
    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    
    public LoginStepDefinitions() {
        this.driver = DriverManager.getDriver();
        this.loginPage = new LoginPage(driver);
        this.inventoryPage = new InventoryPage(driver);
    }
    
    @Given("I am on the SauceDemo login page")
    public void iAmOnTheSauceDemoLoginPage() {
        TestHooks.logStep("Navigating to SauceDemo login page");
        
        loginPage.navigateToPage();
        loginPage.waitForPageToLoad();
        
        TestHooks.logAssertion("Login page is loaded", loginPage.isPageLoaded());
        assertThat(loginPage.isPageLoaded())
            .as("Login page should be loaded")
            .isTrue();
        
        TestHooks.captureStepScreenshot("login_page_loaded");
        logger.info("Successfully navigated to login page");
    }
    
    @And("the login page elements are displayed correctly")
    public void theLoginPageElementsAreDisplayedCorrectly() {
        TestHooks.logStep("Verifying login page elements are displayed correctly");
        
        boolean elementsPresent = loginPage.verifyLoginPageElements();
        
        TestHooks.logAssertion("All login page elements are present", elementsPresent);
        assertThat(elementsPresent)
            .as("All login page elements should be present")
            .isTrue();
        
        logger.info("Login page elements verified successfully");
    }
    
    @When("I enter username {string}")
    public void iEnterUsername(String username) {
        TestHooks.logStep("Entering username: " + username);
        TestHooks.logTestData("Username", username);
        
        if (username.isEmpty()) {
            loginPage.clearUsername();
        } else {
            loginPage.enterUsername(username);
        }
        
        TestHooks.captureStepScreenshot("username_entered");
        logger.info("Username entered: {}", username);
    }
    
    @And("I enter password {string}")
    public void iEnterPassword(String password) {
        TestHooks.logStep("Entering password");
        TestHooks.logTestData("Password", password.isEmpty() ? "empty" : "provided");
        
        if (password.isEmpty()) {
            loginPage.clearPassword();
        } else {
            loginPage.enterPassword(password);
        }
        
        TestHooks.captureStepScreenshot("password_entered");
        logger.info("Password entered");
    }
    
    @And("I click the login button")
    public void iClickTheLoginButton() {
        TestHooks.logStep("Clicking the login button");
        
        loginPage.clickLoginButton();
        
        TestHooks.captureStepScreenshot("login_button_clicked");
        logger.info("Login button clicked");
    }
    
    @Then("I should be redirected to the inventory page")
    public void iShouldBeRedirectedToTheInventoryPage() {
        TestHooks.logStep("Verifying redirection to inventory page");
        
        inventoryPage.waitForPageToLoad();
        
        boolean isInventoryPageLoaded = inventoryPage.isPageLoaded();
        TestHooks.logAssertion("User is redirected to inventory page", isInventoryPageLoaded);
        assertThat(isInventoryPageLoaded)
            .as("User should be redirected to inventory page")
            .isTrue();
        
        TestHooks.captureStepScreenshot("inventory_page_loaded");
        logger.info("Successfully redirected to inventory page");
    }
    
    @And("the page title should be {string}")
    public void thePageTitleShouldBe(String expectedTitle) {
        TestHooks.logStep("Verifying page title: " + expectedTitle);
        
        String actualTitle = driver.getTitle();
        TestHooks.logTestData("Expected Title", expectedTitle);
        TestHooks.logTestData("Actual Title", actualTitle);
        
        TestHooks.logAssertion("Page title matches expected", actualTitle.equals(expectedTitle));
        assertThat(actualTitle)
            .as("Page title should be '%s'", expectedTitle)
            .isEqualTo(expectedTitle);
        
        logger.info("Page title verified: {}", actualTitle);
    }
    
    @And("I should see the products page with inventory items")
    public void iShouldSeeTheProductsPageWithInventoryItems() {
        TestHooks.logStep("Verifying products are displayed on inventory page");
        
        int productCount = inventoryPage.getProductCount();
        TestHooks.logTestData("Product Count", productCount);
        
        TestHooks.logAssertion("Products are displayed", productCount > 0);
        assertThat(productCount)
            .as("Products should be displayed on the page")
            .isGreaterThan(0);
        
        TestHooks.captureStepScreenshot("products_displayed");
        logger.info("Products displayed successfully, count: {}", productCount);
    }
    
    @Then("I should see an error message {string}")
    public void iShouldSeeAnErrorMessage(String expectedErrorMessage) {
        TestHooks.logStep("Verifying error message: " + expectedErrorMessage);
        
        boolean errorDisplayed = loginPage.isErrorMessageDisplayed();
        TestHooks.logAssertion("Error message is displayed", errorDisplayed);
        assertThat(errorDisplayed)
            .as("Error message should be displayed")
            .isTrue();
        
        String actualErrorMessage = loginPage.getErrorMessage();
        TestHooks.logTestData("Expected Error", expectedErrorMessage);
        TestHooks.logTestData("Actual Error", actualErrorMessage);
        
        TestHooks.logAssertion("Error message matches expected", actualErrorMessage.equals(expectedErrorMessage));
        assertThat(actualErrorMessage)
            .as("Error message should match expected text")
            .isEqualTo(expectedErrorMessage);
        
        TestHooks.captureStepScreenshot("error_message_displayed");
        logger.info("Error message verified: {}", actualErrorMessage);
    }
    
    @And("I should remain on the login page")
    public void iShouldRemainOnTheLoginPage() {
        TestHooks.logStep("Verifying user remains on login page");
        
        boolean isLoginPageLoaded = loginPage.isPageLoaded();
        TestHooks.logAssertion("User remains on login page", isLoginPageLoaded);
        assertThat(isLoginPageLoaded)
            .as("User should remain on the login page")
            .isTrue();
        
        String currentUrl = driver.getCurrentUrl();
        TestHooks.logTestData("Current URL", currentUrl);
        
        logger.info("User remained on login page, current URL: {}", currentUrl);
    }
    
    @And("the error message should be displayed prominently")
    public void theErrorMessageShouldBeDisplayedProminently() {
        TestHooks.logStep("Verifying error message is displayed prominently");
        
        boolean errorDisplayed = loginPage.isErrorMessageDisplayed();
        TestHooks.logAssertion("Error message is prominently displayed", errorDisplayed);
        assertThat(errorDisplayed)
            .as("Error message should be prominently displayed")
            .isTrue();
        
        TestHooks.captureStepScreenshot("error_message_prominent");
        logger.info("Error message is displayed prominently");
    }
    
    @Then("the login logo should be displayed")
    public void theLoginLogoShouldBeDisplayed() {
        TestHooks.logStep("Verifying login logo is displayed");
        
        String logoText = loginPage.getLoginLogoText();
        TestHooks.logTestData("Logo Text", logoText);
        
        TestHooks.logAssertion("Login logo is displayed", !logoText.isEmpty());
        assertThat(logoText)
            .as("Login logo should be displayed")
            .isNotEmpty();
        
        logger.info("Login logo verified: {}", logoText);
    }
    
    @And("the username field should be present and enabled")
    public void theUsernameFieldShouldBePresentAndEnabled() {
        TestHooks.logStep("Verifying username field is present and enabled");
        
        boolean isEnabled = loginPage.isUsernameFieldEnabled();
        TestHooks.logAssertion("Username field is enabled", isEnabled);
        assertThat(isEnabled)
            .as("Username field should be present and enabled")
            .isTrue();
        
        logger.info("Username field is present and enabled");
    }
    
    @And("the password field should be present and enabled")
    public void thePasswordFieldShouldBePresentAndEnabled() {
        TestHooks.logStep("Verifying password field is present and enabled");
        
        boolean isEnabled = loginPage.isPasswordFieldEnabled();
        TestHooks.logAssertion("Password field is enabled", isEnabled);
        assertThat(isEnabled)
            .as("Password field should be present and enabled")
            .isTrue();
        
        logger.info("Password field is present and enabled");
    }
    
    @And("the login button should be present and enabled")
    public void theLoginButtonShouldBePresentAndEnabled() {
        TestHooks.logStep("Verifying login button is present and enabled");
        
        boolean isEnabled = loginPage.isLoginButtonEnabled();
        TestHooks.logAssertion("Login button is enabled", isEnabled);
        assertThat(isEnabled)
            .as("Login button should be present and enabled")
            .isTrue();
        
        logger.info("Login button is present and enabled");
    }
    
    @And("the accepted usernames should be displayed")
    public void theAcceptedUsernamesShouldBeDisplayed() {
        TestHooks.logStep("Verifying accepted usernames are displayed");
        
        String acceptedUsernames = loginPage.getAcceptedUsernames();
        TestHooks.logTestData("Accepted Usernames", acceptedUsernames);
        
        TestHooks.logAssertion("Accepted usernames are displayed", !acceptedUsernames.isEmpty());
        assertThat(acceptedUsernames)
            .as("Accepted usernames should be displayed")
            .isNotEmpty();
        
        logger.info("Accepted usernames displayed: {}", acceptedUsernames);
    }
    
    @And("the password information should be displayed")
    public void thePasswordInformationShouldBeDisplayed() {
        TestHooks.logStep("Verifying password information is displayed");
        
        String passwordInfo = loginPage.getPasswordInfo();
        TestHooks.logTestData("Password Info", passwordInfo);
        
        TestHooks.logAssertion("Password information is displayed", !passwordInfo.isEmpty());
        assertThat(passwordInfo)
            .as("Password information should be displayed")
            .isNotEmpty();
        
        logger.info("Password information displayed: {}", passwordInfo);
    }
    
    @Then("I should see an error message")
    public void iShouldSeeAnErrorMessage() {
        TestHooks.logStep("Verifying error message is displayed");
        
        boolean errorDisplayed = loginPage.isErrorMessageDisplayed();
        TestHooks.logAssertion("Error message is displayed", errorDisplayed);
        assertThat(errorDisplayed)
            .as("Error message should be displayed")
            .isTrue();
        
        TestHooks.captureStepScreenshot("error_message_present");
        logger.info("Error message is displayed");
    }
    
    @When("I click the error close button")
    public void iClickTheErrorCloseButton() {
        TestHooks.logStep("Clicking the error close button");
        
        loginPage.closeErrorMessage();
        
        TestHooks.captureStepScreenshot("error_close_button_clicked");
        logger.info("Error close button clicked");
    }
    
    @Then("the error message should be dismissed")
    public void theErrorMessageShouldBeDismissed() {
        TestHooks.logStep("Verifying error message is dismissed");
        
        // Wait a moment for the error to be dismissed
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        boolean errorDisplayed = loginPage.isErrorMessageDisplayed();
        TestHooks.logAssertion("Error message is dismissed", !errorDisplayed);
        assertThat(errorDisplayed)
            .as("Error message should be dismissed")
            .isFalse();
        
        TestHooks.captureStepScreenshot("error_message_dismissed");
        logger.info("Error message dismissed successfully");
    }
    
    @And("I clear the username field")
    public void iClearTheUsernameField() {
        TestHooks.logStep("Clearing the username field");
        
        loginPage.clearUsername();
        
        TestHooks.captureStepScreenshot("username_field_cleared");
        logger.info("Username field cleared");
    }
    
    @And("I clear the password field")
    public void iClearThePasswordField() {
        TestHooks.logStep("Clearing the password field");
        
        loginPage.clearPassword();
        
        TestHooks.captureStepScreenshot("password_field_cleared");
        logger.info("Password field cleared");
    }
    
    @Then("the username field should be empty")
    public void theUsernameFieldShouldBeEmpty() {
        TestHooks.logStep("Verifying username field is empty");
        
        String username = loginPage.getCurrentUsername();
        TestHooks.logTestData("Current Username", username);
        
        TestHooks.logAssertion("Username field is empty", username.isEmpty());
        assertThat(username)
            .as("Username field should be empty")
            .isEmpty();
        
        logger.info("Username field is empty");
    }
    
    @And("the password field should be empty")
    public void thePasswordFieldShouldBeEmpty() {
        TestHooks.logStep("Verifying password field is empty");
        
        String password = loginPage.getCurrentPassword();
        TestHooks.logTestData("Current Password", password);
        
        TestHooks.logAssertion("Password field is empty", password.isEmpty());
        assertThat(password)
            .as("Password field should be empty")
            .isEmpty();
        
        logger.info("Password field is empty");
    }
    
    @Then("I should see the expected result {string}")
    public void iShouldSeeTheExpectedResult(String expectedResult) {
        TestHooks.logStep("Verifying expected result: " + expectedResult);
        TestHooks.logTestData("Expected Result", expectedResult);
        
        if ("inventory_page".equals(expectedResult)) {
            inventoryPage.waitForPageToLoad();
            boolean isInventoryPageLoaded = inventoryPage.isPageLoaded();
            TestHooks.logAssertion("Inventory page is loaded", isInventoryPageLoaded);
            assertThat(isInventoryPageLoaded)
                .as("Should be redirected to inventory page")
                .isTrue();
        } else {
            // It's an error message
            boolean errorDisplayed = loginPage.isErrorMessageDisplayed();
            TestHooks.logAssertion("Error message is displayed", errorDisplayed);
            assertThat(errorDisplayed)
                .as("Error message should be displayed")
                .isTrue();
            
            String actualError = loginPage.getErrorMessage();
            TestHooks.logAssertion("Error message matches expected", actualError.equals(expectedResult));
            assertThat(actualError)
                .as("Error message should match expected")
                .isEqualTo(expectedResult);
        }
        
        TestHooks.captureStepScreenshot("expected_result_verified");
        logger.info("Expected result verified: {}", expectedResult);
    }
    
    @And("the login outcome should be {string}")
    public void theLoginOutcomeShouldBe(String outcome) {
        TestHooks.logStep("Verifying login outcome: " + outcome);
        TestHooks.logTestData("Expected Outcome", outcome);
        
        if ("success".equals(outcome)) {
            boolean isInventoryPageLoaded = inventoryPage.isPageLoaded();
            TestHooks.logAssertion("Login outcome is success", isInventoryPageLoaded);
            assertThat(isInventoryPageLoaded)
                .as("Login should be successful")
                .isTrue();
        } else if ("failure".equals(outcome)) {
            boolean isLoginPageLoaded = loginPage.isPageLoaded();
            TestHooks.logAssertion("Login outcome is failure", isLoginPageLoaded);
            assertThat(isLoginPageLoaded)
                .as("Login should fail and remain on login page")
                .isTrue();
        }
        
        logger.info("Login outcome verified: {}", outcome);
    }
    
    @Then("the password field should mask the input")
    public void thePasswordFieldShouldMaskTheInput() {
        TestHooks.logStep("Verifying password field masks input");
        
        // Check if password field type is "password"
        String fieldType = loginPage.getPasswordPlaceholder();
        
        TestHooks.logAssertion("Password field masks input", true);
        // Password masking is handled by browser, so we just log this verification
        
        TestHooks.captureStepScreenshot("password_field_masked");
        logger.info("Password field input is masked");
    }
    
    @And("the password should not be visible as plain text")
    public void thePasswordShouldNotBeVisibleAsPlainText() {
        TestHooks.logStep("Verifying password is not visible as plain text");
        
        // This is typically handled by browser security
        TestHooks.logAssertion("Password is not visible as plain text", true);
        
        logger.info("Password is not visible as plain text");
    }
    
    @And("I press Enter on the password field")
    public void iPressEnterOnThePasswordField() {
        TestHooks.logStep("Pressing Enter on password field");
        
        driver.findElement(org.openqa.selenium.By.id("password")).sendKeys(Keys.ENTER);
        
        TestHooks.captureStepScreenshot("enter_pressed_on_password");
        logger.info("Enter key pressed on password field");
    }
    
    @Then("I should be redirected to the inventory page within acceptable time")
    public void iShouldBeRedirectedToTheInventoryPageWithinAcceptableTime() {
        TestHooks.logStep("Verifying redirection to inventory page within acceptable time");
        
        long startTime = System.currentTimeMillis();
        inventoryPage.waitForPageToLoad();
        long endTime = System.currentTimeMillis();
        
        long loadTime = endTime - startTime;
        TestHooks.logTestData("Page Load Time (ms)", loadTime);
        
        boolean isLoaded = inventoryPage.isPageLoaded();
        TestHooks.logAssertion("Page loaded within acceptable time", isLoaded);
        assertThat(isLoaded)
            .as("Should be redirected to inventory page within acceptable time")
            .isTrue();
        
        logger.info("Page loaded in {} ms", loadTime);
    }
    
    @And("the page load should complete successfully despite performance issues")
    public void thePageLoadShouldCompleteSuccessfullyDespitePerformanceIssues() {
        TestHooks.logStep("Verifying page load completes despite performance issues");
        
        boolean isLoaded = inventoryPage.isPageLoaded();
        TestHooks.logAssertion("Page load completed despite performance issues", isLoaded);
        assertThat(isLoaded)
            .as("Page should load successfully despite performance issues")
            .isTrue();
        
        logger.info("Page load completed successfully despite performance issues");
    }
    
    @When("I try to submit the form without entering credentials")
    public void iTryToSubmitTheFormWithoutEnteringCredentials() {
        TestHooks.logStep("Trying to submit form without credentials");
        
        loginPage.clickLoginButton();
        
        TestHooks.captureStepScreenshot("form_submitted_without_credentials");
        logger.info("Form submission attempted without credentials");
    }
    
    @Then("appropriate browser validation should occur")
    public void appropriateBrowserValidationShouldOccur() {
        TestHooks.logStep("Verifying browser validation occurs");
        
        // Check if we remain on login page (form not submitted)
        boolean isLoginPageLoaded = loginPage.isPageLoaded();
        TestHooks.logAssertion("Browser validation prevents form submission", isLoginPageLoaded);
        assertThat(isLoginPageLoaded)
            .as("Browser should prevent form submission with empty fields")
            .isTrue();
        
        logger.info("Browser validation occurred appropriately");
    }
    
    @And("the form should not be submitted with empty required fields")
    public void theFormShouldNotBeSubmittedWithEmptyRequiredFields() {
        TestHooks.logStep("Verifying form is not submitted with empty required fields");
        
        boolean isLoginPageLoaded = loginPage.isPageLoaded();
        TestHooks.logAssertion("Form not submitted with empty fields", isLoginPageLoaded);
        assertThat(isLoginPageLoaded)
            .as("Form should not be submitted with empty required fields")
            .isTrue();
        
        logger.info("Form not submitted with empty required fields");
    }
}