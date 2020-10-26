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
		//System.out.println("Aqui");
		return ctx.makeBooleanLiteral(value);
	}
}