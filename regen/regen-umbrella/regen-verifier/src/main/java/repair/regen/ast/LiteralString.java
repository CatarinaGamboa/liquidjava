package repair.regen.ast;

import java.util.List;

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
	@Override
	public void substitute(String from, String to) {
		//end leaf
		
	}
	@Override
	public void getVariableNames(List<String> toAdd) {
		//end leaf
		
	}
	@Override
	public void getStateInvocations(List<String> toAdd, List<String> all) {
		//end leaf
		
	}
	@Override
	public Expression clone() {
		return new LiteralString(value);
	}
	
	@Override
	public boolean isBooleanTrue() {
		return false;
	}

}
