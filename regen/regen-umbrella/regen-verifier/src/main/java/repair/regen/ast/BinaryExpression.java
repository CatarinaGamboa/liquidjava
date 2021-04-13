package repair.regen.ast;

import static org.junit.Assert.fail;

import java.util.List;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public class BinaryExpression extends Expression{
	
	private String op;

	public BinaryExpression(Expression e1, String op, Expression e2) {
		this.op = op;
		addChild(e1);
		addChild(e2);
	}
	
	public Expression getFirstOperand() {
		return children.get(0);
	}
	
	public Expression getSecondOperand() {
		return children.get(1);
	}
		
	
	@Override
	public Expr eval(TranslatorToZ3 ctx) throws Exception {
		Expr ee1 = getFirstOperand().eval(ctx);
		Expr ee2 = getSecondOperand().eval(ctx);
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
		return getFirstOperand().toString() + " " + op + " " + getSecondOperand().toString();
	}

	@Override
	public void getVariableNames(List<String> toAdd) {
		getFirstOperand().getVariableNames(toAdd);
		getSecondOperand().getVariableNames(toAdd);
	}

	@Override
	public void getStateInvocations(List<String> toAdd, List<String> all) {
		getFirstOperand().getStateInvocations(toAdd, all);
		getSecondOperand().getStateInvocations(toAdd, all);
		System.out.println();
	}

	@Override
	public Expression clone() {
		return new BinaryExpression(getFirstOperand().clone(), op, getSecondOperand().clone());
	}
	
	@Override
	public boolean isBooleanTrue() {
		switch(op) {
		case "&&":
			return getFirstOperand().isBooleanTrue() && getSecondOperand().isBooleanTrue();
		case "||":
			return getFirstOperand().isBooleanTrue() && getSecondOperand().isBooleanTrue();
		case "-->":
			return getFirstOperand().isBooleanTrue() && getSecondOperand().isBooleanTrue();
		case "==":
			return getFirstOperand().isBooleanTrue() && getSecondOperand().isBooleanTrue();
		default:
			return false;
		}
		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getFirstOperand() == null) ? 0 : getFirstOperand().hashCode());
		result = prime * result + ((getSecondOperand() == null) ? 0 : getSecondOperand().hashCode());
		result = prime * result + ((op == null) ? 0 : op.hashCode());
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
		BinaryExpression other = (BinaryExpression) obj;
		if (getFirstOperand() == null) {
			if (other.getFirstOperand() != null)
				return false;
		} else if (!getFirstOperand().equals(other.getFirstOperand()))
			return false;
		if (getSecondOperand() == null) {
			if (other.getSecondOperand() != null)
				return false;
		} else if (!getSecondOperand().equals(other.getSecondOperand()))
			return false;
		if (op == null) {
			if (other.op != null)
				return false;
		} else if (!op.equals(other.op))
			return false;
		return true;
	}


}
