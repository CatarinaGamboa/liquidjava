package repair.regen.ast;

import java.util.List;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public abstract class Expression {
	
	public abstract Expr eval(TranslatorToZ3 ctx) throws Exception;
	public abstract void substitute(String from, String to);
	public abstract void getVariableNames(List<String> toAdd);
	public abstract void getGhostInvocations(List<String> toAdd);
	public abstract Expression clone();
	//	public abstract void changeOld(String s, Expression e); 
	//	public abstract void changeStateRefinement(String s, Expression e);
	//	public abstract void changeAlias(String s, Expression e);
	public abstract String toString();
	
	

}
