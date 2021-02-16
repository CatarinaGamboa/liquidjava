package repair.regen.language;

import org.modelcc.IModel;
import org.modelcc.Pattern;
import org.modelcc.Priority;
import org.modelcc.Value;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

@Priority(10)
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

	@Override
	public String toString() {
		return Integer.toString(value);
	}
}
