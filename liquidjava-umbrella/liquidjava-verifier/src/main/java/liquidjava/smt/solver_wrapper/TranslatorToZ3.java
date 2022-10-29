package liquidjava.smt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.microsoft.z3.*;

import liquidjava.processor.context.AliasWrapper;
import liquidjava.processor.context.GhostFunction;
import liquidjava.processor.context.GhostState;
import liquidjava.processor.context.RefinedVariable;
import liquidjava.smt.solver_wrapper.smt;
import spoon.reflect.reference.CtTypeReference;

public class TranslatorToZ3 extends smt {

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
    public Status verifyExpression(Expr<?> e) throws Exception {
        Solver s = z3.mkSolver();
        // s.add((BoolExpr) e.eval(this));
        // for(Expression ex: premisesToAdd)
        // s.add((BoolExpr) ex.eval(this));
        s.add((BoolExpr) e);
        Status st = s.check();
        if (st.equals(Status.SATISFIABLE)) {
            // Example of values
            // System.out.println(s.getModel());
        }
        return st;
    }

    // #####################Literals and Variables#####################
    public Expr<?> makeIntegerLiteral(int value) {
        return z3.mkInt(value);
    }

    public Expr<?> makeLongLiteral(long value) {
        return z3.mkReal(value);
    }

    public Expr<?> makeDoubleLiteral(double value) {
        return z3.mkFP(value, z3.mkFPSort64());
    }

    public Expr<?> makeString(String s) {
        return z3.mkString(s);
    }

    public Expr<?> makeBooleanLiteral(boolean value) {
        return z3.mkBool(value);
    }

