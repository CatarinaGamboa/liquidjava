package repair.regen.language;

import java.util.List;

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
	public Expr eval(TranslatorToZ3 ctx) throws Exception {
		return e.eval(ctx);
	}
	
	@Override
	public String toString() {
		return String.format("(%s)", e.toString());
	}
	
	public Expression getExpression() {
		return e;
	}
	@Override
	public void substituteVariable(String from, String to) {
		e.substituteVariable(from, to);
	}
	
	@Override
	public void getVariableNames(List<String> l) {
		e.getVariableNames(l);
	}
}