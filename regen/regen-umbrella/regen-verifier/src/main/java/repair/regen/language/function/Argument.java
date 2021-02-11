package repair.regen.language.function;

import org.modelcc.IModel;
import org.modelcc.Optional;

import repair.regen.language.Expression;
import repair.regen.language.Variable;
import repair.regen.language.symbols.Coma;
/*
 * FArgs = emp | Arguments
 * Arguments = Exp FollowUp | Exp
 * FollowUp = , Arguments
 */
public class Argument implements IModel{
	Expression v;
	@Optional
	FollowUpArgument fua;
	
	public String toString() {
		return v.toString() + (fua == null? "":fua.toString());
	}

}
