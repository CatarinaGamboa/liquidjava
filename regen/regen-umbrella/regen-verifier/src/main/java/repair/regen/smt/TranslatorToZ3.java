package repair.regen.smt;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.BoolExpr;

import com.microsoft.z3.Expr;
import com.microsoft.z3.FPExpr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.FuncDecl.Parameter;
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
import repair.regen.language.parser.SyntaxException;
import repair.regen.processor.context.AliasWrapper;
import repair.regen.processor.context.GhostFunction;
import spoon.reflect.reference.CtTypeReference;

public class TranslatorToZ3 {

	private com.microsoft.z3.Context z3 = new com.microsoft.z3.Context();
	private Map<String, Expr> varTranslation = new HashMap<>();
	private Map<String, List<Expr>> varSuperTypes = new HashMap<>();
	private Map<String, AliasWrapper> aliasTranslation = new HashMap<>();
	private Map<String, FuncDecl> funcTranslation = new HashMap<>();
	private List<Expression> premisesToAdd = new ArrayList<>();

	public TranslatorToZ3(repair.regen.processor.context.Context c) {
		TranslatorContextToZ3.translateVariables(z3, c.getContext(), varTranslation);
		TranslatorContextToZ3.addAlias(z3, c.getAlias(), aliasTranslation);
		TranslatorContextToZ3.addGhostFunctions(z3, c.getGhosts(), funcTranslation);
	}


	public Status verifyExpression(Expression e) throws Exception {
		Solver s = z3.mkSolver();
		s.add((BoolExpr) e.eval(this));
		for(Expression ex: premisesToAdd)
			s.add((BoolExpr) ex.eval(this));
		
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
	
	
	private Expr getVariableTranslation(String name) throws Exception {
		Expr e= varTranslation.get(name);
		if(e == null)
			e = varTranslation.get(String.format("this#%s", name));
		if(e == null)
			throw new SyntaxException("Unknown variable:"+name);
		return e;
	}


	public Expr makeVariable(String name) throws Exception {
		return getVariableTranslation(name);//int[] not in varTranslation
	}

	public Expr makeFunctionInvocation(String name, Expr[] params) {
		if(name.equals("addToIndex"))
			return makeStore(name, params);
		if(name.equals("getFromIndex"))
			return makeSelect(name, params);

		FuncDecl fd = funcTranslation.get(name);
		Parameter[] p = fd.getParameters();
		Sort[] s  =fd.getDomain();
		for (int i = 0; i < s.length; i++) {
			Expr param = params[i];
			System.out.println("Sort:" + s[i]);
			System.out.println("Sort:" + param.getSort());
//			if(param.isConst() && param.toString())
//				System.out.println(param);
				
			
		}
		
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



	public Expression makeAlias(AliasName name, List<Expression> list) throws TypeMismatchError, Exception {
		AliasWrapper al = aliasTranslation.get(name.toString());
		if(al.getVarNames().size() != list.size())
			throw new TypeMismatchError("Arguments do not match: invocation size "+
		list.size()+", expected size:"+al.getVarNames().size());
		List<String> newNames = al.getNewVariables();
		TranslatorContextToZ3.translateVariables(z3, al.getTypes(newNames), varTranslation);
		
		checkTypes(list, al.getTypes());
		
		Expression e = al.getNewExpression(newNames);
		Expression add = al.getPremises(list, newNames);
		premisesToAdd.add(add);

//		System.out.println("Make Alias:" + e.toString());
		return e;
	}

	private void checkTypes(List<Expression> list, List<CtTypeReference<?>> types) throws Exception {
		for (int i = 0; i < list.size(); i++) {
			Sort se = (list.get(i).eval(this)).getSort();
			Sort st = TranslatorContextToZ3.getSort(z3,types.get(i).getQualifiedName());
			if(!se.equals(st))
				throw new TypeMismatchError("Types of arguments do not match. Got "+
						list.get(i)+":"+se.toString()+" but expected "+st.toString());
		}
		
	}

}
