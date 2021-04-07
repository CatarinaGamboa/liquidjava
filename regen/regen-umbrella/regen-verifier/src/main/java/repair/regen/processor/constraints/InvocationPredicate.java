package repair.regen.processor.constraints;

public class InvocationPredicate extends Predicate{

	public InvocationPredicate(String name, String... params) {
		StringBuilder sb = new StringBuilder(name+"(");
		for(String s: params) {
			sb.append(s + ",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(")");
		exp = sb.toString();
	}
	
	public InvocationPredicate(String name, String e) {
		exp = String.format("%s(%s)", name, e);
	}
}
