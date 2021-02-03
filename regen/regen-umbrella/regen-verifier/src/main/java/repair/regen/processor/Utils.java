package repair.regen.processor;

import java.util.ArrayList;
import java.util.List;

import repair.regen.processor.context.Context;
import repair.regen.processor.context.RefinedVariable;

public class Utils {
	private Context context = Context.getInstance();
	
	
	public List<RefinedVariable> searchForVars(String string){return searchForVars(string, "");} 
	public List<RefinedVariable> searchForVars(String string, String differentFrom) {
		List<RefinedVariable> l = new ArrayList<>();
		String[] a = string.split("&&|<|>|(<=)|(>=)|(==)|=|-|\\+|/|\\*|%|(\\|\\||\\(|\\)) ");//TODO missing OR and maybe other
		for(String s: a) {
			String t = s.replace(" ", "");
			t = t.replace("(", "");
			t = t.replace(")", "");
			if(!t.equals(differentFrom)) {
				RefinedVariable v = context.getVariableByName(t);
				//System.out.println(t+": variable :"+v);
				if(v != null && !l.contains(v)) {
					l.add(v);
				}
			}
		}
		return l;
	}


}
