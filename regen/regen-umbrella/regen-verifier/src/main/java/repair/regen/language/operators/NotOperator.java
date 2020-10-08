package repair.regen.language.operators;

import org.modelcc.IModel;
import org.modelcc.Pattern;

import com.microsoft.z3.Expr;

import repair.regen.language.Expression;
import repair.regen.smt.TranslatorToZ3;

@Pattern(regExp = "\\!")
public class NotOperator extends UnaryOperator implements IModel {
	@Override
	public Expr eval(TranslatorToZ3 ctx, Expression e1) {
		return ctx.mkNot(e1.eval(ctx));
	}
}
