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

}
