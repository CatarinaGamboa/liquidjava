package liquidjava.smt.solver_wrapper.Z3Wrapper;

import com.martiansoftware.jsap.SyntaxException;
import com.microsoft.z3.*;
import liquidjava.processor.context.AliasWrapper;
import liquidjava.processor.context.GhostFunction;
import liquidjava.processor.context.GhostState;
import liquidjava.processor.context.RefinedVariable;
import liquidjava.rj_language.ast.*;
import liquidjava.rj_language.visitors.AbstractExpressionVisitor;
import liquidjava.smt.NotFoundError;
import spoon.reflect.reference.CtTypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.Assert.fail;

public class Z3Translator extends AbstractExpressionVisitor {
    protected final Map<String, Expr<?>> varTranslation = new HashMap<>();
    protected final Map<String, List<Expr<?>>> varSuperTypes = new HashMap<>();
    protected final Map<String, AliasWrapper> aliasTranslation = new HashMap<>();
    protected final Map<String, FuncDecl<?>> funcTranslation = new HashMap<>();

    public Context getContext() {
        return z3;
    }

    private final com.microsoft.z3.Context z3 = new com.microsoft.z3.Context();

    public Expr<?> getResult() {
        return result;
    }

    private Expr<?> result;

    public Z3Translator(liquidjava.processor.context.Context c) {
        ContextTranslator.translateVariables(z3, c.getTypeContext(), varTranslation);
        ContextTranslator.storeVariablesSubtypes(z3, c.getAllVariablesWithSupertypes(), varSuperTypes);
        ContextTranslator.addAlias(z3, c.getAlias(), aliasTranslation);
        ContextTranslator.addGhostFunctions(z3, c.getGhosts(), funcTranslation);
        ContextTranslator.addGhostStates(z3, c.getGhostState(), funcTranslation);
        System.out.println();
    }

    protected Expr<?> getVariableTranslation(String name) throws Exception {
        if (!varTranslation.containsKey(name))
            throw new NotFoundError("Variable '" + name.toString() + "' not found");
        Expr<?> e = varTranslation.get(name);
        if (e == null)
            e = varTranslation.get(String.format("this#%s", name));
        if (e == null)
            throw new SyntaxException("Unknown variable:" + name);
        return e;
    }

    public Expr<?> makeVariable(String name) throws Exception {
        return getVariableTranslation(name);// int[] not in varTranslation
    }

    private FPExpr toFP(Expr<?> e) {
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

        }
        Expr<?>[] z3params = new Expr[params.length];
        System.arraycopy(params, 0, z3params, 0, params.length);

