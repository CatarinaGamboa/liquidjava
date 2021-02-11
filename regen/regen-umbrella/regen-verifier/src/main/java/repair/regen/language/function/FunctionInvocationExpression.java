package repair.regen.language.function;

import org.modelcc.IModel;
import org.modelcc.Multiplicity;
import org.modelcc.Optional;

import com.microsoft.z3.Expr;

import repair.regen.language.Expression;
import repair.regen.language.symbols.ParenthesisLeft;
import repair.regen.language.symbols.ParenthesisRight;
import repair.regen.smt.TranslatorToZ3;

public class FunctionInvocationExpression extends Expression implements IModel{
	FunctionName name;
	ParenthesisLeft pl;
	
//	@Multiplicity(minimum=0,maximum=1000)
//	@Optional
	Argument mv;
	/*
	 * FArgs = emp | Arguments
	 * Arguments = Exp FollowUp | Exp
	 * FollowUp = , Arguments
	 */
	ParenthesisRight pr;

	@Override
	public Expr eval(TranslatorToZ3 ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name.toString()+" (");
		sb.append(mv.toString());

		sb.append(")");
		return sb.toString();
	}
	
	
	
	
}
