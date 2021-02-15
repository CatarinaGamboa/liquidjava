package repair.regen.language.function;

import java.util.List;

import org.modelcc.IModel;
import org.modelcc.Optional;

import com.microsoft.z3.Expr;

import repair.regen.language.Expression;
import repair.regen.language.symbols.Coma;
import repair.regen.smt.TranslatorToZ3;
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


	public void eval(TranslatorToZ3 ctx, List<Expr> l) {
		arg.eval(ctx, l);
	}

}
