package repair.regen.language.function;

import java.util.List;

import org.modelcc.IModel;
import org.modelcc.Optional;

import com.microsoft.z3.Expr;

import repair.regen.language.Expression;
import repair.regen.smt.TranslatorToZ3;
/*
 * FArgs = emp | Arguments
 * Arguments = Exp FollowUp | Exp
 * FollowUp = , Arguments
 */
public class Argument implements IModel{
	Expression v;
	@Optional
	FollowUpArgument fua;
	
	public void eval(TranslatorToZ3 ctx, List<Expr> l) {
		l.add(v.eval(ctx));
		if(fua != null)
			fua.eval(ctx, l);
	}
	

	public String toString() {
		return v.toString() + (fua == null? "":fua.toString());
	}

}
