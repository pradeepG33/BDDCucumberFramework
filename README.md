# SauceDemo Automation Framework

A comprehensive Selenium automation framework built for testing the SauceDemo website (https://www.saucedemo.com) using Java, Selenium WebDriver, Cucumber (BDD), TestNG, and Maven.

## 🏗️ Framework Architecture

This framework follows industry best practices and includes:

- **Page Object Model (POM)** with PageFactory
- **Behavior Driven Development (BDD)** using Cucumber
- **Data-driven testing** with JSON and Excel support
- **Parallel execution** capabilities
- **Cross-browser testing** support
- **Comprehensive reporting** with ExtentReports and Allure
- **Screenshot capture** on test events
- **Retry mechanism** for flaky tests
- **Logging** with Log4j2
- **CI/CD ready** configuration

## 🛠️ Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 11+ | Programming Language |
| Maven | 3.6+ | Build Management |
| Selenium WebDriver | 4.15.0 | Web Automation |
| Cucumber | 7.14.0 | BDD Framework |
| TestNG | 7.8.0 | Testing Framework |
| ExtentReports | 5.1.1 | HTML Reporting |
| Allure | 2.24.0 | Advanced Reporting |
| Log4j2 | 2.20.0 | Logging |
| WebDriverManager | 5.6.2 | Driver Management |
| AssertJ | 3.24.2 | Fluent Assertions |

## 📁 Project Structure

```
saucedemo-automation-framework/
├── src/
│   ├── main/java/com/saucedemo/
│   │   ├── config/
│   │   │   └── ConfigManager.java
│   │   ├── listeners/
│   │   │   ├── ExtentReportListener.java
│   │   │   ├── RetryAnalyzer.java
│   │   │   ├── RetryListener.java
│   │   │   └── ScreenshotListener.java
│   │   ├── pages/
│   │   │   ├── BasePage.java
│   │   │   ├── LoginPage.java
│   │   │   ├── InventoryPage.java
│   │   │   └── CartPage.java
│   │   └── utils/
│   │       ├── DriverManager.java
│   │       ├── ElementUtils.java
│   │       ├── ScreenshotUtils.java
│   │       └── TestDataManager.java
│   └── test/
│       ├── java/com/saucedemo/
│       │   ├── hooks/
│       │   │   └── TestHooks.java
│       │   ├── runners/
│       │   │   └── LoginTestRunner.java
│       │   └── stepdefinitions/
│       │       └── LoginStepDefinitions.java
│       └── resources/
│           ├── config/
│           │   └── config.properties
│           ├── features/
│           │   └── login.feature
│           ├── testdata/
│           │   ├── users.json
│           │   ├── products.json
│           │   └── checkout.json
│           ├── log4j2.xml
│           └── testng.xml
├── target/
│   ├── cucumber-reports/
│   ├── extent-reports/
│   ├── screenshots/
│   └── logs/
├── pom.xml
└── README.md
```

## 🚀 Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Chrome, Firefox, or Edge browser
- Git

### Installation

1. **Clone the repository:**
```bash
git clone <repository-url>
cd saucedemo-automation-framework
```

2. **Install dependencies:**
```bash
mvn clean install
```

3. **Verify setup:**
```bash
mvn test -Dtest=LoginTestRunner
```

## ⚙️ Configuration

### config.properties
All framework configurations are managed in `src/test/resources/config/config.properties`:

```properties
# Application Configuration
app.url=https://www.saucedemo.com
browser=chrome
headless=false

# Timeouts
implicit.wait=10
explicit.wait=15
page.load.timeout=30

# Reporting
extent.report.name=SauceDemo Automation Report
reports.path=target/reports/
screenshot.path=target/screenshots/

# Retry Configuration
retry.count=2

# Parallel Execution
thread.count=3
parallel.mode=methods
```

## 🧪 Test Execution

### Run All Tests
```bash
mvn clean test
```

### Run Specific Feature
```bash
mvn test -Dtest=LoginTestRunner
```

### Run with Different Browser
```bash
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
mvn test -Dbrowser=chrome
```

### Run in Headless Mode
```bash
mvn test -Dheadless=true
```

### Run with Specific Tags
```bash
mvn test -Dcucumber.filter.tags="@smoke"
mvn test -Dcucumber.filter.tags="@positive"
mvn test -Dcucumber.filter.tags="@negative"
```

### Parallel Execution
```bash
mvn test -Pparallel
```

## 📊 Reporting

### ExtentReports
- **Location:** `target/reports/ExtentReport_[timestamp].html`
- **Features:** Rich HTML reports with screenshots, logs, and system information

### Allure Reports
```bash
mvn allure:serve
```

### Cucumber Reports
- **HTML:** `target/cucumber-reports/`
- **JSON:** `target/cucumber-reports/cucumber.json`

## 🎯 Test Coverage

### Login Functionality ✅
- [x] Valid login with all user types
- [x] Invalid login scenarios
- [x] Error message validation
- [x] UI element validation
- [x] Security testing
- [x] Performance testing

### Product Functionality (Planned)
- [ ] Product sorting and filtering
- [ ] Add/remove products to cart
- [ ] Product details validation
- [ ] User role-based scenarios

### Cart Functionality (Planned)
- [ ] Add/remove items
- [ ] Cart badge validation
- [ ] Cart calculations
- [ ] Navigation testing

### Checkout Functionality (Planned)
- [ ] Complete checkout flow
- [ ] Form validation
- [ ] Error handling
- [ ] Payment flow

## 🔧 Framework Features

### 1. Page Object Model
- Centralized element management
- Reusable page methods
- PageFactory implementation
- Inheritance hierarchy

### 2. Data-Driven Testing
```java
// JSON data support
TestDataManager.getUserData("standard_user");
TestDataManager.getProductData();

// Excel data support (if needed)
TestDataManager.readExcelData("testdata.xlsx", "LoginData");
```

### 3. Screenshot Management
```java
// Automatic screenshots on failure
// Manual screenshot capture
ScreenshotUtils.captureScreenshot(driver, "test_step");
```

### 4. Logging
```java
// Structured logging with Log4j2
logger.info("Test step executed successfully");
```

### 5. Retry Mechanism
- Automatic retry for failed tests
- Configurable retry count
- Retry reporting

### 6. Cross-Browser Support
- Chrome, Firefox, Edge, Safari
- Automatic driver management
- Browser-specific configurations

## 🏷️ Cucumber Tags

| Tag | Purpose |
|-----|---------|
| `@login` | Login functionality tests |
| `@inventory` | Product/inventory tests |
| `@cart` | Cart functionality tests |
| `@checkout` | Checkout process tests |
| `@smoke` | Critical functionality |
| `@positive` | Happy path scenarios |
| `@negative` | Error scenarios |
| `@data_driven` | Data-driven tests |
| `@cleanup_cart` | Tests requiring cart cleanup |

## 🛡️ Test Data Management

### User Credentials
All test users are configured in `testdata/users.json`:
- `standard_user` - Normal functionality
- `locked_out_user` - Account locked scenario
- `problem_user` - UI issues simulation
- `performance_glitch_user` - Performance testing

### Dynamic Test Data
```java
// Get user data
Map<String, Object> userData = TestDataManager.getUserData("standard_user");

// Get product data
List<Map<String, Object>> products = TestDataManager.getProductData();
```

## 🔄 CI/CD Integration

### GitHub Actions (Example)
```yaml
name: Selenium Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Set up JDK 11
      uses: actions/setup-java@v2
      with:
        java-version: '11'
    - name: Run tests
      run: mvn clean test -Dheadless=true
    - name: Generate Allure Report
      run: mvn allure:report
```

## 🐛 Debugging

### Debug Mode
```bash
mvn test -Ddebug=true -Dheadless=false
```

### Verbose Logging
```bash
mvn test -Dlog.level=DEBUG
```

### Screenshot Analysis
- All screenshots are saved in `target/screenshots/`
- Automatic failure screenshots
- Step-by-step screenshots available

## 📝 Best Practices

### 1. Test Writing
- Use descriptive test names
- Follow Given-When-Then structure
- Add appropriate tags
- Include negative scenarios

### 2. Page Objects
- Keep methods focused and reusable
- Use meaningful element names
- Implement proper waits
- Add comprehensive logging

### 3. Data Management
- Use external data files
- Parameterize test data
- Avoid hard-coded values
- Implement data cleanup

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Add comprehensive tests
4. Follow coding standards
5. Submit a pull request

## 📞 Support

For questions or issues:
- Create an issue in the repository
- Check existing documentation
- Review test logs and screenshots

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

**Built with ❤️ for reliable web automation testing**