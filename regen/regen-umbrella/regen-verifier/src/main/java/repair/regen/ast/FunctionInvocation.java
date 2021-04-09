package repair.regen.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public class FunctionInvocation extends Expression{
	String name;
	List<Expression> args;
	
	public FunctionInvocation(String name, List<Expression> args) {
		this.name = name;
		this.args = args;
		for(Expression e: args)
			addChild(e);
	}
	
	public void setChild(int index, Expression element) {
		super.setChild(index, element);
		args.set(index, element);
	}
	

	@Override
	public Expr eval(TranslatorToZ3 ctx) throws Exception {
		Expr[] argsExpr  = new Expr[args.size()];
		for (int i = 0; i < argsExpr.length; i++) {
			argsExpr[i] =  args.get(i).eval(ctx);
		}
		return ctx.makeFunctionInvocation(name, argsExpr);
	}

	@Override
	public String toString() {
		return name+"("+args.stream().map(p->p.toString()).collect(Collectors.joining(","))+")";
	}

	@Override
	public void substitute(String from, String to) {
		for(Expression e: args)
			e.substitute(from, to);
	}

	@Override
	public void getVariableNames(List<String> toAdd) {
		for(Expression e: args)
			e.getVariableNames(toAdd);
		
	}

	@Override
	public void getStateInvocations(List<String> toAdd, List<String> all) {
		if(!toAdd.contains(name) && all.contains(name))
			toAdd.add(name);
		for(Expression e: args)
			e.getStateInvocations(toAdd, all);
	}

	@Override
	public Expression clone() {
		List<Expression> le = new ArrayList<>();
		for(Expression e: args)
			le.add(e.clone());
		return new FunctionInvocation(name, le);
	}
	
	@Override
	public boolean isBooleanTrue() {
		return false;
	}

	public boolean argumentsEqual(List<Expression> parameters) {
		if(parameters.size() != args.size()) return false;
		for (int i = 0; i < args.size(); i++) {
			if(!parameters.get(i).equals(args.get(i)))
				return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((args == null) ? 0 : args.hashCode());
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
		FunctionInvocation other = (FunctionInvocation) obj;
		if (args == null) {
			if (other.args != null)
				return false;
		} else if (!args.equals(other.args))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
