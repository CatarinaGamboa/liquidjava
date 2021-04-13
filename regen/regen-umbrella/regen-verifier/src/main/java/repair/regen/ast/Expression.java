package repair.regen.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;
import repair.regen.utils.Pair;

public abstract class Expression {

	public abstract Expr eval(TranslatorToZ3 ctx) throws Exception;
//	public abstract void substitute(String from, String to);
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
	 * Substitutes the expression first given expression by the second 
	 * @param from
	 * @param to
	 * @return
	 */
	public Expression substitute(Expression from, Expression to) {
		Expression e = clone();
		if(this.equals(from))
			e = to;
		e.auxSubstitute(from, to);
		return e;
	}

	private void auxSubstitute(Expression from, Expression to) {
		if(hasChildren()) {
			for (int i = 0; i < children.size(); i++) {
				Expression exp = children.get(i);
				if(exp.equals(from))
					setChild(i, to);
				exp.auxSubstitute(from, to);
			}
		}
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


	public Expression substituteState(Map<String, Expression> subMap, String[] toChange) {
		Expression e = clone();
		if(this instanceof FunctionInvocation) {
			FunctionInvocation fi = (FunctionInvocation)this;
			if(subMap.containsKey(fi.name) && fi.children.size() == 1
					&& fi.children.get(0) instanceof Var) {//object state
				Var v = (Var)fi.children.get(0);
				Expression sub = subMap.get(fi.name).clone();
				for(String s: toChange) {
					sub = sub.substitute(new Var(s),v);
				}
				//substitute by sub in parent
				e = new GroupExpression(sub);
			}
		}
		e.auxSubstituteState(subMap, toChange);
		return e; 
	}

	private void auxSubstituteState(Map<String, Expression> subMap, String[] toChange) {
		if(hasChildren()) {
			for (int i = 0; i < children.size(); i++) {
				Expression exp = children.get(i);
				if(exp instanceof FunctionInvocation) {
					FunctionInvocation fi = (FunctionInvocation) exp;
					if(subMap.containsKey(fi.name) && fi.children.size() == 1
							&& fi.children.get(0) instanceof Var) {//object state
						Var v = (Var)fi.children.get(0);
						Expression sub = subMap.get(fi.name).clone();
						for(String s: toChange) {
							sub = sub.substitute(new Var(s), v);
						}
						//substitute by sub in parent
						setChild(i, new GroupExpression(sub));
					}

				}
				exp.auxSubstituteState(subMap, toChange);
			}
		}

	}
	public Expression changeAlias(HashMap<String, Pair<Expression, List<Expression>>> mapAlias) {
		Expression e = clone();
		if(this instanceof AliasInvocation) {
			AliasInvocation ai = (AliasInvocation)this;
			if(mapAlias.containsKey(ai.name)) {//object state
				Expression sub = mapAlias.get(ai.name).getFirst().clone();
				List<Expression> vars = mapAlias.get(ai.name).getSecond();
				for (int i = 0; i < children.size(); i++) {
					sub = sub.substitute(vars.get(i).clone(), children.get(i));
				}
				e = new GroupExpression(sub);
			}
		}
		e.auxChangeAlias(mapAlias);
		return e; 
	}

	private void auxChangeAlias(HashMap<String, Pair<Expression, List<Expression>>> mapAlias) {
		if(hasChildren())
			for (int i = 0; i < children.size(); i++) {
				if(children.get(i) instanceof AliasInvocation) {
					AliasInvocation ai = (AliasInvocation)children.get(i);
					Expression sub = mapAlias.get(ai.name).getFirst().clone();
					List<Expression> vars = mapAlias.get(ai.name).getSecond();
					if(ai.hasChildren())
						for (int j = 0; j< ai.children.size(); j++) {
							sub = sub.substitute(vars.get(j).clone(), ai.children.get(j));
						}
					setChild(i, sub);
				}
				children.get(i).auxChangeAlias(mapAlias);
			}
		
	}
}