package repair.regen.processor.constraints;

import repair.regen.utils.ErrorEmitter;
import spoon.reflect.declaration.CtElement;

public class FunctionPredicate extends Predicate{
	
	public FunctionPredicate(ErrorEmitter ee, CtElement elem, String functionName, String... params) {
		super(functionName+"("+getFormattedParams(params)+")", elem, ee);
	}


	public static FunctionPredicate builtin_length(String param, CtElement elem, ErrorEmitter ee) {
		return new FunctionPredicate(ee, elem, "length", param);
	}
	
	public static FunctionPredicate builtin_addToIndex(String array, String index, String value, CtElement elem, ErrorEmitter ee) {
		return new FunctionPredicate(ee, elem, "addToIndex", index, value);
	}
	
	
	
	private static String getFormattedParams(String... params) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < params.length; i++) {
			sb.append(params[i]);
			if(i < params.length-1)
				sb.append(", ");
		}
		return sb.toString(); 
	}
	
}
