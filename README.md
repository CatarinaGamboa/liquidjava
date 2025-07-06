# LiquidJava - Extending Java with Liquid Types

![LiquidJava Banner](docs/design/figs/banner.gif)

## Welcome to LiquidJava

LiquidJava is an additional type checker for Java that based on liquid types and typestates.

Simple example:

```java
@Refinement("a > 0")
int a = 3; // okay
a = -8; // type error!
```

This project has the LiquidJava verifier, the API and some examples for testing.
You can find out more about LiquidJava in the following resources:

* [Try it](https://github.com/CatarinaGamboa/liquidjava-examples) with Codespaces or locally following these steps
* [Website](https://catarinagamboa.github.io/liquidjava.html)
* [Examples of LiquidJava](https://github.com/CatarinaGamboa/liquidjava-examples)
* [LiquidJava Specification of the Java Standard Library](https://github.com/CatarinaGamboa/liquid-java-external-libs)
* [VSCode plugin for LiquidJava](https://github.com/CatarinaGamboa/vscode-liquidjava)
<!-- * [Formalization of LiquidJava](https://github.com/CatarinaGamboa/liquidjava-formalization) - not opensource yet -->

# Setup the project

## Prerequisites
Before setting up LiquidJava, ensure you have the following installed:

- Java 20 or newer - The project is configured to use Java 20;
- Maven 3.6+ - For building and dependency management.

## Installation Steps

1. Clone the repository: `git clone https://github.com/CatarinaGamboa/liquidjava.git`;
2. Build the project `mvn clean install`;
3. Run tests to verify installation: `mvn test`;
4. If importing into an IDE, import the project as a Maven project using the root `pom.xml`.

## Verify Installation

To check your refinements using LiquidJava:

**Run verification on examples**:
```bash
mvn exec:java -pl liquidjava-verifier -Dexec.mainClass="liquidjava.api.CommandLineLauncher" -Dexec.args="liquidjava-example/src/main/java/testSuite/CorrectSimpleAssignment.java"
```
   This should output: `Correct! Passed Verification`.

**Test an error case**:
```bash
mvn exec:java -pl liquidjava-verifier -Dexec.mainClass="liquidjava.api.CommandLineLauncher" -Dexec.args="liquidjava-example/src/main/java/testSuite/ErrorSimpleAssignment.java"
```
   This should output an error message describing the refinement violation.

## Run verification

### In a specific project

Run the file `liquidjava-verifier\api\CommandLineLaucher` with the path to the target project for verification.
If there are no arguments given, the application verifies the project on the path `liquidjava-example\src\main\java`.

## Testing

Run `mvn test` to run all the tests in LiquidJava.

The starter test file is `TestExamples.java` which uses the test suite under the `liquidjava-examples/testSuite`.

Paths in the test suite are considered test cases if:

1. File that start with `Correct` or `Error` (e.g, "CorrectRecursion.java")
2. Package/Folder that contains the word `correct` or `error`.

Therefore, files/folders that do not pass this description are not verified.

## Project structure

* **docs**: documents used for the design of the language. The folder includes a readme to a full artifact used in the design process, here are some initial documents used to prepare the design of the refinements language at its evaluation
* **liquidjava-api**: inlcudes the annotations that can be introduced in the Java programs to add the refinements
* **liquidjava-examples**: includes a main folder with the current example that the verifier is testing; the test suite that is used in maven test is under the `testSuite` folder
* **liquidjava-verifier**: has the project for verification of the classes
  * *api*: classes that test the verifier. Includes the `CommandLineLauncher` that runs the verification on a given class or on the main folder of `liquidjava-examples` if no argument is given. This package includes the JUnit tests to verify if the examples in `liquidjava-example/tests` are correctly verified.
  * *ast*: represents the abstract syntax tree of the refinement's language.
  * *errors*: package for reporting the errors.
  * *processor*: package that handles the type checking.
  * *rj_language*: handles the processing of the strings with refinements.
  * *smt*: package that handles the translation to the SMT solver and the processing of the results the SMT solver produces.
  * *utils*: includes useful methods for all the previous packages.
  * *test/java/liquidjava/api/tests* contains the `TestExamples` class used for running the test suite.
