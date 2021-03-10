package repair.regen.smt;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FPExpr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.Sort;

import repair.regen.processor.context.AliasWrapper;
import repair.regen.processor.context.GhostFunction;
import spoon.reflect.reference.CtTypeReference;

public class TranslatorContextToZ3 {

	static void translateVariables(Context z3, Map<String, CtTypeReference<?>> ctx,
			Map<String, Expr> varTranslation) {
		for (String name : ctx.keySet()) {
			String typeName = ctx.get(name).getQualifiedName();
			if (typeName.contentEquals("int"))
				varTranslation.put(name, z3.mkIntConst(name));
			else if (typeName.contentEquals("short")) 
				varTranslation.put(name, z3.mkIntConst(name));
			else if (typeName.contentEquals("boolean")) 
				varTranslation.put(name, z3.mkBoolConst(name));
			else if (typeName.contentEquals("long"))
				varTranslation.put(name, z3.mkRealConst(name));
			else if (typeName.contentEquals("float")) {
				FPExpr k = (FPExpr)z3.mkConst(name, z3.mkFPSort64());
				varTranslation.put(name, k);
			}else if (typeName.contentEquals("double")) {
				FPExpr k = (FPExpr)z3.mkConst(name, z3.mkFPSort64());
				varTranslation.put(name, k);
			}else if (typeName.contentEquals("int[]")) {
				varTranslation.put(name, 
						z3.mkArrayConst(name, z3.mkIntSort(), z3.mkIntSort()));	
			}else {
				Sort nSort = z3.mkUninterpretedSort(typeName);
				varTranslation.put(name, z3.mkConst(name, nSort));	
				//					System.out.println("Add new type: "+typeName);
			}

			varTranslation.put("true", z3.mkBool(true));
			varTranslation.put("false", z3.mkBool(false));
		}


	}

	static void addAlias(Context z3, List<AliasWrapper> alias, Map<String, AliasWrapper> aliasTranslation) {
		for(AliasWrapper a: alias) {
			aliasTranslation.put(a.getName(), a);
		}
	}

	public static void addGhostFunctions(Context z3, List<GhostFunction> ghosts,
			Map<String, FuncDecl> funcTranslation) {
		addBuiltinFunctions(z3, funcTranslation);
		if(!ghosts.isEmpty()) {
			for(GhostFunction gh: ghosts) {
				List<CtTypeReference<?>> paramTypes = gh.getParametersTypes();
				Sort ret = getSort(z3, gh.getReturnType().toString());
				Sort[] d = paramTypes.stream()
						.map(t->t.toString())
						.map(t->getSort(z3,t))
						.toArray(Sort[]::new);
				funcTranslation.put(gh.getName(), z3.mkFuncDecl(gh.getName(), d, ret));
			}
		}
	}
	


	private static void addBuiltinFunctions(Context z3, Map<String, FuncDecl> funcTranslation) {
		funcTranslation.put("length", z3.mkFuncDecl("length", getSort(z3,"int[]"), getSort(z3,"int")));//ERRRRRRRRRRRRO!!!!!!!!!!!!!
		System.out.println("Error only working for int[] now - Change");
		//TODO add built-in function
		Sort[] s = Arrays.asList(getSort(z3, "int[]"), getSort(z3,"int"), getSort(z3,"int")).stream().toArray(Sort[]::new);	
		funcTranslation.put("addToIndex", z3.mkFuncDecl("addToIndex", s, getSort(z3,"void")));

		s = Arrays.asList(getSort(z3,"int[]"), getSort(z3,"int")).stream().toArray(Sort[]::new);	
		funcTranslation.put("getFromIndex", z3.mkFuncDecl("getFromIndex", s, getSort(z3,"int")));

	}
	
	
	static Sort getSort(Context z3, String sort) {
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
			return z3.mkUninterpretedSort(sort);
		}	
	}

}
