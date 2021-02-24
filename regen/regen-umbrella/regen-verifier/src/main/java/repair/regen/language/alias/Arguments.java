package repair.regen.language.alias;

import java.util.List;

import org.modelcc.IModel;
import org.modelcc.Optional;

import repair.regen.language.Variable;

public class Arguments implements IModel{
	Variable var;
	@Optional
	ArgumentFollowUp afu;
	
	
	public void substituteVariable(String from, String to) {
		var.substituteVariable(from, to);
		if(afu != null)
			afu.substituteVariable(from, to);	
	}


	public void getVariableNames(List<String> l) {
		var.getVariableNames(l);
		if(afu != null)
			afu.getVariableNames(l);	
		
	}


	public void getVariables(List<Variable> lv) {
		lv.add(var);
		if(afu != null)
			afu.getVariables(lv);
		
	}
	
	public String toString() {
		String r = var.toString();
		if(afu != null)
			r = r +afu.toString();
		return r;
	}

}