    public Expr<?> makeFunctionInvocation(String name, Expr<?>[] params) throws Exception {
        if (name.equals("addToIndex"))
            return makeStore(name, params);
        if (name.equals("getFromIndex"))
            return makeSelect(name, params);

        if (!funcTranslation.containsKey(name))
            throw new NotFoundError("Function '" + name + "' not found");

        FuncDecl<?> fd = funcTranslation.get(name);
        Sort[] s = fd.getDomain();
        for (int i = 0; i < s.length; i++) {
            Expr<?> param = params[i];
            if (!s[i].equals(param.getSort())) {
                // Look if the function type is a supertype of this
                List<Expr<?>> le = varSuperTypes.get(param.toString().replace("|", ""));
                if (le != null)
                    for (Expr<?> e : le)
                        if (e.getSort().equals(s[i]))
                            params[i] = e;
            }
            // System.out.println("Expected sort"+s[i]+"; Final sort->" +params[i].toString() +":"+
            // params[i].getSort());
        }

        return z3.mkApp(fd, params);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Expr<?> makeSelect(String name, Expr<?>[] params) {
        if (params.length == 2 && params[0] instanceof ArrayExpr)
            return z3.mkSelect((ArrayExpr) params[0], params[1]);
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Expr<?> makeStore(String name, Expr<?>[] params) {
        if (params.length == 3 && params[0] instanceof ArrayExpr)
            return z3.mkStore((ArrayExpr) params[0], params[1], params[2]);
        return null;
    }

    // #####################Boolean Operations#####################
    public Expr<?> makeEquals(Expr<?> e1, Expr<?> e2) {
        if (e1 instanceof FPExpr || e2 instanceof FPExpr)
            return z3.mkFPEq(toFP(e1), toFP(e2));

        return z3.mkEq(e1, e2);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Expr<?> makeLt(Expr<?> e1, Expr<?> e2) {
        if (e1 instanceof FPExpr || e2 instanceof FPExpr)
            return z3.mkFPLt(toFP(e1), toFP(e2));

        return z3.mkLt((ArithExpr) e1, (ArithExpr) e2);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Expr<?> makeLtEq(Expr<?> e1, Expr<?> e2) {
        if (e1 instanceof FPExpr || e2 instanceof FPExpr)
            return z3.mkFPLEq(toFP(e1), toFP(e2));

        return z3.mkLe((ArithExpr) e1, (ArithExpr) e2);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Expr<?> makeGt(Expr<?> e1, Expr<?> e2) {
        if (e1 instanceof FPExpr || e2 instanceof FPExpr)
            return z3.mkFPGt(toFP(e1), toFP(e2));

        return z3.mkGt((ArithExpr) e1, (ArithExpr) e2);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Expr<?> makeGtEq(Expr<?> e1, Expr<?> e2) {
        if (e1 instanceof FPExpr || e2 instanceof FPExpr)
            return z3.mkFPGEq(toFP(e1), toFP(e2));

        return z3.mkGe((ArithExpr) e1, (ArithExpr) e2);
    }

    public Expr<?> makeImplies(Expr<?> e1, Expr<?> e2) {
        return z3.mkImplies((BoolExpr) e1, (BoolExpr) e2);
    }

    public Expr<?> makeBiconditional(Expr<?> eval, Expr<?> eval2) {
        return z3.mkIff((BoolExpr) eval, (BoolExpr) eval2);
    }

    public Expr<?> makeAnd(Expr<?> eval, Expr<?> eval2) {
        return z3.mkAnd((BoolExpr) eval, (BoolExpr) eval2);
    }

    public Expr<?> mkNot(Expr<?> e1) {
        return z3.mkNot((BoolExpr) e1);
    }

    public Expr<?> makeOr(Expr<?> eval, Expr<?> eval2) {
        return z3.mkOr((BoolExpr) eval, (BoolExpr) eval2);
    }

    // public Expr<?> makeIf(Expr<?> eval, Expr<?> eval2) {
    // z3.mkI
    // return z3.mkOr((BoolExpr) eval, (BoolExpr) eval2);
    // }

    // ##################### Unary Operations #####################
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Expr<?> makeMinus(Expr<?> eval) {
        if (eval instanceof FPExpr)
            return z3.mkFPNeg((FPExpr) eval);
        return z3.mkUnaryMinus((ArithExpr) eval);
    }

    // #####################Arithmetic Operations#####################
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Expr<?> makeAdd(Expr<?> eval, Expr<?> eval2) {
        if (eval instanceof FPExpr || eval2 instanceof FPExpr)
            return z3.mkFPAdd(z3.mkFPRoundNearestTiesToEven(), toFP(eval), toFP(eval2));

        return z3.mkAdd((ArithExpr) eval, (ArithExpr) eval2);

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Expr<?> makeSub(Expr<?> eval, Expr<?> eval2) {
        if (eval instanceof FPExpr || eval2 instanceof FPExpr)
            return z3.mkFPSub(z3.mkFPRoundNearestTiesToEven(), toFP(eval), toFP(eval2));

        return z3.mkSub((ArithExpr) eval, (ArithExpr) eval2);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Expr<?> makeMul(Expr<?> eval, Expr<?> eval2) {
        if (eval instanceof FPExpr || eval2 instanceof FPExpr)
            return z3.mkFPMul(z3.mkFPRoundNearestTiesToEven(), toFP(eval), toFP(eval2));

        return z3.mkMul((ArithExpr) eval, (ArithExpr) eval2);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Expr<?> makeDiv(Expr<?> eval, Expr<?> eval2) {
        if (eval instanceof FPExpr || eval2 instanceof FPExpr)
            return z3.mkFPDiv(z3.mkFPRoundNearestTiesToEven(), toFP(eval), toFP(eval2));

        return z3.mkDiv((ArithExpr) eval, (ArithExpr) eval2);
    }

    public Expr<?> makeMod(Expr<?> eval, Expr<?> eval2) {
        if (eval instanceof FPExpr || eval2 instanceof FPExpr)
            return z3.mkFPRem(toFP(eval), toFP(eval2));
        return z3.mkMod((IntExpr) eval, (IntExpr) eval2);
    }

    public FPExpr toFP(Expr<?> e) {
        FPExpr f;
        if (e instanceof FPExpr) {
            f = (FPExpr) e;
        } else if (e instanceof IntNum)
            f = z3.mkFP(((IntNum) e).getInt(), z3.mkFPSort64());
        else if (e instanceof IntExpr) {
            IntExpr ee = (IntExpr) e;
            RealExpr re = z3.mkInt2Real(ee);
            f = z3.mkFPToFP(z3.mkFPRoundNearestTiesToEven(), re, z3.mkFPSort64());
        } else if (e instanceof RealExpr) {
            f = z3.mkFPToFP(z3.mkFPRoundNearestTiesToEven(), (RealExpr) e, z3.mkFPSort64());
        } else {
            f = null;
            System.out.println("Not implemented!!");
        }
        return f;
    }

    public Expr<?> makeIte(Expr<?> c, Expr<?> t, Expr<?> e) {
        if (c instanceof BoolExpr)
            return z3.mkITE((BoolExpr) c, t, e);
        System.out.println("Condition is not a boolean expression");
        return null;
    }

}

class TranslatorContextToZ3 {

    static void translateVariables(Context z3, Map<String, CtTypeReference<?>> ctx,
                                   Map<String, Expr<?>> varTranslation) {

        for (String name : ctx.keySet())
            varTranslation.put(name, getExpr(z3, name, ctx.get(name)));

        varTranslation.put("true", z3.mkBool(true));
        varTranslation.put("false", z3.mkBool(false));

    }

    public static void storeVariablesSubtypes(Context z3, List<RefinedVariable> variables,
                                              Map<String, List<Expr<?>>> varSuperTypes) {
        for (RefinedVariable v : variables) {
            if (!v.getSuperTypes().isEmpty()) {
                ArrayList<Expr<?>> a = new ArrayList<>();
                for (CtTypeReference<?> ctr : v.getSuperTypes())
                    a.add(getExpr(z3, v.getName(), ctr));
                varSuperTypes.put(v.getName(), a);
            }
        }

    }

    private static Expr<?> getExpr(Context z3, String name, CtTypeReference<?> type) {
        String typeName = type.getQualifiedName();
        if (typeName.contentEquals("int"))
            return z3.mkIntConst(name);
        else if (typeName.contentEquals("short"))
            return z3.mkIntConst(name);
        else if (typeName.contentEquals("boolean"))
            return z3.mkBoolConst(name);
        else if (typeName.contentEquals("long"))
            return z3.mkRealConst(name);
        else if (typeName.contentEquals("float")) {
            return (FPExpr) z3.mkConst(name, z3.mkFPSort64());
        } else if (typeName.contentEquals("double")) {
            return (FPExpr) z3.mkConst(name, z3.mkFPSort64());
        } else if (typeName.contentEquals("int[]")) {
            return z3.mkArrayConst(name, z3.mkIntSort(), z3.mkIntSort());
        } else {
            Sort nSort = z3.mkUninterpretedSort(typeName);
            return z3.mkConst(name, nSort);
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

