package repair.regen.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public class AliasInvocation extends Expression{
	String name;
	List<Expression> args;
	
	public AliasInvocation(String name, List<Expression> args) {
		this.name = name;
		this.args = args;
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
	public void getGhostInvocations(List<String> toAdd) {
		for(Expression e: args)
			e.getGhostInvocations(toAdd);
		
	}

	@Override
	public Expression clone() {
		List<Expression> le = new ArrayList<>();
		for(Expression e: args)
			le.add(e.clone());
		return new AliasInvocation(name, le);
	}
}
