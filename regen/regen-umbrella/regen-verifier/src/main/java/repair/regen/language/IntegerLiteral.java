package repair.regen.language;

import java.util.List;

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
	
	public IntegerLiteral() {}
	
	public IntegerLiteral(int i) {
		value = i;
	}
	

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
	@Override
	public void substituteVariable(String from, String to) {
		//End leaf - int literal
	}
	
	@Override
	public void getVariableNames(List<String> l) {
		//End leaf - int literal
	}
	
}
