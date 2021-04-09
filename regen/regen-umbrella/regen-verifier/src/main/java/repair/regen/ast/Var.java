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
	public void getStateInvocations(List<String> toAdd, List<String> all) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Var other = (Var) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	

}
