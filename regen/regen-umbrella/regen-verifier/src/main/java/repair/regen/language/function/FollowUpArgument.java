package repair.regen.language.function;

import org.modelcc.IModel;
import org.modelcc.Optional;

import repair.regen.language.Expression;
import repair.regen.language.symbols.Coma;
/*
 * FArgs = emp | Arguments
 * Arguments = Exp FollowUp | Exp
 * FollowUp = , Arguments
 */
public class FollowUpArgument implements IModel {
	Coma c;
	Argument arg;
	
	public String toString() {
		return ","+arg.toString();
	}

}
