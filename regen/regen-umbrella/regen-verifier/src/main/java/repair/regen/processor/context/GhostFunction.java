package repair.regen.processor.context;

import java.util.ArrayList;
import java.util.List;

import repair.regen.language.function.FunctionDeclaration;
import repair.regen.language.function.MultipleTypes;
import repair.regen.language.function.Type;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class GhostFunction {
	
	private String name;
	private List<CtTypeReference<?>> param_types;
	private CtTypeReference<?> return_type;
	
	public GhostFunction(FunctionDeclaration fdExp, Factory factory) {
		name = fdExp.getName().toString();
		Type t = fdExp.getReturnType();
		return_type = getType(t.toString(), factory);

		param_types = new ArrayList<>();
		MultipleTypes[] pts = fdExp.getArgTypes();
		for(MultipleTypes mt: pts) {
			param_types.add(getType(mt.getType().toString(), factory));
		}
	}
	
	public String getName() {
		return name;
	}
	
	public CtTypeReference<?> getReturnType() {
		return return_type;
	}

	public List<CtTypeReference<?>> getParametersTypes(){
		return param_types; 
	}
	
	private CtTypeReference<?> getType(String type, Factory factory) {
		//TODO complete
		switch(type) {
		case "int":return factory.Type().INTEGER_PRIMITIVE;
		case "double":return factory.Type().DOUBLE_PRIMITIVE;
		case "boolean": return factory.Type().BOOLEAN_PRIMITIVE;
		case "int[]": return factory.createArrayReference(getType("int", factory));
		case "String": return factory.Type().STRING;
		case "List": return factory.Type().LIST;
		default:
			return factory.Type().OBJECT;
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ghost "+return_type.toString()+" "+name+"(");
		for(CtTypeReference<?> t: param_types) {
			sb.append(t.toString()+" ,");
		}
		sb.delete(sb.length()-2, sb.length());
		sb.append(")");
		return sb.toString();
	}

}
