package repair.regen.processor;

import java.util.ArrayList;
import java.util.List;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtPackage;

/**
 * Finds circular dependencies between packages
 */
public class RefinementProcessor extends AbstractProcessor<CtPackage> {
	
	List<CtPackage> visitedPackages = new ArrayList<>();
	
	@Override
	public void process(CtPackage pkg) {
		if (!visitedPackages.contains(pkg)) {
			visitedPackages.add(pkg);
			pkg.accept(new RefinementTypeChecker());
		}
		
	}



}