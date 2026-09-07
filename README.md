# Selenium · Cucumber BDD · REST Assured — Hybrid Test Automation Framework

A scalable, maintainable test automation framework that supports **UI (Web)** and **API** testing from a single codebase, with a first-class **hybrid** mode where API calls arrange state for UI verification. Built with Java 21, Selenium 4, Cucumber 7 (BDD), REST Assured, JSON-Schema contract validation, Allure reporting, and a ready-to-use GitHub Actions pipeline.

---

## Why this design

| Concern | Choice | Rationale |
|---|---|---|
| UI structure | **Page Object Model** | Locators + actions live in page classes; steps stay readable and change-resilient. |
| API structure | **Service/Client layer** | Each resource's endpoints are encapsulated (e.g. `UserService`), mirroring POM for the API side. |
| Shared state | **Cucumber DI (PicoContainer)** via `TestContext`/`ScenarioContext` | Per-scenario isolation, no static state → safe parallel runs and clean UI↔API handoff. |
| Config | **Owner** library, layered sources | `-D` > env vars > `{env}.properties` > `common.properties`. Runtime overrides with zero file edits. |
| Contract testing | **networknt json-schema-validator** (Draft 2020-12) | The Java equivalent of Ajv/Zod; validates API responses against versioned schemas. |
| Reporting | **Allure** + Cucumber HTML/JSON | Rich, step-level reports with request/response and failure screenshots attached. |
| Parallelism | **Cucumber JUnit Platform** engine | Thread-local driver + per-scenario context. |

---

## Folder structure

```
automation-framework/
├── pom.xml                           # Build, dependencies, Surefire (parallel + AspectJ/Allure)
├── README.md
├── .gitignore
├── .github/workflows/ci.yml          # CI: cross-browser matrix, rerun, Allure publish
│
├── src/main/java/com/framework/
│   ├── config/
│   │   ├── FrameworkConfig.java       # Typed config contract (Owner), layered sources
│   │   └── ConfigManager.java         # Cached single access point
│   ├── constants/
│   │   └── FrameworkConstants.java    # Paths, keys, timeouts
│   ├── core/
│   │   ├── driver/
│   │   │   ├── BrowserType.java        # chrome | firefox | edge
│   │   │   ├── DriverFactory.java      # Builds local/remote drivers + options
│   │   │   └── DriverManager.java      # Thread-local driver lifecycle (parallel-safe)
│   │   ├── api/
│   │   │   ├── RequestSpecFactory.java # Base + authenticated REST Assured specs, Allure filter
│   │   │   └── AuthManager.java        # Shared token generation/caching (UI + API)
│   │   └── context/
│   │       ├── ScenarioContext.java    # Per-scenario key/value bag
│   │       └── TestContext.java        # DI composition root
│   ├── pages/                          # ---- Page Object Model (UI) ----
│   │   ├── BasePage.java               # Common actions on top of WaitUtils
│   │   ├── LoginPage.java              # Form login + token session seeding
│   │   ├── ParaBankRegistrationPage.java # Registration + logout/login navigation
│   │   └── SecureAreaPage.java
│   ├── services/                       # ---- Service layer (API) ----
│   │   ├── BaseService.java
│   │   └── UserService.java            # /users endpoints
│   ├── models/
│   │   ├── RegistrationData.java       # Immutable ParaBank registration data
│   │   └── User.java                   # Jackson + Lombok POJO
│   └── utils/                          # ---- Shared utilities ----
│       ├── JsonUtils.java              # One configured ObjectMapper
│       ├── WaitUtils.java              # Explicit waits (single source of truth)
│       ├── ScreenshotUtils.java        # Failure capture → disk + Allure
│       ├── SchemaValidator.java        # JSON-Schema response validation
│       └── TestDataProvider.java       # Fixtures + Datafaker generation
│
└── src/test/
    ├── java/com/framework/
    │   ├── hooks/
    │   │   ├── CommonHooks.java         # All scenarios: logging/MDC, REST setup, context reset
    │   │   └── UiHooks.java             # @ui only: browser start/stop, failure screenshot
    │   ├── runners/
    │   │   ├── TestRunner.java           # Cucumber JUnit Platform suite
    │   │   └── FailedTestRunner.java     # Reruns only previously-failed scenarios
    │   └── stepdefinitions/
    │       ├── ui/LoginSteps.java
    │       ├── ui/ParaBankRegistrationSteps.java
    │       ├── api/UserApiSteps.java
    │       └── hybrid/UserOnboardingSteps.java
    └── resources/
        ├── config/                      # common + dev/test/prod properties
        ├── features/{ui,api,hybrid}/    # .feature files
        ├── schemas/                     # JSON Schemas for contract validation
        ├── testdata/users.json          # Static fixtures
        ├── junit-platform.properties    # Cucumber/parallel/retry config
        └── log4j2.xml                    # Console + rolling-file logging
```

---

## Prerequisites

- **JDK 21+**
- **Maven 3.9+**
- Chrome / Firefox / Edge installed locally (drivers are auto-resolved by WebDriverManager)
- (Optional) **Allure CLI** to open reports locally

---

## Running tests

