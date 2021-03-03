package repair.regen.processor.context;

import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class Utils {

	public static CtTypeReference<?> getType(String type, Factory factory) {
		//TODO complete
		switch(type) {
		case "int":return factory.Type().INTEGER_PRIMITIVE;
		case "double":return factory.Type().DOUBLE_PRIMITIVE;
		case "boolean": return factory.Type().BOOLEAN_PRIMITIVE;
		case "int[]": return factory.createArrayReference(getType("int", factory));
		case "String": return factory.Type().STRING;
		case "List": return factory.Type().LIST;
		default:
//			return factory.Type().OBJECT;
			return factory.createReference(type);
		}
	}
}
