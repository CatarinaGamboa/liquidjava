package repair.regen.ast;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public class LiteralString extends Expression{
	private String value;
	
	public LiteralString(String v) {
		value = v;
	}
	@Override
	public Expr eval(TranslatorToZ3 ctx) {
		return ctx.makeString(value);
	}
	
	public String toString() {
		return value;
	}

}
