package repair.regen.language.operators;

import org.modelcc.Pattern;
import org.modelcc.Priority;

import com.microsoft.z3.Expr;

import repair.regen.language.Expression;
import repair.regen.smt.TranslatorToZ3;

@Priority(value=10)
@Pattern(regExp = "&&")
public class AndOperator extends BinaryOperator {

	@Override
	public Expr eval(TranslatorToZ3 ctx, Expression e1, Expression e2) throws Exception{
//		System.out.println("Expression1:"+e1);
//		System.out.println("Expression2:"+e2);
		return ctx.makeAnd(e1.eval(ctx), e2.eval(ctx));
	}

	@Override
	public String toString() {
		return "&&";
	}
	

}
