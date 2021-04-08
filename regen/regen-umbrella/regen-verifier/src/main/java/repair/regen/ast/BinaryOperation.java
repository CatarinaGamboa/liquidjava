package repair.regen.ast;

import java.util.List;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public class BinaryOperation extends Expression{
	private Expression e1;
	private String op;
	private Expression e2;

	public BinaryOperation(Expression e1, String op, Expression e2) {
		this.e1 = e1;
		this.op = op;
		this.e2 = e2;
	}
	
	@Override
	public Expr eval(TranslatorToZ3 ctx) throws Exception {
		Expr ee1 = e1.eval(ctx);
		Expr ee2 = e2.eval(ctx);
		return evalBinaryOp(ctx, ee1, ee2);
	}

	private Expr evalBinaryOp(TranslatorToZ3 ctx, Expr e1, Expr e2) {
		switch (op) {
		case "&&":
			return ctx.makeAnd(e1, e2);
		case "||":
			return ctx.makeOr(e1,e2);
		case "-->":
			return ctx.makeImplies(e1, e2);
		case "==":
			return ctx.makeEquals(e1, e2);
		case "!=":
			return ctx.mkNot(ctx.makeEquals(e1, e2));
		case ">=":
			return ctx.makeGtEq(e1, e2);
		case ">":
			return ctx.makeGt(e1, e2);
		case "<=":
			return ctx.makeLtEq(e1, e2);
		case "<": 
			return ctx.makeLt(e1, e2);
		case "+":
			return ctx.makeAdd(e1, e2);
		case "*":
			return ctx.makeMul(e1, e2);
		case "/":
			return ctx.makeDiv(e1, e2);
		default: //last case %
			return ctx.makeMod(e1, e2);
		}
	}

	@Override
	public String toString() {
		return e1.toString() + " " + op + " " + e2.toString();
	}

	@Override
	public void substitute(String from, String to) {
		e1.substitute(from, to);
		e2.substitute(from, to);
	}

	@Override
	public void getVariableNames(List<String> toAdd) {
		e1.getVariableNames(toAdd);
		e2.getVariableNames(toAdd);
	}

	@Override
	public void getGhostInvocations(List<String> toAdd) {
		e1.getGhostInvocations(toAdd);
		e2.getGhostInvocations(toAdd);
	}

	@Override
	public Expression clone() {
		return new BinaryOperation(e1.clone(), op, e2.clone());
	}

}
