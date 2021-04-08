package repair.regen.ast;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public class LiteralInt extends Expression{
	
	private int value;
	
	public LiteralInt(int v) {
		value = v;
	}
	
	public LiteralInt(String v) {
		value = Integer.parseInt(v);
	}

	@Override
	public Expr eval(TranslatorToZ3 ctx) {
		return ctx.makeIntegerLiteral(value);
	}
	
	
	public String toString() {
		return Integer.toString(value);
	}
	

}
