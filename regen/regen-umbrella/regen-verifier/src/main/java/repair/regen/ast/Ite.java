package repair.regen.ast;

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
	}

	@Override
	public Expr eval(TranslatorToZ3 ctx) throws Exception {
		return ctx.makeIte(cond.eval(ctx), then.eval(ctx), els.eval(ctx));
	}

	@Override
	public String toString() {
		return cond.toString() +"?"+then.toString()+":"+els.toString();
	}
	
	

}
