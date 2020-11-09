package repair.regen.language.operators;

import org.modelcc.Pattern;
import org.modelcc.Priority;

import com.microsoft.z3.Expr;

import repair.regen.language.Expression;
import repair.regen.smt.TranslatorToZ3;

@Priority(value=3)
@Pattern(regExp = ">=")
public class GreaterOrEqualOperator extends BinaryOperator {

	@Override
	public Expr eval(TranslatorToZ3 ctx, Expression e1, Expression e2) {
		System.out.println("VEIO AQUI");
		return ctx.makeGtEq(e1.eval(ctx), e2.eval(ctx));
	}
	

}
