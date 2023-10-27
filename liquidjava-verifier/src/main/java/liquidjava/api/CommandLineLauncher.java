package liquidjava.api;

import liquidjava.errors.ErrorEmitter;
import liquidjava.processor.RefinementProcessor;
import spoon.Launcher;
import spoon.processing.ProcessingManager;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;

public class CommandLineLauncher {
    public static void main(String[] args) {
        String allPath = "../liquidjava-example/src/main/java/test/project";

        // String allPath = "C://Regen/test-projects/src/Main.java";
        // In eclipse only needed this:"../liquidjava-example/src/main/java/"
        // In VSCode needs:
        // "../liquidjava/liquidjava-umbrella/liquidjava-example/src/main/java/liquidjava/test/project";
        String file = args.length == 0 ? allPath : args[0];
        ErrorEmitter ee = launch(file);
        if (ee.foundError()) {
            System.out.println(ee.getFullMessage());
            System.exit(ee.getErrorStatus());
        } else {
            System.out.println("Analysis complete!");
        }
    }

    public static ErrorEmitter launchTest(String file) {
        ErrorEmitter ee = launch(file);
        if (ee.foundError()) {
            System.out.println(ee.getFullMessage());
//            System.exit(ee.getErrorStatus());
        } else {
            System.out.println("Analysis complete!");
        }
        return ee;
    }

    public static ErrorEmitter launch(String file) {
        Launcher launcher = new Launcher();
        System.out.println("File path in launch before spoon:" + file);
        launcher.addInputResource(file);
        launcher.getEnvironment().setNoClasspath(true);
        // optional
        // launcher.getEnvironment().setSourceClasspath(
        // "lib1.jar:lib2.jar".split(":"));
        launcher.getEnvironment().setComplianceLevel(8);

        System.out.println("before run");
        launcher.run();

        System.out.println("after run");

        final Factory factory = launcher.getFactory();
        final ProcessingManager processingManager = new QueueProcessingManager(factory);

        ErrorEmitter ee = new ErrorEmitter();
        final RefinementProcessor processor = new RefinementProcessor(factory, ee);
        processingManager.addProcessor(processor);

        System.out.println("before process");

        // To only search the last package - less time spent
        CtPackage v = factory.Package().getAll().stream().reduce((first, second) -> second).orElse(null);
        if (v != null)
            processingManager.process(v);
        // To search all previous packages
        // processingManager.process(factory.Package().getRootPackage());

        return ee;
    }
}
