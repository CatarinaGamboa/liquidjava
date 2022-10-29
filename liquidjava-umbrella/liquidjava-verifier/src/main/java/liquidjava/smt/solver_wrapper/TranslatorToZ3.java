package liquidjava.smt.solver_wrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.microsoft.z3.*;

import liquidjava.processor.context.AliasWrapper;
import liquidjava.processor.context.GhostFunction;
import liquidjava.processor.context.GhostState;
import liquidjava.processor.context.RefinedVariable;
import liquidjava.smt.NotFoundError;
import spoon.reflect.reference.CtTypeReference;

import static liquidjava.smt.solver_wrapper.ExprWrapper.fromZ3;

public class TranslatorToZ3 extends SMTWrapper {

    private final com.microsoft.z3.Context z3 = new com.microsoft.z3.Context();

    public TranslatorToZ3(liquidjava.processor.context.Context c) {
        TranslatorContextToZ3.translateVariables(z3, c.getContext(), varTranslation);
        TranslatorContextToZ3.storeVariablesSubtypes(z3, c.getAllVariablesWithSupertypes(), varSuperTypes);
        TranslatorContextToZ3.addAlias(z3, c.getAlias(), aliasTranslation);
        TranslatorContextToZ3.addGhostFunctions(z3, c.getGhosts(), funcTranslation);
        TranslatorContextToZ3.addGhostStates(z3, c.getGhostState(), funcTranslation);
        System.out.println();
    }

    @SuppressWarnings("unchecked")
    public Status verifyExpression(ExprWrapper e) throws Exception {
        Solver s = z3.mkSolver();
        // s.add((BoolExpr) e.eval(this));
        // for(Expression ex: premisesToAdd)
        // s.add((BoolExpr) ex.eval(this));
        s.add((BoolExpr) e.toZ3());
        com.microsoft.z3.Status st = s.check();
        if (st.equals(com.microsoft.z3.Status.SATISFIABLE)) {
            // Example of values
            // System.out.println(s.getModel());
        }
        return Status.fromZ3(st);
    }

    // #####################Literals and Variables#####################
    public ExprWrapper makeIntegerLiteral(int value) {
        return ExprWrapper.fromZ3(z3.mkInt(value));
    }

    public ExprWrapper makeLongLiteral(long value) {
        return ExprWrapper.fromZ3(z3.mkReal(value));
    }

    public ExprWrapper makeDoubleLiteral(double value) {
        return ExprWrapper.fromZ3(z3.mkFP(value, z3.mkFPSort64()));
    }

    public ExprWrapper makeString(String s) {
        return ExprWrapper.fromZ3((z3.mkString(s)));
    }

    public ExprWrapper makeBooleanLiteral(boolean value) {
        return ExprWrapper.fromZ3((z3.mkBool(value)));
    }

