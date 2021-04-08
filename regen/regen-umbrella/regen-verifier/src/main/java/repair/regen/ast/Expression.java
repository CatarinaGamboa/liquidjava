package repair.regen.ast;

import java.util.ArrayList;
import java.util.List;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public abstract class Expression {
	
	List<Expression> children = new ArrayList<>();
	public void addChild(Expression e) {
		children.add(e);
	}
	
	public List<Expression> getChildren(){
		return children;
	}
	
	public boolean hasChildren() {
		return children.size() > 0;
	}
	
	public abstract Expr eval(TranslatorToZ3 ctx) throws Exception;
	public abstract void substitute(String from, String to);
	public abstract void getVariableNames(List<String> toAdd);
	public abstract void getGhostInvocations(List<String> toAdd);
	public abstract boolean isBooleanTrue();
	//	public abstract void changeOld(String s, Expression e); 
	//	public abstract void changeStateRefinement(String s, Expression e);
	//	public abstract void changeAlias(String s, Expression e);
	public abstract Expression clone();
	public abstract String toString();
	
	

}
