package repair.regen.api;
import repair.regen.processor.RefinementProcessor;
import spoon.Launcher;
import spoon.processing.ProcessingManager;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;

public class CommandLineLauncher {
	public static void main(String[] args) {
		Launcher launcher = new Launcher();
        launcher.addInputResource("../regen-example/src/main/java/");
        launcher.getEnvironment().setNoClasspath(true);
        // optional
        // launcher.getEnvironment().setSourceClasspath(
        //        "lib1.jar:lib2.jar".split(":"));
        launcher.getEnvironment().setComplianceLevel(8);
        launcher.run();
        
        final Factory factory = launcher.getFactory();
		final ProcessingManager processingManager = new QueueProcessingManager(factory);
		final RefinementProcessor processor = new RefinementProcessor();
		processingManager.addProcessor(processor);
		processingManager.process(factory.Package().getRootPackage());

        System.out.println("Analysis complete!");
	}
}
