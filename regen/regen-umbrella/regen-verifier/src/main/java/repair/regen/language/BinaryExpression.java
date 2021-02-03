package repair.regen.language;

import org.modelcc.IModel;

import com.microsoft.z3.Expr;

import repair.regen.language.operators.BinaryOperator;
import repair.regen.smt.TranslatorToZ3;

public class BinaryExpression extends Expression implements IModel {
	Expression e1;
	BinaryOperator op;
	Expression e2;
	
	@Override
	public Expr eval(TranslatorToZ3 ctx) {
		return op.eval(ctx, e1, e2);
	}

	@Override
	public String toString() {
		String se1 = e1.toString();
		String sop = op.toString();
		String se2 = e2.toString();
		return String.format("%s %s %s", se1,sop,se2);
	}
}