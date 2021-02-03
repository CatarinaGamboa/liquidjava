package repair.regen.language;

import org.modelcc.IModel;

import com.microsoft.z3.Expr;

import repair.regen.language.operators.UnaryOperator;
import repair.regen.smt.TranslatorToZ3;

public class UnaryExpression extends Expression implements IModel {
	UnaryOperator op;
	Expression e;

	@Override
	public Expr eval(TranslatorToZ3 ctx) {
		return op.eval(ctx, e);
	}

	@Override
	public String toString() {
		return op.toString()+e.toString();
	}
	
	public Expression getExpression() {
		return e;
	}
	
}