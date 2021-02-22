package repair.regen.language.operators;

import org.modelcc.IModel;
import org.modelcc.Pattern;
import org.modelcc.Priority;

import com.microsoft.z3.Expr;

import repair.regen.language.Expression;
import repair.regen.smt.TranslatorToZ3;
@Priority(1)
@Pattern(regExp = "\\!")
public class NotOperator extends UnaryOperator implements IModel  {
	@Override
	public Expr eval(TranslatorToZ3 ctx, Expression e1) throws Exception{
		return ctx.mkNot(e1.eval(ctx));
	}
	@Override
	public String toString() {
		return "!";
	}
}
