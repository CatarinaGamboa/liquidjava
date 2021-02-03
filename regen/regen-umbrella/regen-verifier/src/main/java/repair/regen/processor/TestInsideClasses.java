package repair.regen.processor;
import java.util.Optional;

import repair.regen.processor.context.RefinedVariable;
import spoon.Launcher;
import spoon.reflect.factory.Factory;

public class TestInsideClasses {
	public static void main(String[] args) {
		
		Launcher launcher = new Launcher();
        launcher.getEnvironment().setComplianceLevel(8);
        launcher.run();
        
        final Factory factory = launcher.getFactory();
		RefinedVariable vi = new RefinedVariable("abc",factory.Type().INTEGER_PRIMITIVE, "true");
		RefinedVariable vi2 = new RefinedVariable("abc1",factory.Type().INTEGER_PRIMITIVE, "abc1 > 0");
		RefinedVariable vi3 = new RefinedVariable("abc2",factory.Type().INTEGER_PRIMITIVE, "abc1 > 5");
		RefinedVariable vi4 = new RefinedVariable("abc3",factory.Type().INTEGER_PRIMITIVE, "abc1 > 5");
		
		vi.addInstance(vi2);
		vi.enterContext();
		vi.addInstance(vi3);
		vi.addInstance(vi4);
		Optional<RefinedVariable> ovi = vi.getLastInstance();
		if(ovi.isPresent()) {
			System.out.println(ovi.get().getName());
		}else {
			System.out.println("No last instance");
		}
	}

}
