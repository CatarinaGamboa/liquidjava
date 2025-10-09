package liquidjava.api;

import java.util.Arrays;
import java.util.List;

import liquidjava.errors.ErrorEmitter;
import liquidjava.processor.RefinementProcessor;
import spoon.Launcher;
import spoon.processing.ProcessingManager;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;

public class CommandLineLauncher {
    public static void main(String[] args) {
        // String allPath = "C://Regen/test-projects/src/Main.java";
        // In eclipse only needed this:"../liquidjava-example/src/main/java/"
        // In VSCode needs:
        // "../liquidjava/liquidjava-umbrella/liquidjava-example/src/main/java/liquidjava/test/project";

        if (args.length == 0) {
            System.out.println("No input files or directories provided");
            System.out.println("\nUsage: ./liquidjava <...paths>");
            System.out.println("  <...paths>: Paths to files or directories to be verified by LiquidJava");
            System.out.println("\nExample: ./liquidjava liquidjava-example/src/main/java/test/currentlyTesting liquidjava-example/src/main/java/testingInProgress/Account.java");
            return;
        }
        List<String> files = Arrays.asList(args);
        ErrorEmitter ee = launch(files.toArray(new String[0]));
        System.out.println(ee.foundError() ? (ee.getFullMessage()) : ("Correct! Passed Verification."));
    }

    /**
     * Launch the LiquidJava verifier on the given file (for testing purposes)
     * @param file Path to the file to be verified
     * @return ErrorEmitter containing any errors found during verification
     */
    public static ErrorEmitter launchTest(String file) {
        ErrorEmitter ee = launch(file);
        return ee;
    }

    /**
     * Launch the LiquidJava verifier on the given files
     * @param files Array of file paths to be verified
     * @return ErrorEmitter containing any errors found during verification
     */
    public static ErrorEmitter launch(String... files) {
        System.out.println("Running LiquidJava on: " + Arrays.toString(files).replaceAll("[\\[\\]]", ""));
        Launcher launcher = new Launcher();
        for (String file : files) {
            launcher.addInputResource(file);
        }
        launcher.getEnvironment().setNoClasspath(true);

        // Get the current classpath from the system
        // String classpath = System.getProperty("java.class.path");
        // launcher.getEnvironment().setSourceClasspath(classpath.split(File.pathSeparator));

        // optional
        // launcher.getEnvironment().setSourceClasspath(
        // "lib1.jar:lib2.jar".split(":"));
        launcher.getEnvironment().setComplianceLevel(8);

        launcher.run();

        final Factory factory = launcher.getFactory();
        final ProcessingManager processingManager = new QueueProcessingManager(factory);

        ErrorEmitter ee = new ErrorEmitter();
        final RefinementProcessor processor = new RefinementProcessor(factory, ee);
        processingManager.addProcessor(processor);

        try {
            // To only search the last package - less time spent
            // CtPackage v = factory.Package().getAll().stream().reduce((first, second) ->
            // second).orElse(null);
            // if (v != null)
            // processingManager.process(v);
            // To search all previous packages
            processingManager.process(factory.Package().getRootPackage());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return ee;
    }
}