```bash
# Full suite (defaults: env=test, browser=chrome, headless=true, tags="@smoke or @regression")
mvn clean test

# Only smoke tests
mvn clean test -Dcucumber.filter.tags="@smoke"

# Only API / only UI / only hybrid
mvn clean test -Dcucumber.filter.tags="@api"
mvn clean test -Dcucumber.filter.tags="@ui"
mvn clean test -Dcucumber.filter.tags="@hybrid"

# ParaBank registration flow only
mvn clean test -Dcucumber.filter.tags="@parabank"

# Cross-browser + environment overrides
mvn clean test -Dbrowser=firefox -Denv=dev -Dheadless=false

# Control parallelism
mvn clean test -Dparallel.count=5

# Remote / Selenium Grid
mvn clean test -Dremote.enabled=true -Dremote.url=http://localhost:4444/wd/hub

# Rerun ONLY the scenarios that failed last time
mvn test -Dtest=FailedTestRunner
```

### Tag conventions
- `@ui`, `@api`, `@hybrid` — layer selectors (also drive which hooks fire)
- `@smoke` — fast, critical-path subset
- `@regression` — full coverage
- `@parabank` — ParaBank registration and login-navigation flow

### ParaBank registration flow

The `@parabank` scenario opens `https://parabank.parasoft.com/parabank/register.htm`, registers a new customer, verifies that the account was created, logs out, and confirms that the login page is displayed.

`TestDataProvider.generateRegistrationData()` creates an isolated user for every scenario with the following values:

| Field | Value |
|---|---|
| First name / last name | `Jane` / `Doe` |
| Address | `123 Main St`, `Springfield`, `IL`, `62704` |
| Phone | `555-0100` |
| SSN | Random value in `###-##-####` format |
| Username | Unique `qauser<suffix>` value |
| Password | `P@ssw0rd123` |

The application URL is configured with `parabank.base.url` in `config/test.properties` and can be overridden at runtime:

```bash
mvn clean test -Dcucumber.filter.tags="@parabank" -Dparabank.base.url=https://parabank.parasoft.com/parabank
```

---

## Reporting

```bash
# Generate + open the Allure report locally
mvn allure:serve

# Or generate static HTML into target/site/allure-maven-plugin
mvn allure:report
```

Also produced every run:
- **Cucumber HTML:** `target/cucumber-reports/cucumber.html`
- **Cucumber JSON:** `target/cucumber-reports/cucumber.json`
- **Screenshots (UI failures):** `target/screenshots/`
- **Logs:** `target/logs/automation.log`

The Allure report contains step-level detail, full API request/response (via the REST Assured filter), and failure screenshots attached inline.

---

## Configuration model

Values resolve **top-down, first match wins** (see `FrameworkConfig`):

1. System properties — `-Dweb.base.url=...` (best for CI)
2. Environment variables — great for secrets
3. `config/{env}.properties` — the `${env}` placeholder is itself resolved from 1–2 (default `test`)
4. `config/common.properties` — env-agnostic defaults

So a value baked into `prod.properties` can still be overridden at runtime without editing any file.

---

## Key concepts

### Shared authentication (UI + API)
`AuthManager` fetches a token once per identity and caches it thread-safely. API tests pass it into `RequestSpecFactory.authenticated(token)`; UI tests can seed the same token into browser storage (`LoginPage#seedSession`) to skip the login screen — the Selenium analogue of "storage state reuse".

### Hybrid testing
`features/hybrid/user_onboarding.feature` **creates a user via the API** (fast, reliable setup), stores the id + shared token in `ScenarioContext`, then **verifies through the UI** reusing that same token. This is the pattern that keeps UI suites fast and decoupled from slow UI-based setup.

### Parallel safety
Every scenario gets its own thread-local `WebDriver` (`DriverManager`) and its own `ScenarioContext`. No static mutable state is shared across scenarios, so raising `-Dparallel.count` is safe.

### Retry / flakiness
Failed scenarios are written to `target/rerun/failed-scenarios.txt` by the `rerun` plugin. `FailedTestRunner` re-executes just those, so a genuine failure (reproducible) is easily told apart from transient flakiness. The CI pipeline runs this rerun step automatically on failure.

---

## CI/CD (GitHub Actions)

`.github/workflows/ci.yml` provides:
- Triggers: push/PR to `main`, nightly cron, and manual `workflow_dispatch` (custom tags/env)
- **Cross-browser matrix** (chrome, firefox), `fail-fast: false`
- Maven dependency caching
- Automatic **rerun of failed scenarios**
- Artifact upload (Allure results, Cucumber reports, screenshots, logs)
- Merged **Allure report published to GitHub Pages** on `main`

---

## Extending the framework

- **New page:** add a class under `pages/` extending `BasePage`; expose business methods, keep locators private.
- **New API resource:** add a class under `services/` extending `BaseService`; return REST Assured `Response`.
- **New feature:** drop a `.feature` under the right `features/` subfolder, tag it, add step definitions under the matching `stepdefinitions/` package (they're auto-glued via the runner).
- **New schema:** add a `*.json` schema under `schemas/` and reference it from a step with `SchemaValidator.validate(...)`.
