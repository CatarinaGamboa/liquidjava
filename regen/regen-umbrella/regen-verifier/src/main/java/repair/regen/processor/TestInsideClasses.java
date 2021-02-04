package repair.regen.processor;
import java.util.List;

import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.context.RefinedVariable;
import spoon.Launcher;
import spoon.reflect.factory.Factory;

public class TestInsideClasses {
	public static void main(String[] args) {
		
		Launcher launcher = new Launcher();
        launcher.getEnvironment().setComplianceLevel(8);
        launcher.run();
        
        final Factory factory = launcher.getFactory();
		RefinedVariable vi2 = new RefinedVariable("_",factory.Type().INTEGER_PRIMITIVE, new Predicate("_ > 0"));
		Constraint c2 = vi2.getRenamedRefinements("_");
		
		List<String> ls = c2.getVariableNames();
		for(String s : ls)
			System.out.println(s);
		

		System.out.println(c2.toString());
	}

}
