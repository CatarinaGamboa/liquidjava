package repair.regen.language;

import org.modelcc.IModel;
import org.modelcc.Prefix;
import org.modelcc.Suffix;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

@Prefix("\\(")
@Suffix("\\)")
public class ExpressionGroup extends Expression implements IModel {
	Expression e;

	@Override
	public Expr eval(TranslatorToZ3 ctx) {
//		System.out.println("IN ExpGroup"+ctx);
		return e.eval(ctx);
	}

	@Override
	public String toString() {
		return String.format("(%s)", e.toString());
	}
}