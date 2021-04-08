package repair.regen.ast;

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
	
	

}
