package repair.regen.language;

import org.modelcc.IModel;
import org.modelcc.Priority;
import org.modelcc.Value;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

@Priority(1)
public class IntegerLiteral extends LiteralExpression implements IModel {
	@Value
	int value;

	public void setValue(int val) {
		value = val;
	}
	
	@Override
	public Expr eval(TranslatorToZ3 ctx) {
		return ctx.makeIntegerLiteral(value);
	}
}
