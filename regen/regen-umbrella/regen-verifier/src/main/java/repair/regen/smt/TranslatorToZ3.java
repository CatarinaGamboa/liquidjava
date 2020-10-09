package repair.regen.smt;

import java.util.HashMap;
import java.util.Map;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;

import repair.regen.language.Expression;
import spoon.reflect.reference.CtTypeReference;

public class TranslatorToZ3 {
	
	private Context z3 = new Context();
	private Map<String, Expr> varTranslation = new HashMap<>();
	
	
	public TranslatorToZ3(Map<String, CtTypeReference<?>> ctx) {
		for (String name : ctx.keySet()) {
			if (ctx.get(name).getQualifiedName().contentEquals("int")) {
				varTranslation.put(name, z3.mkIntConst(name));
			} else {
				System.out.println("Not implemented yet!");
			}
		}
	}
	
	//Literals and Variables
	public Expr makeIntegerLiteral(int value) {
		return z3.mkInt(value);
	}
	
	public Expr makeBooleanLiteral(boolean value) {
		return z3.mkBool(value);
	}

	public Expr makeVariable(String name) {
		return varTranslation.get(name);
	}
	

	//Binary Operations
	public Expr makeEquals(Expr e1, Expr e2) {
		return z3.mkEq(e1, e2);
	}

	public Expr makeLt(Expr e1, Expr e2) {
		return z3.mkLt((ArithExpr) e1,(ArithExpr) e2);
	}
	
	public Expr makeLtEq(Expr e1, Expr e2) {
		return z3.mkLe((ArithExpr) e1,(ArithExpr) e2);
	}

	public Expr makeGt(Expr e1, Expr e2) {
		return z3.mkGt((ArithExpr) e1,(ArithExpr) e2);
	}
	
	public Expr makeGtEq(Expr e1, Expr e2) {
		return z3.mkGe((ArithExpr) e1,(ArithExpr) e2);
	}

	public Expr makeImplies(Expr e1, Expr e2) {
		return z3.mkImplies((BoolExpr) e1,(BoolExpr) e2);
	}

	public Status verifyExpression(Expression e) {
		Solver s = z3.mkSolver();
		s.add((BoolExpr) e.eval(this));
		Status st = s.check();
		if (st.equals(Status.SATISFIABLE)) {
			// Example of values
			// System.out.println(s.getModel());
		}
		return st;
	}

	public Expr makeAnd(Expr eval, Expr eval2) {
		return z3.mkAnd((BoolExpr) eval, (BoolExpr) eval2);
	}

	public Expr mkNot(Expr e1) {
		return z3.mkNot((BoolExpr) e1);
	}

	public Expr makeOr(Expr eval, Expr eval2) {
		return z3.mkOr((BoolExpr) eval, (BoolExpr) eval2);
	}
	
	//Arithmetic Operations
	public Expr makeAdd(Expr eval, Expr eval2) {
		return z3.mkAdd((ArithExpr) eval, (ArithExpr) eval2);
	}
	
	public Expr makeSub(Expr eval, Expr eval2) {
		return z3.mkSub((ArithExpr) eval, (ArithExpr) eval2);
	}
	
	public Expr makeMul(Expr eval, Expr eval2) {
		return z3.mkMul((ArithExpr) eval, (ArithExpr) eval2);
	}
	
	public Expr makeDiv(Expr eval, Expr eval2) {
		return z3.mkDiv((ArithExpr) eval, (ArithExpr) eval2);
	}
	
	public Expr makeMod(Expr eval, Expr eval2) {
		return z3.mkMod((IntExpr) eval, (IntExpr) eval2);
	}


}
