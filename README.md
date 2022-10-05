# LiquidJava - Extending Java with Refinement Types

### Welcome to LiquidJava!

This project implements a prototype of LiquidJava which adds refinement types to the Java language.

The project has 4 main folders:
- **syntax_survey**: survey sent to Java developers about the syntax of the refinements and results obtained.
- **evaluation_survey**: survey questions and results for the usability study on the use of refinement types in Java.
- **ideas**: possible future directions.
- **regen**: finally, the prototype itself.


## Setup the project
1. Clone the repository;
2. Open Eclipse and do `Ìmport...\ Existing Maven Project` selecting the folder `liquidjava-umbrella` and waiting for the build process to finish (bottom right update bar)
3. Select the `liquidjava-umbrella` folder, right click it and select `Run as...\Maven install...`
4. Select `liquidjava-verifier`, on right-click, go to `Build Path\Link Source...` and browse for `liquidjava-verifier\target\generated-sources\antlr4` and select `Finish`

## Run verification
#### In a specific project
Run the file `liquidjava-verifier\api\CommandLineLaucher` with the path to the target project for verification.
If there are no arguments given, the application verifies the project on the path `liquidjava-example\src\main\java`.

#### JUnit tests
Inside the folder `liquidjava-verifier\api\tests` are three classes that test several examples available at `liquidjava-example\src\test\java`.
To run the JUnit tests in one of the files, select it, right-click it and `Run as...\JUnit tests`.
To run the all test suit, select the package `tests` (`liquidjava-verifier\api\tests`), right-click it, and do `Run as...\JUnit tests`.
Make sure to run these tests after making changes in the verification code.

## Project structure
- **regen-api**: inlcudes the annotations that can be introduced in the Java programs to add the refinements
- **regen-examples**: includes a main folder with the current example that the verifier is testing; and inlcudes the classes for the tests
- **regen-verifier**: has the project for verification of the classes
    - *api*: classes that test the verifier. Includes the `CommandLineLauncher` that runs the verification on a given class or on the main folder of `liquidjava-examples` if no argument is given. This package includes the JUnit tests to verify if the examples in `liquidjava-example/tests` are correctly verified.
    - *ast*: represents the abstract syntax tree of the refinement's language.
    - *errors*: package for reporting the errors.
    - *processor*: package that handles the type checking.
    - *rj_language*: handles the processing of the strings with refinements.
    - *smt*: package that handles the translation to the smt solver and the processing of the results the smt solver produces.
    - *utils*: includes useful methods for all the previous packages.



---------------------
[ Future WORK ]

# ReGen: Evolutionary Synthesis-based Program Repair from Refined Types

### Welcome to ReGen!

There are two folders in the repository:
- **ideas**: with the first ideas related to the projected of including refinement types in Java and the concept behind the program repair

- **regen**: folder which contain the system. The inside project, **regen-umbrella**, should be imported as a Maven project.
