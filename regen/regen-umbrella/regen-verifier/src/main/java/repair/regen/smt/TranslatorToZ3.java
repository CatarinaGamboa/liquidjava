package repair.regen.smt;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FPExpr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.IntNum;
import com.microsoft.z3.RealExpr;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Sort;
import com.microsoft.z3.Status;

import repair.regen.language.Expression;
import repair.regen.language.Variable;
import repair.regen.language.alias.Alias;
import repair.regen.language.alias.AliasName;
import repair.regen.processor.context.AliasWrapper;
import repair.regen.processor.context.GhostFunction;
import spoon.reflect.reference.CtTypeReference;

public class TranslatorToZ3 {

	private Context z3 = new Context();
	private Map<String, Expr> varTranslation = new HashMap<>();
	private Map<String, AliasWrapper> aliasTranslation = new HashMap<>();
	private Map<String, FuncDecl> funcTranslation = new HashMap<>();

	public TranslatorToZ3(Map<String, CtTypeReference<?>> ctx, List<GhostFunction> l, List<AliasWrapper> alias) {
		translateVariables(ctx);
		addBuiltinFunctions();
		if(!l.isEmpty()) {
			for(GhostFunction gh: l) {
				List<CtTypeReference<?>> paramTypes = gh.getParametersTypes();
				Sort ret = getSort(gh.getReturnType().toString());
				Sort[] d = paramTypes.stream()
						.map(t->t.toString())
						.map(t->getSort(t))
						.toArray(Sort[]::new);
				funcTranslation.put(gh.getName(), z3.mkFuncDecl(gh.getName(), d, ret));
			}
		}
		addAlias(alias);
	}

	private void addAlias(List<AliasWrapper> alias) {
		for(AliasWrapper a: alias) {
			aliasTranslation.put(a.getName(), a);
		}

	}

	private void addBuiltinFunctions() {
		funcTranslation.put("length", z3.mkFuncDecl("length", getSort("int[]"), getSort("int")));
		//TODO add built-in function
		Sort[] s = Arrays.asList(getSort("int[]"), getSort("int"), getSort("int")).stream().toArray(Sort[]::new);	
		funcTranslation.put("addToIndex", z3.mkFuncDecl("addToIndex", s, getSort("void")));

		s = Arrays.asList(getSort("int[]"), getSort("int")).stream().toArray(Sort[]::new);	
		funcTranslation.put("getFromIndex", z3.mkFuncDecl("getFromIndex", s, getSort("int")));

	}

	public void translateVariables(Map<String, CtTypeReference<?>> ctx) {
		for (String name : ctx.keySet()) {
			if (ctx.get(name).getQualifiedName().contentEquals("int"))
				varTranslation.put(name, z3.mkIntConst(name));
			else if (ctx.get(name).getQualifiedName().contentEquals("short")) 
				varTranslation.put(name, z3.mkIntConst(name));
			else if (ctx.get(name).getQualifiedName().contentEquals("boolean")) 
				varTranslation.put(name, z3.mkBoolConst(name));
			else if (ctx.get(name).getQualifiedName().contentEquals("long"))
				varTranslation.put(name, z3.mkRealConst(name));
			else if (ctx.get(name).getQualifiedName().contentEquals("float")) {
				FPExpr k = (FPExpr)z3.mkConst(name, z3.mkFPSort64());
				varTranslation.put(name, k);
			}else if (ctx.get(name).getQualifiedName().contentEquals("double")) {
				FPExpr k = (FPExpr)z3.mkConst(name, z3.mkFPSort64());
				varTranslation.put(name, k);
			}else if (ctx.get(name).getQualifiedName().contentEquals("int[]")) {
				varTranslation.put(name, 
						z3.mkArrayConst(name, z3.mkIntSort(), z3.mkIntSort()));	
			}else {
				System.out.println(name + ":"+ctx.get(name).getQualifiedName());
				//TODO ADD OTHER TYPES
				System.out.println("Not implemented yet!");
			}
		}
		varTranslation.put("true", z3.mkBool(true));
		varTranslation.put("false", z3.mkBool(false));

	}

