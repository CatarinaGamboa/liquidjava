package repair.regen.language.alias;

import java.util.List;

import org.modelcc.IModel;
import org.modelcc.Optional;

import repair.regen.language.function.Type;

public class Parameter implements IModel{
	Type type;
	Var var;
	@Optional
	ParameterFollowUp pfu;
	
	public String toString() {
		return type.toString()+" "+var.toString()+(pfu == null? "":pfu.toString());
	}

	public void getVariableNames(List<String> l) {
		l.add(var.toString());
		if(pfu != null)
			pfu.getVariableNames(l);
	}

	public void getTypesNames(List<String> l) {
		l.add(type.toString());
		if(pfu != null)
			pfu.getTypesNames(l);
		
	}

}
