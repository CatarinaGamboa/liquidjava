package repair.regen.language;

import org.modelcc.IModel;
import org.modelcc.Pattern;
import org.modelcc.Priority;
import org.modelcc.Value;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

@Priority(5)
@Pattern(regExp = "(true)|(false)")
public class BooleanLiteral extends LiteralExpression implements IModel {
	@Value
	boolean value;

	@Override
	public Expr eval(TranslatorToZ3 ctx) {
		return ctx.makeBooleanLiteral(value);
	}

	@Override
	public String toString() {
		return Boolean.toString(value);
	}
	
	public boolean getValue() {
		return value;
	}

	@Override
	public void substituteVariable(String from, String to) {
		//End leaf - boolean literal
	}
	
}