	private Sort getSort(String sort) {
		switch(sort) {
		case "int": return z3.getIntSort();
		case "boolean":return z3.getBoolSort();
		case "long":return z3.getRealSort();
		case "float": return z3.mkFPSort32();
		case "double":return z3.mkFPSortDouble();
		case "int[]": return z3.mkArraySort(z3.mkIntSort(), z3.mkIntSort());
		case "String":return z3.getStringSort();
		case "void": return z3.mkUninterpretedSort("void");
		//case "List":return z3.mkListSort(name, elemSort)
		default:
			return null;
		}	
	}

	public Status verifyExpression(Expression e) throws Exception {
		Solver s = z3.mkSolver();
		s.add((BoolExpr) e.eval(this));
		Status st = s.check();
		if (st.equals(Status.SATISFIABLE)) {
			// Example of values
			// System.out.println(s.getModel());
		}
		return st;
	}

	//#####################Literals and Variables#####################
	public Expr makeIntegerLiteral(int value) {
		return z3.mkInt(value);
	}

	public Expr makeLongLiteral(long value) {
		return z3.mkReal(value);
	}

	public Expr makeDoubleLiteral(double value) {
		return z3.mkFP(value, z3.mkFPSort64());
	}


	public Expr makeBooleanLiteral(boolean value) {
		return z3.mkBool(value);
	}

	public Expr makeVariable(String name) {
		return varTranslation.get(name);//int[] not in varTranslation
	}

	public Expr makeFunctionInvocation(String name, Expr[] params) {
		if(name.equals("addToIndex"))
			return makeStore(name, params);
		if(name.equals("getFromIndex"))
			return makeSelect(name, params);

		FuncDecl fd = funcTranslation.get(name);
		return z3.mkApp(fd, params);
	}


	private Expr makeSelect(String name, Expr[] params) {
		if(params.length == 2 && params[0] instanceof ArrayExpr)
			return z3.mkSelect((ArrayExpr)params[0], params[1]);
		return null;
	}

	private Expr makeStore(String name, Expr[] params) {
		if(params.length == 3 && params[0] instanceof ArrayExpr)
			return z3.mkStore((ArrayExpr) params[0], params[1], params[2]);
		return null;
	}

	//#####################Boolean Operations#####################
	public Expr makeEquals(Expr e1, Expr e2) {
		if(e1 instanceof FPExpr || e2 instanceof FPExpr) 
			return z3.mkFPEq(toFP(e1), toFP(e2));

		return z3.mkEq(e1, e2);
	}



	public Expr makeLt(Expr e1, Expr e2) {
		if(e1 instanceof FPExpr || e2 instanceof FPExpr) 		
			return z3.mkFPLt(toFP(e1), toFP(e2));

		return z3.mkLt((ArithExpr) e1,(ArithExpr) e2);
	}

	public Expr makeLtEq(Expr e1, Expr e2) {
		if(e1 instanceof FPExpr || e2 instanceof FPExpr)
			return z3.mkFPLEq(toFP(e1), toFP(e2));

		return z3.mkLe((ArithExpr) e1,(ArithExpr) e2);
	}

	public Expr makeGt(Expr e1, Expr e2) {
		if(e1 instanceof FPExpr || e2 instanceof FPExpr) 
			return z3.mkFPGt(toFP(e1), toFP(e2));

		return z3.mkGt((ArithExpr) e1,(ArithExpr) e2);		
	}

	public Expr makeGtEq(Expr e1, Expr e2) {
		if(e1 instanceof FPExpr || e2 instanceof FPExpr) 
			return z3.mkFPGEq(toFP(e1), toFP(e2));

		return z3.mkGe((ArithExpr) e1,(ArithExpr) e2);
	}

