package repair.regen.language.alias;

import java.util.List;

import org.modelcc.IModel;

import repair.regen.language.symbols.Coma;

public class ParameterFollowUp implements IModel{
	Coma c;
	Parameter p;
	
	public String toString() {
		return ", "+p.toString();
	}

	public void getVariableNames(List<String> l) {
		p.getVariableNames(l);
	}

	public void getTypesNames(List<String> l) {
		p.getTypesNames(l);
	}
}
