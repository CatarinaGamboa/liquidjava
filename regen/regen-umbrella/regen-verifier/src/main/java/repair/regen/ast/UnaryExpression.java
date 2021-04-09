package repair.regen.ast;

import java.util.List;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public class UnaryExpression extends Expression{
	
	private String op;
	private Expression e;
	
	public UnaryExpression(String op, Expression e) {
		this.op = op;
		this.e = e;
		addChild(e);
	}
	
	public void setChild(int index, Expression element) {
		super.setChild(index, element);
		e = element;
	}
	
	@Override
	public Expr eval(TranslatorToZ3 ctx) throws Exception {
		switch(op) {
			case "-":
				return ctx.makeMinus(e.eval(ctx));
			case "!":
				return ctx.mkNot(e.eval(ctx));
		}
		return null;
	}

	@Override
	public String toString() {
		return op + e.toString();
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
		return new UnaryExpression(op, e.clone());
	}
	
	@Override
	public boolean isBooleanTrue() {
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((e == null) ? 0 : e.hashCode());
		result = prime * result + ((op == null) ? 0 : op.hashCode());
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
		UnaryExpression other = (UnaryExpression) obj;
		if (e == null) {
			if (other.e != null)
				return false;
		} else if (!e.equals(other.e))
			return false;
		if (op == null) {
			if (other.op != null)
				return false;
		} else if (!op.equals(other.op))
			return false;
		return true;
	}

}
