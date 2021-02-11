package repair.regen.language;

import org.modelcc.IModel;

import com.microsoft.z3.Expr;

import repair.regen.language.operators.BinaryOperator;
import repair.regen.language.operators.IfElseOperator;
import repair.regen.language.operators.IfThenOperator;
import repair.regen.smt.TranslatorToZ3;

public class IfElseExpression  extends Expression implements IModel {
	Expression cond;
	IfThenOperator it;
	Expression then;
	IfElseOperator ie;
	Expression els;
	

	@Override
	public Expr eval(TranslatorToZ3 ctx) {
		return ctx.makeIte(cond.eval(ctx), then.eval(ctx), els.eval(ctx));
	}
	
	public Expression getCondition() {
		return cond;
	}
	public Expression getThenExpression() {
		return then;
	}
	public Expression getElseExpression() {
		return els;
	}

	@Override
	public String toString() {
		return String.format("%s %s %s %s %s", cond.toString(), it.toString(), 
				then.toString(), ie.toString(), els.toString());
	}

}