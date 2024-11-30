# LiquidJava - Extending Java with Liquid Types

![LiquidJava Banner](docs/design/figs/banner.gif)


### Welcome to LiquidJava!

LiquidJava is an additional typechecker for Java that supports liquid types and typestate.

Simple example:


```java
@Refinement("a > 0")
int a = 3; // okay
a = -8; // type error!
```

This project has the LiquidJava verifier, the api and some examples for testing.
You can find out more about LiquidJava in the following resources:

* [Website](https://catarinagamboa.github.io/liquidjava.html)
* [Examples of LiquidJava](https://github.com/CatarinaGamboa/liquidjava-examples)
* [LiquidJava Specification of Java StdLib](https://github.com/CatarinaGamboa/liquid-java-external-libs)
* [VSCode plugin for LiquidJava](https://github.com/CatarinaGamboa/vscode-liquidjava)
<!-- * [Formalization of LiquidJava](https://github.com/CatarinaGamboa/liquidjava-formalization) - not opensource yet -->


## Setup the project
1. Clone the repository;
2. Run `setup.sh`, some dependencies include using `Java 20` or newer and using `Maven`.
3. Open the project in your favorite IDE (we have used Eclipse and VSCode)
4. Use the `.pom` in `liquidjava-umbrella` to `compile` and run the `tests`

## Run verification
#### In a specific project
Run the file `liquidjava-verifier\api\CommandLineLaucher` with the path to the target project for verification.
If there are no arguments given, the application verifies the project on the path `liquidjava-example\src\main\java`.


## Testing
Run `mvn test` to run all the tests in liquidjava.

The starter test file is `TestExamples.java` which uses the test suite under the `liquidjava-examples/testSuite`. 

Paths in the testSuite are considered test cases if:
1. File that start with `Correct` or `Error` (e.g, "CorrectRecursion.java")
2. Package/Folder that contains the word `correct` or `error`.

Therefore, files/folders that do not pass this description are not verified.

## Project structure
- **docs**: documents used for the design of the language. The folder includes a readme to a full artifact used in the design process, here are some initial documents used to prepare the design of the refinements language at its evaluation
- **liquidjava-api**: inlcudes the annotations that can be introduced in the Java programs to add the refinements
- **liquidjava-examples**: includes a main folder with the current example that the verifier is testing; the test suite that is used in maven test is under the `testSuite` folder
- **liquidjava-verifier**: has the project for verification of the classes
    - *api*: classes that test the verifier. Includes the `CommandLineLauncher` that runs the verification on a given class or on the main folder of `liquidjava-examples` if no argument is given. This package includes the JUnit tests to verify if the examples in `liquidjava-example/tests` are correctly verified.
    - *ast*: represents the abstract syntax tree of the refinement's language.
    - *errors*: package for reporting the errors.
    - *processor*: package that handles the type checking.
    - *rj_language*: handles the processing of the strings with refinements.
    - *smt*: package that handles the translation to the smt solver and the processing of the results the smt solver produces.
    - *utils*: includes useful methods for all the previous packages.
    - *test/java/liquidjava/api/tests* contains the `TestExamples` class used for running the testSuite


