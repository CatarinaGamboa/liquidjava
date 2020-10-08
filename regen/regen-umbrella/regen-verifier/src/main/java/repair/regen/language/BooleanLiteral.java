package repair.regen.language;

import org.modelcc.IModel;
import org.modelcc.Priority;
import org.modelcc.Value;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

@Priority(10)
public class BooleanLiteral extends LiteralExpression implements IModel {
	@Value
	boolean value;

	@Override
	public Expr eval(TranslatorToZ3 ctx) {
		return ctx.makeBooleanLiteral(value);
	}
}