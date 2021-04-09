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
	

}
