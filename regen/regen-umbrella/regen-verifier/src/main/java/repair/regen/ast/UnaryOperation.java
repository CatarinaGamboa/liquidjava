package repair.regen.ast;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public class UnaryOperation extends Expression{
	
	private String op;
	private Expression e;
	
	public UnaryOperation(String op, Expression e) {
		this.op = op;
		this.e = e;
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

}
