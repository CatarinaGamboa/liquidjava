package repair.regen.ast;

import java.util.List;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public class LiteralBoolean extends Expression{
	
	boolean value;
	
	public LiteralBoolean(boolean value) {
		this.value = value;
	}

	public LiteralBoolean(String value) {
		this.value = Boolean.parseBoolean(value);
	}
	
	public Expr eval(TranslatorToZ3 ctx) {
		return ctx.makeBooleanLiteral(value);
	}
	
	public String toString() {
		return Boolean.toString(value);
	}

	@Override
	public void substitute(String from, String to) {
		//end leaf
	}

	@Override
	public void getVariableNames(List<String> toAdd) {
		// end leaf
	}

	@Override
	public void getGhostInvocations(List<String> toAdd) {
		// end leaf
		
	}

	@Override
	public Expression clone() {
		return new LiteralBoolean(value);
	}
	@Override
	public boolean isBooleanTrue() {
		return value;
	}

}
