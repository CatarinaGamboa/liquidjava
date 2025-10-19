# LiquidJava - Extending Java with Liquid Types

![LiquidJava Banner](docs/design/figs/banner.gif)

## Welcome to LiquidJava!

LiquidJava is an additional type checker for Java, based on **liquid types** and **typestates**, which provides additional safety guarantees to Java programs through **refinements** at compile time.

**Example:**

```java
@Refinement("a > 0")
int a = 3; // okay
a = -8; // type error!
```

This project contains the LiquidJava verifier and its API, as well as some examples for testing.

You can find out more about LiquidJava in the following resources:

* [Try it](https://github.com/CatarinaGamboa/liquidjava-examples) with GitHub Codespaces or locally
* [VS Code extension for LiquidJava](https://github.com/CatarinaGamboa/vscode-liquidjava)
* [LiquidJava website](https://catarinagamboa.github.io/liquidjava.html)
* [LiquidJava specification examples for the Java standard library](https://github.com/CatarinaGamboa/liquid-java-external-libs)
<!-- * [Formalization of LiquidJava](https://github.com/CatarinaGamboa/liquidjava-formalization) - not opensource yet -->

## Getting Started

### VS Code Extension

The easiest way to use LiquidJava is through its [VS Code extension](https://github.com/CatarinaGamboa/vscode-liquidjava), which uses the LiquidJava verifier directly inside VS Code, with error diagnostics and syntax highlighting for refinements.

### Command Line

For development, you may use the LiquidJava verifier from the command line.

#### Prerequisites

Before setting up LiquidJava, ensure you have the following installed:

- Java 20+ - JDK for compiling and running Java programs
- Maven 3.6+ - For building and dependency management

Additionally, you'll need the following dependency, which includes the LiquidJava API annotations:

#### Maven
```xml
<dependency>
    <groupId>io.github.rcosta358</groupId>
    <artifactId>liquidjava-api</artifactId>
    <version>0.0.2</version>
</dependency>
```

#### Gradle
```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'io.github.rcosta358:liquidjava-api:0.0.2'
}
```

#### Setup

1. Clone the repository: `git clone https://github.com/CatarinaGamboa/liquidjava.git`
2. Build the project `mvn clean install`
3. Run tests to verify installation: `mvn test`
4. If importing into an IDE, import the project as a Maven project using the root `pom.xml`

#### Run Verification

To run LiquidJava, use the Maven command below, replacing `/path/to/your/project` with the path to the Java file or directory you want to verify.

```bash
mvn exec:java -pl liquidjava-verifier -Dexec.mainClass="liquidjava.api.CommandLineLauncher" -Dexec.args="/path/to/your/project"
```

If you're on Linux/macOS, you can use the `liquidjava` script (from the repository root) to simplify the process.

**Test a correct case**:
```bash
./liquidjava liquidjava-example/src/main/java/testSuite/CorrectSimpleAssignment.java
```

This should output: `Correct! Passed Verification`.

**Test an error case**:
```bash
./liquidjava liquidjava-example/src/main/java/testSuite/ErrorSimpleAssignment.java
```

This should output an error message describing the refinement violation.

#### Testing

Run `mvn test` to run all the tests in LiquidJava.

The starter test file is `TestExamples.java`, which runs the test suite under the `testSuite` directory in `liquidjava-example`.

The test suite considers test cases:
1. Files that start with `Correct` or `Error` (e.g., `CorrectRecursion.java`)
2. Packages or folders that contain the word `correct` or `error` (e.g., `arraylist_correct`)

Therefore, the files and folders that do not follow this pattern are ignored.

## Project Structure

* **docs**: Contains documents used for the design of the language. This folder includes a [README](./docs/design/README.md) with the link to the full artifact used in the design process. It also contains initial documents used to prepare the design of the refinements language during its evaluation
* **liquidjava-api**: Includes the annotations that can be introduced in the Java programs to add the refinements
* **liquidjava-example**: Includes some examples and the test suite used for testing the verifier
* **liquidjava-verifier**: Includes the implementation of the verifier. Its main packages are:
  * `api`: Includes the `CommandLineLauncher` that runs the verification on a given class or in the `currentlyTesting` directory if no argument is given
  * `ast`: Represents the Abstract Syntax Tree (AST) of the Refinements Language (RJ)
  * `errors`: Package for reporting the errors
  * `processor`: Package that handles the type checking
  * `rj_language`: Handles the parsing of the refinement strings to an AST
  * `smt`: Package that handles the translation to the SMT solver and the processing of the results the SMT solver produces
  * `utils`: Includes useful methods for all the previous packages
  * `test/java/liquidjava/api/tests`: Contains the `TestExamples` class used for running the test suite
