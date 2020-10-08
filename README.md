# ReGen: Evolutionary Synthesis-based Program Repair from Refined Types

### Welcome to ReGen!

There are three folders in the repository:
- **ideas**: with the first ideas related to the projected of including refinement types in Java and the concept behind the program repair

- **z3-4.8.9-x64-win**: dependency required to verify the refinements. Instructions below on how to add it to your classpath.

- **regen**: folder which contain the system. The inside project, **regen-umbrella**, should be imported as a Maven project.

---
To properly run the tool do as follows:

 1. Clone the repository
 
 2. Add the **z3-4.8.9-x64-win** bin folder to the classpath: If you are using Windows, the `PATH` should be:
`path\to\git\regen\z3-4.8.9-x64-win\bin`

3. Open Eclipse and import the **regen-umbrella** project as follows: `File > Import > Maven > Existing Maven Project > regen-umbrella`

4. *Optional:* The Compiler and Build Path may raise some warnings because of compliance and the execution environment, to solve these, for each **regen-XXX** project, simply select `Properties > Java Compiler > Disable: Use compliance for execution environment... > Enable: Use '--release' option > Apply`.

5. Run the CommandLineLauncher in `regen-verifier.api` and done! 
