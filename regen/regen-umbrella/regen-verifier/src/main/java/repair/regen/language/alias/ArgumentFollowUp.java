package repair.regen.language.alias;

import java.util.List;

import org.modelcc.IModel;

import repair.regen.language.Variable;
import repair.regen.language.symbols.Coma;

public class ArgumentFollowUp implements IModel{
	Coma c;
	Arguments arg;
	
	public void substituteVariable(String from, String to) {
		arg.substituteVariable(from, to);
	}

	public void getVariableNames(List<String> l) {
		arg.getVariableNames(l);
	}

	public void getVariables(List<Variable> lv) {
		arg.getVariables(lv);
	}

	public String toString() {
		return ", "+arg.toString();
	}
}