        return z3.mkApp(fd, z3params);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Expr<?> makeSelect(String name, Expr<?>[] params) {
        if (params.length == 2 && params[0] instanceof ArrayExpr)
            return z3.mkSelect((ArrayExpr) (params[0]), params[1]);
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Expr<?> makeStore(String name, Expr<?>[] params) {
        if (params.length == 3 && params[0] instanceof ArrayExpr)
            return z3.mkStore((ArrayExpr) params[0], params[1], params[2]);
        return null;
    }

    @Override
    public void visitAliasInvocation(AliasInvocation ai) throws Exception {
        Expr<?>[] argsExpr = new Expr<?>[ai.getArgs().size()];
        for (int i = 0; i < argsExpr.length; i++) {
            ai.getArgs().get(i).accept(this);

            argsExpr[i] = this.result;
        }
        result = makeFunctionInvocation(ai.getName(), argsExpr);
    }

    @Override
    public void visitBinaryExpression(BinaryExpression be) throws Exception {
        be.getFirstOperand().accept(this);
        Expr<?> left = result;
        be.getSecondOperand().accept(this);
        Expr<?> right = result;
        result = null;
        switch (be.getOperator()) {
        case "&&":
            result = z3.mkAnd((BoolExpr) left, (BoolExpr) right);
            break;
        case "||":
            result = z3.mkOr((BoolExpr) left, (BoolExpr) right);
            break;
        case "-->":
            result = z3.mkImplies((BoolExpr) left, (BoolExpr) right);
            break;
        case "==":
            if (left instanceof FPExpr || right instanceof FPExpr) {
                result = z3.mkFPEq(toFP(left), toFP(right));
            } else {
                result = z3.mkEq(left, right);
            }
            break;
        case "!=":
            if (left instanceof FPExpr || right instanceof FPExpr) {
                result = z3.mkNot(z3.mkFPEq(toFP(left), toFP(right)));
            } else {
                result = z3.mkNot(z3.mkEq(left, right));
            }
            break;
        case ">=":
            if (left instanceof FPExpr || right instanceof FPExpr) {
                result = z3.mkFPGEq(toFP(left), toFP(right));
            } else {
                result = z3.mkGe((ArithExpr) left, (ArithExpr) right);
            }
            break;
        case ">":
            if (left instanceof FPExpr || right instanceof FPExpr) {
                result = z3.mkFPGt(toFP(left), toFP(right));
            } else {
                result = z3.mkGt((ArithExpr) left, (ArithExpr) right);
            }
            break;
        case "<=":
            if (left instanceof FPExpr || right instanceof FPExpr) {
                result = z3.mkFPLEq(toFP(left), toFP(right));
            } else {
                result = z3.mkLe((ArithExpr) left, (ArithExpr) right);
            }
            break;
        case "<":
            if (left instanceof FPExpr || right instanceof FPExpr) {
                result = z3.mkFPLt(toFP(left), toFP(right));
            } else {
                result = z3.mkLt((ArithExpr) left, (ArithExpr) right);
            }
            break;
        case "+":
            if (left instanceof FPExpr || right instanceof FPExpr) {
                result = z3.mkFPAdd(z3.mkFPRoundNearestTiesToEven(), toFP(left), toFP(right));
            } else {
                result = z3.mkAdd((ArithExpr) left, (ArithExpr) right);
            }
            break;
        case "-":
            if (left instanceof FPExpr || right instanceof FPExpr) {
                result = z3.mkFPSub(z3.mkFPRoundNearestTiesToEven(), toFP(left), toFP(right));
            } else {
                result = z3.mkSub((ArithExpr) left, (ArithExpr) right);
            }
            break;
        case "*":
            if (left instanceof FPExpr || right instanceof FPExpr) {
                result = z3.mkFPMul(z3.mkFPRoundNearestTiesToEven(), toFP(left), toFP(right));
            } else {
                result = z3.mkMul((ArithExpr) left, (ArithExpr) right);
            }
            break;
        case "/":
            if (left instanceof FPExpr || right instanceof FPExpr) {
                result = z3.mkFPDiv(z3.mkFPRoundNearestTiesToEven(), toFP(left), toFP(right));
            } else {
                result = z3.mkDiv((ArithExpr) left, (ArithExpr) right);
            }
            break;
        case "%":
            if (left instanceof FPExpr || right instanceof FPExpr) {
                result = z3.mkFPRem(toFP(left), toFP(right));
            } else {
                result = z3.mkRem((IntExpr) left, (IntExpr) right);
            }
            break;
        case "|->":
            throw new Exception("Z3 does not support separation logic: |->");
        case "|*":
            throw new Exception("Z3 does not support separation logic: |*");
        }
        if (result == null) {
            fail("Reached unknown operation `" + be.getOperator() + "`");
        }
    }

    @Override
    public void visitFunctionInvocation(FunctionInvocation fi) throws Exception {
        Expr<?>[] argsExpr = new Expr<?>[fi.getArgs().size()];
        for (int i = 0; i < argsExpr.length; i++) {
            fi.getArgs().get(i).accept(this);

            argsExpr[i] = this.result;
        }
        result = makeFunctionInvocation(fi.getName(), argsExpr);
    }

    @Override
    public void visitITE(Ite ite) throws Exception {
        ite.getCondition().accept(this);
        Expr<?> c = result;
        ite.getThen().accept(this);
        Expr<?> t = result;
        ite.getElse().accept(this);
        Expr<?> e = result;

        if (!(c instanceof BoolExpr)) {
            System.out.println("Condition is not a boolean expression");
            result = null;
        } else {
            result = z3.mkITE((BoolExpr) c, t, e);
        }

    }

    @Override
    public void visitLiteralBoolean(LiteralBoolean lb) {
        result = z3.mkBool(lb.getValue());
    }

    @Override
    public void visitLiteralInt(LiteralInt li) {
        result = z3.mkInt(li.getValue());
    }

    @Override
    public void visitLiteralReal(LiteralReal lr) {
        result = z3.mkFP(lr.getValue(), z3.mkFPSort64());
    }

    @Override
    public void visitLiteralString(LiteralString ls) {
        result = z3.mkString(ls.getValue());
    }

    @Override
    public void visitUnaryExpression(UnaryExpression ue) throws Exception {
        ue.getExpression().accept(this);
        Expr<?> e = result;
        result = null;
        switch (ue.getOp()) {
        case "-":
            if (e instanceof FPExpr)
                result = z3.mkFPNeg((FPExpr) e);
            else {
                result = z3.mkUnaryMinus((ArithExpr) e);
            }
            break;
        case "!":
            result = z3.mkNot((BoolExpr) e);
            break;
        }

    }

    @Override
    public void visitVar(Var v) throws Exception {
        result = makeVariable(v.getName());
    }

    @Override
    public void visitUnit(SepUnit unit) throws Exception {
        throw new Exception("Z3 solver does not support separation logic!");
    }

    @Override
    public void visitSepEmp(SepEmp sepEmp) throws Exception {
        throw new Exception("Z3 solver does not support separation logic!");
    }
}

class ContextTranslator {

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
