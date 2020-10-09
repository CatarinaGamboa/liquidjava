# ReGen: Evolutionary Synthesis-based Program Repair from Refined Types

### Welcome to ReGen!

There are two folders in the repository:
- **ideas**: with the first ideas related to the projected of including refinement types in Java and the concept behind the program repair

- **regen**: folder which contain the system. The inside project, **regen-umbrella**, should be imported as a Maven project.

---
To properly run the tool do as follows:

1. Clone the repository

2. Open Eclipse and import the **regen-umbrella** project as follows: `File > Import > Maven > Existing Maven Project > regen-umbrella`

3. *Optional:* The Compiler and Build Path may raise some warnings because of compliance and the execution environment, to solve these, for each **regen-XXX** project, simply select `Properties > Java Compiler > Disable: Use compliance for execution environment... > Enable: Use '--release' option > Apply`.

4. Run the CommandLineLauncher in `regen-verifier.api` and done! 



**NOTE**: So that ModelCC can properly work, the path you are working on cannot have white spaces!!
