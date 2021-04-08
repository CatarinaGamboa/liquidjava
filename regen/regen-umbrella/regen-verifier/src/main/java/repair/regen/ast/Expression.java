package repair.regen.ast;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public abstract class Expression {
	
	public abstract Expr eval(TranslatorToZ3 ctx) throws Exception;
	
	public abstract String toString();

}
