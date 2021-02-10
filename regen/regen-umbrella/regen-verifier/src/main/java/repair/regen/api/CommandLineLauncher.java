package repair.regen.api;
import java.util.Collection;

import repair.regen.processor.RefinementProcessor;
import spoon.Launcher;
import spoon.processing.ProcessingManager;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.factory.Factory;
import spoon.reflect.factory.PackageFactory;
import spoon.support.QueueProcessingManager;

public class CommandLineLauncher {
	public static void main(String[] args) {
		String file = args.length == 0? "../regen-example/src/main/java/":args[0];
		process(file);
	}

	static void process(String file) {
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
		final RefinementProcessor processor = new RefinementProcessor(factory);
		processingManager.addProcessor(processor);
		
		//To only search the last package - less time spent 
		CtPackage v = factory.Package().getAll().stream()
				  .reduce((first, second) -> second)
				  .orElse(null);
		if(v != null)
			processingManager.process(v);
		//To search all previous packages
		//processingManager.process(factory.Package().getRootPackage());

        System.out.println("Analysis complete!");
	}
}
