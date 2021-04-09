package repair.regen.ast;

import java.util.List;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public class Ite extends Expression{
	private Expression cond;
	private Expression then;
	private Expression els;
	
	public Ite(Expression e1, Expression e2, Expression e3) {
		cond = e1;
		then = e2;
		els = e3;
		addChild(cond);
		addChild(then);
		addChild(els);
	}
	
	public void setChild(int index, Expression element) {
		super.setChild(index, element);
		if(index == 0) cond = element;
		else if(index == 1) then = element;
		else els = element;
	}

	@Override
	public Expr eval(TranslatorToZ3 ctx) throws Exception {
		return ctx.makeIte(cond.eval(ctx), then.eval(ctx), els.eval(ctx));
	}

	@Override
	public String toString() {
		return cond.toString() +"?"+then.toString()+":"+els.toString();
	}

	@Override
	public void substitute(String from, String to) {
		cond.substitute(from, to);
		then.substitute(from, to);
		els.substitute(from, to);
	}

	@Override
	public void getVariableNames(List<String> toAdd) {
		cond.getVariableNames(toAdd);
		then.getVariableNames(toAdd);
		els.getVariableNames(toAdd);
	}

	@Override
	public void getStateInvocations(List<String> toAdd, List<String> all) {
		cond.getStateInvocations(toAdd, all);
		then.getStateInvocations(toAdd, all);
		els.getStateInvocations(toAdd, all);
	}

	@Override
	public Expression clone() {
		return new Ite(cond.clone(), then.clone(), els.clone());
	}
	
	@Override
	public boolean isBooleanTrue() {
		return cond.isBooleanTrue() && then.isBooleanTrue() 
				&& els.isBooleanTrue();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cond == null) ? 0 : cond.hashCode());
		result = prime * result + ((els == null) ? 0 : els.hashCode());
		result = prime * result + ((then == null) ? 0 : then.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ite other = (Ite) obj;
		if (cond == null) {
			if (other.cond != null)
				return false;
		} else if (!cond.equals(other.cond))
			return false;
		if (els == null) {
			if (other.els != null)
				return false;
		} else if (!els.equals(other.els))
			return false;
		if (then == null) {
			if (other.then != null)
				return false;
		} else if (!then.equals(other.then))
			return false;
		return true;
	}
	

}
