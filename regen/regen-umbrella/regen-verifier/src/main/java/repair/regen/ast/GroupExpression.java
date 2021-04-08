package repair.regen.ast;

import java.util.List;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public class GroupExpression extends Expression {
	private Expression e;
	public GroupExpression(Expression e) {
		this.e = e;
		addChild(e);
	}

	@Override
	public Expr eval(TranslatorToZ3 ctx) throws Exception {
		return e.eval(ctx);
	}
	
	public String toString() {
		return "("+e.toString()+")";
	}

	@Override
	public void substitute(String from, String to) {
		e.substitute(from, to);		
	}

	@Override
	public void getVariableNames(List<String> toAdd) {
		e.getVariableNames(toAdd);
	}

	@Override
	public void getGhostInvocations(List<String> toAdd) {
		e.getGhostInvocations(toAdd);
	}

	@Override
	public Expression clone() {
		return new GroupExpression(e.clone());
	}
	
	@Override
	public boolean isBooleanTrue() {
		return e.isBooleanTrue();
	}
	

}
