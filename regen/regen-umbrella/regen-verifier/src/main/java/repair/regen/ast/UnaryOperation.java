package repair.regen.ast;

import java.util.List;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public class UnaryOperation extends Expression{
	
	private String op;
	private Expression e;
	
	public UnaryOperation(String op, Expression e) {
		this.op = op;
		this.e = e;
		addChild(e);
	}
	
	@Override
	public Expr eval(TranslatorToZ3 ctx) throws Exception {
		switch(op) {
			case "-":
				return ctx.makeMinus(e.eval(ctx));
			case "!":
				return ctx.mkNot(e.eval(ctx));
		}
		return null;
	}

	@Override
	public String toString() {
		return op + e.toString();
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
		return new UnaryOperation(op, e.clone());
	}

}
