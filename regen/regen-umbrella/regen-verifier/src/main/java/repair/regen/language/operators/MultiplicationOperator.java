package repair.regen.language.operators;

import org.modelcc.IModel;
import org.modelcc.Pattern;
import org.modelcc.Priority;

import com.microsoft.z3.Expr;

import repair.regen.language.Expression;
import repair.regen.smt.TranslatorToZ3;

@Priority(value = 1)
@Pattern(regExp = "\\*")
public class MultiplicationOperator extends BinaryOperator implements IModel {
	@Override
	public Expr eval(TranslatorToZ3 ctx, Expression e1, Expression e2) throws Exception{
		return ctx.makeMul(e1.eval(ctx), e2.eval(ctx));
	}
	
	@Override
	public String toString() {
		return "*";
	}
}
