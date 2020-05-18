package repair.regen.language.operators;

import org.modelcc.IModel;
import org.modelcc.Pattern;

import com.microsoft.z3.Expr;

import repair.regen.language.Expression;
import repair.regen.smt.TranslatorToZ3;

@Pattern(regExp = "-")
public class MinusOperator extends UnaryOperator implements IModel {
	@Override
	public Expr eval(TranslatorToZ3 ctx, Expression e) {
		return null;
	}
}