	public Expr makeImplies(Expr e1, Expr e2) {
		return z3.mkImplies((BoolExpr) e1,(BoolExpr) e2);
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

	//	public Expr makeIf(Expr eval, Expr eval2) {
	//		z3.mkI
	//		return z3.mkOr((BoolExpr) eval, (BoolExpr) eval2);
	//	}

	//##################### Unary Operations #####################
	public Expr makeMinus(Expr eval) {
		if(eval instanceof FPExpr) 
			return z3.mkFPNeg((FPExpr) eval);
		return z3.mkUnaryMinus((ArithExpr) eval);
	}

	//#####################Arithmetic Operations#####################
	public Expr makeAdd(Expr eval, Expr eval2) {
		if(eval instanceof FPExpr || eval2 instanceof FPExpr) 
			return z3.mkFPAdd(z3.mkFPRoundNearestTiesToEven(), toFP(eval), toFP(eval2));

		return z3.mkAdd((ArithExpr) eval, (ArithExpr) eval2);

	}

	public Expr makeSub(Expr eval, Expr eval2) {
		if(eval instanceof FPExpr || eval2 instanceof FPExpr) 
			return z3.mkFPSub(z3.mkFPRoundNearestTiesToEven(), toFP(eval), toFP(eval2));

		return z3.mkSub((ArithExpr) eval, (ArithExpr) eval2);
	}

	public Expr makeMul(Expr eval, Expr eval2) {
		if(eval instanceof FPExpr || eval2 instanceof FPExpr) 
			return z3.mkFPMul(z3.mkFPRoundNearestTiesToEven(), toFP(eval), toFP(eval2));


		return z3.mkMul((ArithExpr) eval, (ArithExpr) eval2);
	}

	public Expr makeDiv(Expr eval, Expr eval2) {
		if(eval instanceof FPExpr || eval2 instanceof FPExpr)
			return z3.mkFPDiv(z3.mkFPRoundNearestTiesToEven(), toFP(eval), toFP(eval2));

		return z3.mkDiv((ArithExpr) eval, (ArithExpr) eval2);
	}

	public Expr makeMod(Expr eval, Expr eval2) {
		if(eval instanceof FPExpr || eval2 instanceof FPExpr)
			return z3.mkFPRem(toFP(eval), toFP(eval2));
		return z3.mkMod((IntExpr) eval, (IntExpr) eval2);
	}

	private FPExpr toFP(Expr e) {
		FPExpr f;
		if(e instanceof FPExpr) {
			f = (FPExpr) e;
		}else if(e instanceof IntNum) 
			f = z3.mkFP(((IntNum) e).getInt(), z3.mkFPSort64());
		else if(e instanceof IntExpr) {
			IntExpr ee= (IntExpr) e;
			RealExpr re = z3.mkInt2Real(ee);
			f = z3.mkFPToFP(z3.mkFPRoundNearestTiesToEven(), re, z3.mkFPSort64());
		}else if(e instanceof RealExpr) {
			f = z3.mkFPToFP(z3.mkFPRoundNearestTiesToEven(), (RealExpr)e, z3.mkFPSort64());
		}else {
			f = null;
			System.out.println("Not implemented!!");
		}
		return f;
	}


	public Expr makeIte(Expr c, Expr t, Expr e) {
		if(c instanceof BoolExpr)
			return z3.mkITE((BoolExpr)c, t, e);
		System.out.println("Condition is not a boolean expression");
		return null;
	}



	public Expression makeAlias(AliasName name, Variable var) throws TypeMismatchError {
		AliasWrapper al = aliasTranslation.get(name.toString());
		Expression e = al.getClonedConstraint().getExpression();//cloning
		String nVarName = var.getName();

		//check type
		Expr varE = varTranslation.get(var.getName());
		Sort varSort = varE.getSort();
		if(varSort.equals(getSort(al.getType().toString()))) {
			e.substituteVariable(al.getVarName(), nVarName);
			System.out.println("Make Alias:" + e.toString());
			return e;
		}
		else {
			throw new TypeMismatchError("Type mismatch in alias usage: using "+varSort.toString()
			+" expecting "+al.getType().toString());
			
		}


	}
}
