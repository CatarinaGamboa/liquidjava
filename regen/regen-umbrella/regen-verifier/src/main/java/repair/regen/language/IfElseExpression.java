package repair.regen.language;

import java.util.List;

import org.modelcc.IModel;

import com.microsoft.z3.Expr;

import repair.regen.language.operators.BinaryOperator;
import repair.regen.language.operators.IfElseOperator;
import repair.regen.language.operators.IfThenOperator;
import repair.regen.smt.TranslatorToZ3;

public class IfElseExpression  extends Expression implements IModel {
	Expression cond;
	IfThenOperator it;
	Expression then;
	IfElseOperator ie;
	Expression els;
	

	@Override
	public Expr eval(TranslatorToZ3 ctx) throws Exception {
		return ctx.makeIte(cond.eval(ctx), then.eval(ctx), els.eval(ctx));
	}
	
	@Override
	public Expr beforeEval(TranslatorToZ3 ctx) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Expression getCondition() {
		return cond;
	}
	public Expression getThenExpression() {
		return then;
	}
	public Expression getElseExpression() {
		return els;
	}
	
	@Override
	public void substituteVariable(String from, String to) {
		//go inside all expressions
		cond.substituteVariable(from, to);
		then.substituteVariable(from, to);
		els.substituteVariable(from, to);
	}
	
	@Override
	public void getVariableNames(List<String> l) {
		cond.getVariableNames(l);
		then.getVariableNames(l);
		els.getVariableNames(l);
	}
	

	@Override
	public String toString() {
		return String.format("%s %s %s %s %s", cond.toString(), it.toString(), 
				then.toString(), ie.toString(), els.toString());
	}

}
