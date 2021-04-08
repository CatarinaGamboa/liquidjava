package repair.regen.ast;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public class GroupExpression extends Expression {
	private Expression e;
	public GroupExpression(Expression e) {
		this.e = e;
	}

	@Override
	public Expr eval(TranslatorToZ3 ctx) throws Exception {
		return e.eval(ctx);
	}
	
	public String toString() {
		return e.toString();
	}
	

}
