package repair.regen.processor;
import java.util.List;

import repair.regen.processor.constraints.Conjunction;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.EqualsPredicate;
import repair.regen.processor.constraints.FunctionPredicate;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.context.RefinedVariable;
import repair.regen.processor.context.Variable;
import spoon.Launcher;
import spoon.reflect.factory.Factory;

public class TestInsideClasses {
	public static void main(String[] args) {
		
		Launcher launcher = new Launcher();
        launcher.getEnvironment().setComplianceLevel(8);
        launcher.run();
        
        final Factory factory = launcher.getFactory();
		RefinedVariable vi2 = new Variable("a",factory.Type().INTEGER_PRIMITIVE, new Predicate("a > 0"));
		Constraint c2 = vi2.getRenamedRefinements("_");
		
		
		FunctionPredicate fp = new FunctionPredicate("ola", "a", "b");

		System.out.println(c2.toString());
	}

}
