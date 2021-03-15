package repair.regen.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import repair.regen.processor.context.Context;
import repair.regen.processor.refinement_checker.ExternalRefinementTypeChecker;
import repair.regen.processor.refinement_checker.RefinementTypeChecker;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.factory.Factory;

/**
 * Finds circular dependencies between packages
 */
public class RefinementProcessor extends AbstractProcessor<CtPackage> {
	
	List<CtPackage> visitedPackages = new ArrayList<>();
	Factory factory;
	
	public RefinementProcessor(Factory factory) {
		this.factory = factory;
	}

	@Override
	public void process(CtPackage pkg) {
		if (!visitedPackages.contains(pkg)) {
			Set<CtPackage> sp = pkg.getPackages();
			visitedPackages.add(pkg);
			Context c = Context.getInstance();
			c.reinitializeAllContext();
			pkg.accept(new ExternalRefinementTypeChecker(c, factory));
			pkg.accept(new RefinementTypeChecker(c, factory));
		}

	}



}