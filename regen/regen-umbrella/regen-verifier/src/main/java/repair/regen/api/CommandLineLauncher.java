package repair.regen.api;

import repair.regen.errors.ErrorEmitter;
import repair.regen.processor.RefinementProcessor;
import spoon.Launcher;
import spoon.processing.ProcessingManager;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;

public class CommandLineLauncher {
	public static void main(String[] args) {
		String allPath = "../regen-example/src/main/java/regen/test/project";
		
//		String allPath = "C://Regen/test-projects/src/Main.java";
		//In eclipse only needed this:"../regen-example/src/main/java/"
		//In VSCode needs: "../regen/regen-umbrella/regen-example/src/main/java/regen/test/project";
		String file = args.length == 0? allPath:args[0];
		ErrorEmitter ee = launch(file);
		if(ee.foundError()) {
			System.out.println(ee.getFullMessage());
			System.exit(ee.getErrorStatus());
		}else {
			System.out.println("Analysis complete!");
		}
	}
	
	public static void launchTest(String file) {
		ErrorEmitter ee = launch(file);
		if(ee.foundError()) {
			System.out.println(ee.getFullMessage());
			System.exit(ee.getErrorStatus());
		}else {
			System.out.println("Analysis complete!");
		}
	}

	public static ErrorEmitter launch(String file) {
		Launcher launcher = new Launcher();
        launcher.addInputResource(file);
        launcher.getEnvironment().setNoClasspath(true);
        // optional
        // launcher.getEnvironment().setSourceClasspath(
        //        "lib1.jar:lib2.jar".split(":"));
        launcher.getEnvironment().setComplianceLevel(8);
        launcher.run();
        
        final Factory factory = launcher.getFactory();
		final ProcessingManager processingManager = new QueueProcessingManager(factory);
		
		ErrorEmitter ee = new ErrorEmitter();
		final RefinementProcessor processor = new RefinementProcessor(factory, ee);
		processingManager.addProcessor(processor);
		
		//To only search the last package - less time spent 
		CtPackage v = factory.Package().getAll().stream()
				  .reduce((first, second) -> second)
				  .orElse(null);
		if(v != null)
			processingManager.process(v);
		//To search all previous packages
		//processingManager.process(factory.Package().getRootPackage());
		
		
		return ee;
		
	}
}
