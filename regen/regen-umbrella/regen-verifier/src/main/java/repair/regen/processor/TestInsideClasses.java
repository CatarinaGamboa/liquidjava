package repair.regen.processor;
import java.util.Optional;

import repair.regen.processor.context.VariableInfo;
import spoon.Launcher;
import spoon.reflect.factory.Factory;

public class TestInsideClasses {
	public static void main(String[] args) {
		
		Launcher launcher = new Launcher();
        launcher.getEnvironment().setComplianceLevel(8);
        launcher.run();
        
        final Factory factory = launcher.getFactory();
		VariableInfo vi = new VariableInfo("abc",factory.Type().INTEGER_PRIMITIVE, "true");
		VariableInfo vi2 = new VariableInfo("abc1",factory.Type().INTEGER_PRIMITIVE, "abc1 > 0");
		VariableInfo vi3 = new VariableInfo("abc2",factory.Type().INTEGER_PRIMITIVE, "abc1 > 5");
		VariableInfo vi4 = new VariableInfo("abc3",factory.Type().INTEGER_PRIMITIVE, "abc1 > 5");
		
		vi.addInstance(vi2);
		vi.enterContext();
		vi.addInstance(vi3);
		vi.addInstance(vi4);
		Optional<VariableInfo> ovi = vi.getLastInstance();
		if(ovi.isPresent()) {
			System.out.println(ovi.get().getName());
		}else {
			System.out.println("No last instance");
		}
	}

}
