package repair.regen.language;

import org.modelcc.IModel;
import org.modelcc.Priority;
import org.modelcc.Value;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

@Priority(7)
public class IntegerLiteral extends LiteralExpression implements IModel {
	@Value
	int value;

	@Override
	public Expr eval(TranslatorToZ3 ctx) {
		return ctx.makeIntegerLiteral(value);
	}
}
