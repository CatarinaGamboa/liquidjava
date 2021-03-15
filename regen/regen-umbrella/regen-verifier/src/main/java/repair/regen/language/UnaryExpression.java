package repair.regen.language;

import java.util.List;

import org.modelcc.IModel;

import com.microsoft.z3.Expr;

import repair.regen.language.operators.UnaryOperator;
import repair.regen.smt.TranslatorToZ3;

public class UnaryExpression extends Expression implements IModel {
	UnaryOperator op;
	Expression e;

	public UnaryExpression() {}
	public UnaryExpression(UnaryOperator u, Expression e) {
		op = u;
		this.e = e;
	}

	@Override
	public Expr eval(TranslatorToZ3 ctx) throws Exception {
		return op.eval(ctx, e);
	}
	
	@Override
	public String toString() {
		return op.toString()+e.toString();
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
	
	public void setExpression(Expression e2) {
		e = e2;
	}
	
}