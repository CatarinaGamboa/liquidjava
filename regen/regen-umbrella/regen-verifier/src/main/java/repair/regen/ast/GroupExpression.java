package repair.regen.ast;

import java.util.List;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public class GroupExpression extends Expression {
	private Expression e;
	public GroupExpression(Expression e) {
		this.e = e;
		addChild(e);
	}
	
	public void setChild(int index, Expression element) {
		super.setChild(index, element);
		e = element;
	}

	@Override
	public Expr eval(TranslatorToZ3 ctx) throws Exception {
		return e.eval(ctx);
	}
	
	public String toString() {
		return "("+e.toString()+")";
	}

	@Override
	public void substitute(String from, String to) {
		e.substitute(from, to);		
	}

	@Override
	public void getVariableNames(List<String> toAdd) {
		e.getVariableNames(toAdd);
	}

	@Override
	public void getStateInvocations(List<String> toAdd, List<String> all) {
		e.getStateInvocations(toAdd, all);
	}

	@Override
	public Expression clone() {
		return new GroupExpression(e.clone());
	}
	
	@Override
	public boolean isBooleanTrue() {
		return e.isBooleanTrue();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((e == null) ? 0 : e.hashCode());
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
		GroupExpression other = (GroupExpression) obj;
		if (e == null) {
			if (other.e != null)
				return false;
		} else if (!e.equals(other.e))
			return false;
		return true;
	}
	

}
