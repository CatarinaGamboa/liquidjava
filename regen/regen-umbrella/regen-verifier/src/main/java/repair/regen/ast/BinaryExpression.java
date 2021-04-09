package repair.regen.ast;

import static org.junit.Assert.fail;

import java.util.List;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public class BinaryExpression extends Expression{
	private Expression e1;
	private String op;
	private Expression e2;

	public BinaryExpression(Expression e1, String op, Expression e2) {
		this.e1 = e1;
		this.op = op;
		this.e2 = e2;
		addChild(e1);
		addChild(e2);
	}
	
	public void setChild(int index, Expression element) {
		super.setChild(index, element);
		if(index == 0)
			e1 = element;
		else
			e2 = element;
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
		case "-":
			return ctx.makeSub(e1, e2);
		case "*":
			return ctx.makeMul(e1, e2);
		case "/":
			return ctx.makeDiv(e1, e2);
		case "%":
			return ctx.makeMod(e1, e2);
		default: //last case %
			fail("Reached unkown operation "+ op);
			return null;
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
	public void getStateInvocations(List<String> toAdd, List<String> all) {
		e1.getStateInvocations(toAdd, all);
		e2.getStateInvocations(toAdd, all);
		System.out.println();
	}

	@Override
	public Expression clone() {
		return new BinaryExpression(e1.clone(), op, e2.clone());
	}
	
	@Override
	public boolean isBooleanTrue() {
		switch(op) {
		case "&&":
			return e1.isBooleanTrue() && e2.isBooleanTrue();
		case "||":
			return e1.isBooleanTrue() && e2.isBooleanTrue();
		case "-->":
			return e1.isBooleanTrue() && e2.isBooleanTrue();
		case "==":
			return e1.isBooleanTrue() && e2.isBooleanTrue();
		default:
			return false;
		}
		
	}

}
