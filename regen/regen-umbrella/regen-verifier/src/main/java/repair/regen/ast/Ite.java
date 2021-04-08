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
	public void getGhostInvocations(List<String> toAdd) {
		cond.getGhostInvocations(toAdd);
		then.getGhostInvocations(toAdd);
		els.getGhostInvocations(toAdd);
	}

	@Override
	public Expression clone() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
