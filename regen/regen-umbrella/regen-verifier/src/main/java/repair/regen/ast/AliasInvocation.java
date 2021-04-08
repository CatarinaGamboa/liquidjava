package repair.regen.ast;

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
}
