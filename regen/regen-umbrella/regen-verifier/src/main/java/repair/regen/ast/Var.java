package repair.regen.ast;

import java.util.List;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public class Var extends Expression{
	
	private String name;
	
	public Var(String name) {
		this.name = name;
	}

	@Override
	public Expr eval(TranslatorToZ3 ctx) throws Exception {
		return ctx.makeVariable(name);
	}
	
	public String toString() {
		return name;
	}

	@Override
	public void substitute(String from, String to) {
		if(name.equals(from))
			name = to;
	}

	@Override
	public void getVariableNames(List<String> toAdd) {
		if(!toAdd.contains(name))
			toAdd.add(name);
	}

	@Override
	public void getGhostInvocations(List<String> toAdd) {
		//end leaf
	}

	@Override
	public Expression clone() {
		return new Var(name);
	}
	
	@Override
	public boolean isBooleanTrue() {
		return false;
	}

}
