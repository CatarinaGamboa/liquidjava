package repair.regen.ast;

import java.util.ArrayList;
import java.util.List;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public abstract class Expression {

	public abstract Expr eval(TranslatorToZ3 ctx) throws Exception;
	public abstract void substitute(String from, String to);
	public abstract void getVariableNames(List<String> toAdd);
	public abstract void getStateInvocations(List<String> toAdd, List<String> all);
	public abstract boolean isBooleanTrue();
	//	public abstract void changeOld(String s, Expression e); 
	//	public abstract void changeStateRefinement(String s, Expression e);
	//	public abstract void changeAlias(String s, Expression e);
	public abstract int hashCode();
	public abstract boolean equals(Object obj);
	public abstract Expression clone();
	public abstract String toString();



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

	public void setChild(int index, Expression element) {
		children.set(index, element);
	}

	/**
	 * Substitutes the function call with the given parameter to the expression e
	 * @param s
	 * @param e
	 */
	public void substituteFunction(String functionName, List<Expression> parameters, Expression sub) {
		if(hasChildren())
			for (int i = 0; i < children.size(); i++) {
				Expression exp = children.get(i);
				if(exp instanceof FunctionInvocation) {
					FunctionInvocation fi = (FunctionInvocation) exp;
					if(fi.name.equals(functionName) && fi.argumentsEqual(parameters)) {
						//substitute by sub in parent
						setChild(i, sub);
					}

				}
				exp.substituteFunction(functionName, parameters, sub);
			}
	}


}
