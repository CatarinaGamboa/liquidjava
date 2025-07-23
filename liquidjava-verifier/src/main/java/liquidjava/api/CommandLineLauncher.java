package liquidjava.api;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import liquidjava.errors.ErrorEmitter;
import liquidjava.processor.RefinementProcessor;
import spoon.Launcher;
import spoon.processing.ProcessingManager;
import spoon.reflect.declaration.CtPackage;
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
            System.out.println(
                    "\nExample: ./liquidjava liquidjava-example/src/main/java/test/currentlyTesting liquidjava-example/src/main/java/testingInProgress/Account.java");
            return;
        }
        List<String> paths = Arrays.asList(args);
        ErrorEmitter ee = launch(paths.toArray(new String[0]));
        System.out.println(ee.foundError() ? (ee.getFullMessage()) : ("Correct! Passed Verification."));
    }

    public static ErrorEmitter launchTest(String path) {
        ErrorEmitter ee = launch(path);
        return ee;
    }

    public static ErrorEmitter launch(String... paths) {
        System.out.println("Running LiquidJava on: " + Arrays.toString(paths).replaceAll("[\\[\\]]", ""));

        ErrorEmitter ee = new ErrorEmitter();
        Launcher launcher = new Launcher();
        for (String path : paths) {
            if (!new File(path).exists()) {
                ee.addError("Path not found", "The path " + path + " does not exist", 1);
                return ee;
            }
            launcher.addInputResource(path);
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

        final RefinementProcessor processor = new RefinementProcessor(factory, ee);
        processingManager.addProcessor(processor);

        try {
            // analyze all packages
            CtPackage root = factory.Package().getRootPackage();
            if (root != null)
                processingManager.process(root);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return ee;
    }
}