    public ExprWrapper makeFunctionInvocation(String name, ExprWrapper[] params) throws Exception {
        if (name.equals("addToIndex"))
            return makeStore(name, params);
        if (name.equals("getFromIndex"))
            return makeSelect(name, params);

        if (!funcTranslation.containsKey(name))
            throw new NotFoundError("Function '" + name + "' not found");

        FuncDecl<?> fd = funcTranslation.get(name);
        Sort[] s = fd.getDomain();
        for (int i = 0; i < s.length; i++) {
            Expr<?> param = params[i].toZ3();
            if (!s[i].equals(param.getSort())) {
                // Look if the function type is a supertype of this
                List<ExprWrapper> le = varSuperTypes.get(param.toString().replace("|", ""));
                if (le != null)
                    for (ExprWrapper e : le)
                        if (e.toZ3().getSort().equals(s[i]))
                            params[i] = e;
            }
            // System.out.println("Expected sort"+s[i]+"; Final sort->" +params[i].toString() +":"+
            // params[i].getSort());
        }
        Expr<?>[] z3params = new Expr[params.length];
        for (int i = 0; i < params.length; ++i) {
            z3params[i] = params[i].toZ3();
        }

        return ExprWrapper.fromZ3(z3.mkApp(fd, z3params));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ExprWrapper makeSelect(String name, ExprWrapper[] params) {
        if (params.length == 2 && params[0].toZ3() instanceof ArrayExpr)
            return ExprWrapper.fromZ3(z3.mkSelect((ArrayExpr) params[0].toZ3(), params[1].toZ3()));
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ExprWrapper makeStore(String name, ExprWrapper[] params) {
        if (params.length == 3 && params[0].toZ3() instanceof ArrayExpr)
            return ExprWrapper.fromZ3(z3.mkStore((ArrayExpr) params[0].toZ3(), params[1].toZ3(), params[2].toZ3()));
        return null;
    }

    // #####################Boolean Operations#####################
    public ExprWrapper makeEquals(ExprWrapper e1, ExprWrapper e2) {
        if (e1.toZ3() instanceof FPExpr || e2.toZ3() instanceof FPExpr)
            return ExprWrapper.fromZ3(z3.mkFPEq(toFP(e1), toFP(e2)));

        return ExprWrapper.fromZ3(z3.mkEq(e1.toZ3(), e2.toZ3()));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ExprWrapper makeLt(ExprWrapper e1, ExprWrapper e2) {
        if (e1.toZ3() instanceof FPExpr || e2.toZ3() instanceof FPExpr)
            return ExprWrapper.fromZ3(z3.mkFPLt(toFP(e1), toFP(e2)));

        return ExprWrapper.fromZ3(z3.mkLt((ArithExpr) e1.toZ3(), (ArithExpr) e2.toZ3()));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ExprWrapper makeLtEq(ExprWrapper e1, ExprWrapper e2) {
        if (e1.toZ3() instanceof FPExpr || e2.toZ3() instanceof FPExpr)
            return ExprWrapper.fromZ3(z3.mkFPLEq(toFP(e1), toFP(e2)));

        return ExprWrapper.fromZ3(z3.mkLe((ArithExpr) e1.toZ3(), (ArithExpr) e2.toZ3()));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ExprWrapper makeGt(ExprWrapper e1, ExprWrapper e2) {
        if (e1.toZ3() instanceof FPExpr || e2.toZ3() instanceof FPExpr)
            return ExprWrapper.fromZ3(z3.mkFPGt(toFP(e1), toFP(e2)));

        return ExprWrapper.fromZ3(z3.mkGt((ArithExpr) e1.toZ3(), (ArithExpr) e2.toZ3()));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ExprWrapper makeGtEq(ExprWrapper e1, ExprWrapper e2) {
        if (e1.toZ3() instanceof FPExpr || e2.toZ3() instanceof FPExpr)
            return ExprWrapper.fromZ3(z3.mkFPGEq(toFP(e1), toFP(e2)));

        return ExprWrapper.fromZ3(z3.mkGe((ArithExpr) e1.toZ3(), (ArithExpr) e2.toZ3()));
    }

    public ExprWrapper makeImplies(ExprWrapper e1, ExprWrapper e2) {
        return ExprWrapper.fromZ3(z3.mkImplies((BoolExpr) e1.toZ3(), (BoolExpr) e2.toZ3()));
    }

    public ExprWrapper makeBiconditional(ExprWrapper eval, ExprWrapper eval2) {
        return ExprWrapper.fromZ3(z3.mkIff((BoolExpr) eval.toZ3(), (BoolExpr) eval2.toZ3()));
    }

    public ExprWrapper makeAnd(ExprWrapper eval, ExprWrapper eval2) {
        return ExprWrapper.fromZ3(z3.mkAnd((BoolExpr) eval.toZ3(), (BoolExpr) eval2.toZ3()));
    }

    public ExprWrapper mkNot(ExprWrapper e1) {
        return ExprWrapper.fromZ3(z3.mkNot((BoolExpr) e1.toZ3()));
    }

    public ExprWrapper makeOr(ExprWrapper eval, ExprWrapper eval2) {
        return ExprWrapper.fromZ3(z3.mkOr((BoolExpr) eval.toZ3(), (BoolExpr) eval2.toZ3()));
    }

    // public ExprWrapper makeIf(ExprWrapper eval, ExprWrapper eval2) {
    // z3.mkI
    // return z3.mkOr((BoolExpr) eval, (BoolExpr) eval2);
    // }

    // ##################### Unary Operations #####################
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ExprWrapper makeMinus(ExprWrapper eval) {
        if (eval.toZ3() instanceof FPExpr)
            return ExprWrapper.fromZ3(z3.mkFPNeg((FPExpr) eval.toZ3()));
        return ExprWrapper.fromZ3(z3.mkUnaryMinus((ArithExpr) eval.toZ3()));
    }

    // #####################Arithmetic Operations#####################
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ExprWrapper makeAdd(ExprWrapper eval, ExprWrapper eval2) {
        if (eval.toZ3() instanceof FPExpr || eval2.toZ3() instanceof FPExpr)
            return ExprWrapper.fromZ3(z3.mkFPAdd(z3.mkFPRoundNearestTiesToEven(), toFP(eval), toFP(eval2)));

        return ExprWrapper.fromZ3(z3.mkAdd((ArithExpr) eval.toZ3(), (ArithExpr) eval2.toZ3()));

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ExprWrapper makeSub(ExprWrapper eval, ExprWrapper eval2) {
        if (eval.toZ3() instanceof FPExpr || eval2.toZ3() instanceof FPExpr)
            return ExprWrapper.fromZ3(z3.mkFPSub(z3.mkFPRoundNearestTiesToEven(), toFP(eval), toFP(eval2)));

        return ExprWrapper.fromZ3(z3.mkSub((ArithExpr) eval.toZ3(), (ArithExpr) eval2.toZ3()));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ExprWrapper makeMul(ExprWrapper eval, ExprWrapper eval2) {
        if (eval.toZ3() instanceof FPExpr || eval2.toZ3() instanceof FPExpr)
            return ExprWrapper.fromZ3(z3.mkFPMul(z3.mkFPRoundNearestTiesToEven(), toFP(eval), toFP(eval2)));

        return ExprWrapper.fromZ3(z3.mkMul((ArithExpr) eval.toZ3(), (ArithExpr) eval2.toZ3()));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ExprWrapper makeDiv(ExprWrapper eval, ExprWrapper eval2) {
        if (eval.toZ3() instanceof FPExpr || eval2.toZ3() instanceof FPExpr)
            return ExprWrapper.fromZ3(z3.mkFPDiv(z3.mkFPRoundNearestTiesToEven(), toFP(eval), toFP(eval2)));

        return ExprWrapper.fromZ3(z3.mkDiv((ArithExpr) eval.toZ3(), (ArithExpr) eval2.toZ3()));
    }

    public ExprWrapper makeMod(ExprWrapper eval, ExprWrapper eval2) {
        if (eval.toZ3() instanceof FPExpr || eval2.toZ3() instanceof FPExpr)
            return ExprWrapper.fromZ3(z3.mkFPRem(toFP(eval), toFP(eval2)));
        return ExprWrapper.fromZ3(z3.mkMod((IntExpr) eval.toZ3(), (IntExpr) eval2.toZ3()));
    }

    private FPExpr toFP(ExprWrapper e) {
        FPExpr f;
        if (e.toZ3() instanceof FPExpr) {
            f = (FPExpr) e.toZ3();
        } else if (e.toZ3() instanceof IntNum)
            f = z3.mkFP(((IntNum) e.toZ3()).getInt(), z3.mkFPSort64());
        else if (e.toZ3() instanceof IntExpr) {
            IntExpr ee = (IntExpr) e.toZ3();
            RealExpr re = z3.mkInt2Real(ee);
            f = z3.mkFPToFP(z3.mkFPRoundNearestTiesToEven(), re, z3.mkFPSort64());
        } else if (e.toZ3() instanceof RealExpr) {
            f = z3.mkFPToFP(z3.mkFPRoundNearestTiesToEven(), (RealExpr) e.toZ3(), z3.mkFPSort64());
        } else {
            f = null;
            System.out.println("Not implemented!!");
        }
        return f;
    }

    public ExprWrapper makeIte(ExprWrapper c, ExprWrapper t, ExprWrapper e) {
        if (c.toZ3() instanceof BoolExpr)
            return ExprWrapper.fromZ3(z3.mkITE((BoolExpr) c.toZ3(), t.toZ3(), e.toZ3()));
        System.out.println("Condition is not a boolean expression");
        return null;
    }

}

class TranslatorContextToZ3 {

    static void translateVariables(Context z3, Map<String, CtTypeReference<?>> ctx,
            Map<String, ExprWrapper> varTranslation) {

        for (String name : ctx.keySet())
            varTranslation.put(name, getExpr(z3, name, ctx.get(name)));

        varTranslation.put("true", ExprWrapper.fromZ3(z3.mkBool(true)));
        varTranslation.put("false", ExprWrapper.fromZ3(z3.mkBool(false)));

    }

    public static void storeVariablesSubtypes(Context z3, List<RefinedVariable> variables,
            Map<String, List<ExprWrapper>> varSuperTypes) {
        for (RefinedVariable v : variables) {
            if (!v.getSuperTypes().isEmpty()) {
                ArrayList<ExprWrapper> a = new ArrayList<>();
                for (CtTypeReference<?> ctr : v.getSuperTypes())
                    a.add(getExpr(z3, v.getName(), ctr));
                varSuperTypes.put(v.getName(), a);
            }
        }

    }

    private static ExprWrapper getExpr(Context z3, String name, CtTypeReference<?> type) {
        String typeName = type.getQualifiedName();
        if (typeName.contentEquals("int"))
            return ExprWrapper.fromZ3(z3.mkIntConst(name));
        else if (typeName.contentEquals("short"))
            return ExprWrapper.fromZ3(z3.mkIntConst(name));
        else if (typeName.contentEquals("boolean"))
            return ExprWrapper.fromZ3(z3.mkBoolConst(name));
        else if (typeName.contentEquals("long"))
            return ExprWrapper.fromZ3(z3.mkRealConst(name));
        else if (typeName.contentEquals("float")) {
            return ExprWrapper.fromZ3((FPExpr) z3.mkConst(name, z3.mkFPSort64()));
        } else if (typeName.contentEquals("double")) {
            return ExprWrapper.fromZ3((FPExpr) z3.mkConst(name, z3.mkFPSort64()));
        } else if (typeName.contentEquals("int[]")) {
            return ExprWrapper.fromZ3(z3.mkArrayConst(name, z3.mkIntSort(), z3.mkIntSort()));
        } else {
            Sort nSort = z3.mkUninterpretedSort(typeName);
            return ExprWrapper.fromZ3(z3.mkConst(name, nSort));
            // System.out.println("Add new type: "+typeName);
        }

    }

    static void addAlias(Context z3, List<AliasWrapper> alias, Map<String, AliasWrapper> aliasTranslation) {
        for (AliasWrapper a : alias) {
            aliasTranslation.put(a.getName(), a);
        }
    }

    public static void addGhostFunctions(Context z3, List<GhostFunction> ghosts,
            Map<String, FuncDecl<?>> funcTranslation) {
        addBuiltinFunctions(z3, funcTranslation);
        if (!ghosts.isEmpty()) {
            for (GhostFunction gh : ghosts) {
                addGhostFunction(z3, gh, funcTranslation);
            }
        }
    }

    private static void addBuiltinFunctions(Context z3, Map<String, FuncDecl<?>> funcTranslation) {
        funcTranslation.put("length", z3.mkFuncDecl("length", getSort(z3, "int[]"), getSort(z3, "int")));// ERRRRRRRRRRRRO!!!!!!!!!!!!!
        System.out.println("\nWorks only for int[] now! Change in future. Ignore this message, it is a glorified todo");
        // TODO add built-in function
        Sort[] s = Stream.of(getSort(z3, "int[]"), getSort(z3, "int"), getSort(z3, "int")).toArray(Sort[]::new);
        funcTranslation.put("addToIndex", z3.mkFuncDecl("addToIndex", s, getSort(z3, "void")));

        s = Stream.of(getSort(z3, "int[]"), getSort(z3, "int")).toArray(Sort[]::new);
        funcTranslation.put("getFromIndex", z3.mkFuncDecl("getFromIndex", s, getSort(z3, "int")));

    }

    static Sort getSort(Context z3, String sort) {
        switch (sort) {
        case "int":
            return z3.getIntSort();
        case "boolean":
            return z3.getBoolSort();
        case "long":
            return z3.getRealSort();
        case "float":
            return z3.mkFPSort32();
        case "double":
            return z3.mkFPSortDouble();
        case "int[]":
            return z3.mkArraySort(z3.mkIntSort(), z3.mkIntSort());
        case "String":
            return z3.getStringSort();
        case "void":
            return z3.mkUninterpretedSort("void");
        // case "List":return z3.mkListSort(name, elemSort)
        default:
            return z3.mkUninterpretedSort(sort);
        }
    }

    public static void addGhostStates(Context z3, List<GhostState> ghostState,
            Map<String, FuncDecl<?>> funcTranslation) {
        for (GhostState g : ghostState) {
            addGhostFunction(z3, g, funcTranslation);
            // if(g.getRefinement() != null)
            // premisesToAdd.add(g.getRefinement().getExpression());
        }

    }

    private static void addGhostFunction(Context z3, GhostFunction gh, Map<String, FuncDecl<?>> funcTranslation) {
        List<CtTypeReference<?>> paramTypes = gh.getParametersTypes();
        Sort ret = getSort(z3, gh.getReturnType().toString());
        Sort[] d = paramTypes.stream().map(t -> t.toString()).map(t -> getSort(z3, t)).toArray(Sort[]::new);
        funcTranslation.put(gh.getName(), z3.mkFuncDecl(gh.getName(), d, ret));
    }

}
