package repair.regen.language;

import java.util.List;

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
	
	public BooleanLiteral() {};
	public BooleanLiteral(boolean v) {
		this.value = v;
	}

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

	@Override
	public void getVariableNames(List<String> l) {
		//End leaf - boolean literal
	}
	
}