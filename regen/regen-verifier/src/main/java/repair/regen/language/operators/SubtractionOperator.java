package repair.regen.language.operators;

import org.modelcc.IModel;
import org.modelcc.Pattern;
import org.modelcc.Priority;

import com.microsoft.z3.Expr;

import repair.regen.language.Expression;
import repair.regen.smt.TranslatorToZ3;

@Priority(value = 2)
@Pattern(regExp = "-")
public class SubtractionOperator extends BinaryOperator implements IModel {
	@Override
	public Expr eval(TranslatorToZ3 ctx, Expression e1, Expression e2) {
		return null;
	}
}
