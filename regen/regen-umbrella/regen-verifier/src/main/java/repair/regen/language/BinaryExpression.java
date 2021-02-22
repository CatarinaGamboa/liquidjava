package repair.regen.language;

import java.util.List;

import org.modelcc.IModel;
import org.modelcc.Priority;

import com.microsoft.z3.Expr;

import repair.regen.language.operators.BinaryOperator;
import repair.regen.smt.TranslatorToZ3;
@Priority(0)
public class BinaryExpression extends Expression implements IModel {
	Expression e1;
	BinaryOperator op;
	Expression e2;
	
	@Override
	public Expr eval(TranslatorToZ3 ctx) {
		return op.eval(ctx, e1, e2);
	}

	@Override
	public String toString() {
		String se1 = e1.toString();
		String sop = op.toString();
		String se2 = e2.toString();
		return String.format("%s %s %s", se1,sop,se2);
	}
	
	public Expression getFirstExpression() {
		return e1;
	}
	public Expression getSecondExpression() {
		return e2;
	}
	
	@Override
	public void substituteVariable(String from, String to) {
		e1.substituteVariable(from, to);
		e2.substituteVariable(from, to);
	}
	
	@Override
	public void getVariableNames(List<String> l) {
		e1.getVariableNames(l);
		e2.getVariableNames(l);
	}
	
}