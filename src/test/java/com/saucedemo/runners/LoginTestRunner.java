package com.saucedemo.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Test Runner for Login functionality tests
 */
@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features/login.feature",
    glue = {
        "com.saucedemo.stepdefinitions",
        "com.saucedemo.hooks"
    },
    plugin = {
        "pretty",
        "html:target/cucumber-reports/login",
        "json:target/cucumber-reports/login/cucumber.json",
        "junit:target/cucumber-reports/login/cucumber.xml",
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
    },
    monochrome = true,
    publish = false,
    dryRun = false,
    tags = "@login"
)
public class LoginTestRunner {
    // This class is empty, test execution is handled by Cucumber
}