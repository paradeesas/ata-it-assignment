# API Automation Framework

This project is a robust, scalable, and maintainable automated testing framework for RESTful APIs, built with Java, REST Assured, and JUnit 5.

## Setup / Installation
1. **Prerequisites:**
   Ensure you have [Java 17+](https://adoptium.net/)

2. **Download Maven:**
   - Go to [Apache Maven Download](https://maven.apache.org/download.cgi) and download the **Binary zip archive** (e.g., `apache-maven-3.9.x-bin.zip`).

3. **Extract the file:**
   - Extract the downloaded zip file to a directory of your choice (e.g., `/Users/your-username/Applications/`).

4. **Configure PATH:**
   - Open your terminal and edit your shell configuration file (e.g., `~/.zshrc`):
     ```bash
     nano ~/.zshrc
     ```
   - Add the following line at the end (replace with your actual path):
     ```bash
     export PATH=/Users/your-username/Applications/apache-maven-3.9.x/bin:$PATH
     ```
   - Save and exit (Ctrl+O, Enter, Ctrl+X), then apply the changes:
     ```bash
     source ~/.zshrc
     ```

5. **Verify Installation:**
   - Run the following command to verify:
     ```bash
     mvn -version
     ```

6. **Clone the repository:**
   ```bash
   git clone https://github.com/paradeesas/ata-it-assignment.git
   cd rest-api-automation

7. **How to run:**
   ```bash
   mvn clean test

## Architectural Decisions
* **Separation of Concerns:** The framework follows a modular design, separating API clients (`UserClient`) for request handling and assertion utilities (`AssertionUtils`) for validation logic. This promotes reusability and ensures the test classes remain clean and focused on business logic.
* **Logging Strategy:** 
   * **Request Traceability:** Every request is logged using `LogDetail.BODY` within the client layer, ensuring full body visibility for effective troubleshooting.
    * **Clean Test Reports:** Assertions utilize `.log().ifValidationFails()`. This design ensures logs remain clean during successful runs while automatically capturing critical failure information only when a test fails.
* **Centralized Configuration:** All environment-specific settings (Base URI, Credentials) are managed via a centralized configuration manager, making the framework environment-agnostic.

## Chosen Scenarios
* **CRUD Operations (Create, Read, Update, Delete):** Essential to verify the core functionality and lifecycle of user resources.
* **Schema Validation:** Ensures that the API response structure adheres to the predefined contract, preventing breaking changes.
* **Negative/Edge Cases:** Validating API behavior under scenarios such as unauthorized access (missing token) or invalid input, ensuring the system fails gracefully and provides meaningful error messages.

## Future Improvements
If given more time, I would implement the following:
* **CI/CD Integration**: Configure a pipeline (GitHub Actions/Jenkins) to trigger tests automatically on every push.
* **Test Data Management (Setup & Teardown)**: Implement robust pre-test data preparation and post-test data cleanup mechanisms. This ensures test independence, prevents data pollution in the test environment, and avoids flakiness caused by leftover state.
* **Data-Driven Testing**: Implement external data sources (e.g., CSV or Excel) to test multiple input combinations efficiently.