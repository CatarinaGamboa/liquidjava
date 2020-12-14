package repair.regen.processor;

import java.util.ArrayList;
import java.util.List;

public class Utils {
	private Context context = Context.getInstance();
	
	
	public List<VariableInfo> searchForVars(String string){return searchForVars(string, "");} 
	public List<VariableInfo> searchForVars(String string, String differentFrom) {
		List<VariableInfo> l = new ArrayList<>();
		String[] a = string.split("&&|<|>|(<=)|(>=)|(==)|=|-|\\+|/|\\*|%|(\\|\\||\\(|\\))");//TODO missing OR and maybe other
		for(String s: a) {
			String t = s.replace(" ", "");
			if(!t.equals(differentFrom)) {
				VariableInfo v = context.getVariableByName(t);
				//System.out.println(t+": variable :"+v);
				if(v != null && !l.contains(v)) {
					l.add(v);
				}
			}
		}
		return l;
	}


}
