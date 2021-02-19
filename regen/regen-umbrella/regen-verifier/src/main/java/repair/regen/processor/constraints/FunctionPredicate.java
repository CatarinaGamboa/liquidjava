package repair.regen.processor.constraints;

public class FunctionPredicate extends Predicate{
	
	public FunctionPredicate(String functionName, String... params) {
		super(functionName+"("+getFormattedParams(params)+")");
	}


	public static FunctionPredicate builtin_length(String param) {
		return new FunctionPredicate("length", param);
	}
	
	public static FunctionPredicate builtin_addToIndex(String array, String index, String value) {
		return new FunctionPredicate("addToIndex", index, value);
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
