package repair.regen.ast;

import java.util.List;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public class LiteralReal extends Expression{
	
	private double value;
	
	public LiteralReal(double v) {
		value = v;
	}
	
	public LiteralReal(String v) {
		value = Double.parseDouble(v);
	}

	@Override
	public Expr eval(TranslatorToZ3 ctx) {
		return ctx.makeDoubleLiteral(value);
	}
	
	public String toString() {
		return Double.toString(value);
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
	public void getGhostInvocations(List<String> toAdd) {
		//end leaf
		
	}

	@Override
	public Expression clone() {
		return new LiteralReal(value);
	}
	
	@Override
	public boolean isBooleanTrue() {
		return false;
	}
}
