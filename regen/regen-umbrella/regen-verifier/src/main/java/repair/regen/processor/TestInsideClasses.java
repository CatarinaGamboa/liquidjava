package repair.regen.processor;
import java.util.ArrayList;
import java.util.List;

import repair.regen.processor.constraints.Conjunction;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.EqualsPredicate;
import repair.regen.processor.constraints.FunctionPredicate;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.context.GhostState;
import repair.regen.processor.context.RefinedVariable;
import repair.regen.processor.context.Variable;
import spoon.Launcher;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class TestInsideClasses {
	public static void main(String[] args) {
		
		Launcher launcher = new Launcher();
        launcher.getEnvironment().setComplianceLevel(8);
        launcher.run();
        
        final Factory factory = launcher.getFactory();
		RefinedVariable vi2 = new Variable("a",factory.Type().INTEGER_PRIMITIVE, new Predicate("a > 0"));
		CtTypeReference intt = factory.Type().INTEGER_PRIMITIVE;
		List<CtTypeReference<?>> l = new ArrayList<>();
		l.add(intt);
		GhostState s = new GhostState("green", l, intt, "A");
		GhostState ss = new GhostState("yellow", l, intt, "A");
		GhostState sss = new GhostState("red", l, intt, "A");
		List<GhostState> gh = new ArrayList<>();
		gh.add(s);gh.add(ss);gh.add(sss);
		Predicate p = new Predicate("green(this) && red(this) == 10 && u(3)");
		System.out.println(p.getGhostInvocations(gh));
		
	}

}
