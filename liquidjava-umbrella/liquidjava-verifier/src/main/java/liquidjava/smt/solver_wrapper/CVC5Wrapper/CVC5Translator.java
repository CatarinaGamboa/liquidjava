package liquidjava.smt.solver_wrapper.CVC5Wrapper;

import com.martiansoftware.jsap.SyntaxException;
import com.microsoft.z3.*;
import io.github.cvc5.Solver;
import io.github.cvc5.Sort;
import liquidjava.processor.context.*;
import liquidjava.processor.context.Context;
import liquidjava.rj_language.ast.*;
import liquidjava.rj_language.visitors.AbstractExpressionVisitor;
import liquidjava.smt.NotFoundError;
import spoon.reflect.reference.CtTypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import io.github.cvc5.*;

import static org.junit.Assert.fail;

public class CVC5Translator extends AbstractExpressionVisitor {

    protected final Map<String, Term> varTranslation = new HashMap<>();
    protected final Map<String, List<Term>> varSuperTypes = new HashMap<>();
    protected final Map<String, AliasWrapper> aliasTranslation = new HashMap<>();
    protected final Map<String, Term> funcTranslation = new HashMap<>();

    private final io.github.cvc5.Solver cvc5 = new io.github.cvc5.Solver();

    private Term result;

    CVC5Translator(Context c) throws CVC5ApiException {
        ContextTranslator.translateVariables(cvc5, c.getContext(), varTranslation);
        ContextTranslator.storeVariablesSubtypes(cvc5, c.getAllVariablesWithSupertypes(), varSuperTypes);
        ContextTranslator.addAlias(cvc5, c.getAlias(), aliasTranslation);
        ContextTranslator.addGhostFunctions(cvc5, c.getGhosts(), funcTranslation);
        ContextTranslator.addGhostStates(cvc5, c.getGhostState(), funcTranslation);
    }

    protected Term getVariableTranslation(String name) throws Exception {
        if (!varTranslation.containsKey(name))
            throw new NotFoundError("Variable '" + name.toString() + "' not found");
        Term e = varTranslation.get(name);
        if (e == null)
            e = varTranslation.get(String.format("this#%s", name));
        if (e == null)
            throw new SyntaxException("Unknown variable:" + name);
        return e;
    }

    public Term makeVariable(String name) throws Exception {
        return getVariableTranslation(name);// int[] not in varTranslation
    }

    private Term toFP(Term t) {
        if (t.isFloatingPointValue()) {
            return t;
        }
        if (t.isIntegerValue()) {
            return cvc5.mkTerm(cvc5.mkOp(Kind.FLOATINGPOINT_TO_FP_FROM_REAL), cvc5.mkTerm(cvc5.mkOp(Kind.TO_REAL), t));
        }
        if (t.isRealValue()) {
            return cvc5.mkTerm(cvc5.mkOp(Kind.FLOATINGPOINT_TO_FP_FROM_REAL), t);
        }
        throw new RuntimeException("Expected int, real or floating point. Got " + t);
    }

    public Term makeFunctionInvocation(String name, Term[] params) throws CVC5ApiException, Exception {
        if (name.equals("addToIndex"))
            return makeStore(name, params);
        if (name.equals("getFromIndex"))
            return makeSelect(name, params);

        Term fd = funcTranslation.get(name);
        Sort funcSort = fd.getSort();
        Sort[] argSorts = funcSort.getFunctionDomainSorts();

        for (int i = 0; i < argSorts.length; ++i) {
            Term param = params[i];
            if (argSorts[i].equals(param.getSort())) {
                continue;
            }
            System.out.println("Searching for " + param + " should have `|`s");
            List<Term> le = varSuperTypes.get(param.toString().replace("|", ""));

            if (le == null) {
                continue;
            }
            for (Term t : le) {
                if (t.getSort().equals(argSorts[i])) {
                    params[i] = t;
                }
            }
        }

        Term[] cvc5params = new Term[params.length];
        System.arraycopy(params, 0, cvc5params, 0, params.length);

        return cvc5.mkTerm(cvc5.mkOp(Kind.APPLY_UF), cvc5params);
    }

    public Term makeSelect(String name, Term[] params) throws CVC5ApiException {
        if (params.length == 2 && params[0].getSort() == ContextTranslator.getSort(cvc5, "int[]")) {
            Op selectOp = cvc5.mkOp(Kind.SELECT);
            return cvc5.mkTerm(selectOp, params);
        }
        return null;
    }

    public Term makeStore(String name, Term[] params) throws CVC5ApiException {
        if (params.length == 3 && params[0].getSort() == ContextTranslator.getSort(cvc5, "int[]")) {
            Op selectOp = cvc5.mkOp(Kind.STORE);
            return cvc5.mkTerm(selectOp, params);
        }
        return null;
    }

    @Override
    public void visitAliasInvocation(AliasInvocation ai) throws Exception {
        Term[] argsExpr = new Term[ai.getArgs().size()];
        for (int i = 0; i < argsExpr.length; i++) {
            ai.getArgs().get(i).accept(this);

            argsExpr[i] = this.result;
        }
        result = makeFunctionInvocation(ai.getName(), argsExpr);
    }

    @Override
    public void visitBinaryExpression(BinaryExpression be) throws Exception {
        be.getFirstOperand().accept(this);
        Term left = result;
        be.getSecondOperand().accept(this);
        Term right = result;
        result = null;
        switch (be.getOperator()) {
        case "&&":
            result = cvc5.mkTerm(cvc5.mkOp(Kind.AND), left, right);
            break;
        case "||":
            result = cvc5.mkTerm(cvc5.mkOp(Kind.OR), left, right);
            break;
        case "-->":
            result = cvc5.mkTerm(cvc5.mkOp(Kind.IMPLIES), left, right);
            break;
        case "==":
            if (left.isFloatingPointValue() || right.isFloatingPointValue()) {
                result = cvc5.mkTerm(cvc5.mkOp(Kind.FLOATINGPOINT_EQ), toFP(left), toFP(right));
            } else {
                result = cvc5.mkTerm(cvc5.mkOp(Kind.EQUAL), left, right);
            }
            break;
        case "!=":
            if (left.isFloatingPointValue() || right.isFloatingPointValue()) {
                result = cvc5.mkTerm(cvc5.mkOp(Kind.FLOATINGPOINT_EQ), toFP(left), toFP(right));
            } else {
                result = cvc5.mkTerm(cvc5.mkOp(Kind.EQUAL), left, right);
            }
            result = cvc5.mkTerm(cvc5.mkOp(Kind.NOT), result);
            break;
        case ">=":
            if (left.isFloatingPointValue() || right.isFloatingPointValue()) {
                result = cvc5.mkTerm(cvc5.mkOp(Kind.FLOATINGPOINT_GEQ), toFP(left), toFP(right));
            } else {
                result = cvc5.mkTerm(cvc5.mkOp(Kind.GEQ), left, right);
            }
            break;
        case ">":
            if (left.isFloatingPointValue() || right.isFloatingPointValue()) {
                result = cvc5.mkTerm(cvc5.mkOp(Kind.FLOATINGPOINT_GT), toFP(left), toFP(right));
            } else {
                result = cvc5.mkTerm(cvc5.mkOp(Kind.GT), left, right);
            }
            break;
        case "<=":
            if (left.isFloatingPointValue() || right.isFloatingPointValue()) {
                result = cvc5.mkTerm(cvc5.mkOp(Kind.FLOATINGPOINT_LEQ), toFP(left), toFP(right));
            } else {
                result = cvc5.mkTerm(cvc5.mkOp(Kind.LEQ), left, right);
            }
            break;
        case "<":
            if (left.isFloatingPointValue() || right.isFloatingPointValue()) {
                result = cvc5.mkTerm(cvc5.mkOp(Kind.FLOATINGPOINT_LT), toFP(left), toFP(right));
            } else {
                result = cvc5.mkTerm(cvc5.mkOp(Kind.LT), left, right);
            }
            break;
        case "+":
            if (left.isFloatingPointValue() || right.isFloatingPointValue()) {
                result = cvc5.mkTerm(cvc5.mkOp(Kind.FLOATINGPOINT_ADD), toFP(left), toFP(right));
            } else {
                result = cvc5.mkTerm(cvc5.mkOp(Kind.ADD), left, right);
            }
            break;
        case "-":
            if (left.isFloatingPointValue() || right.isFloatingPointValue()) {
                result = cvc5.mkTerm(cvc5.mkOp(Kind.FLOATINGPOINT_SUB), toFP(left), toFP(right));
            } else {
                result = cvc5.mkTerm(cvc5.mkOp(Kind.SUB), left, right);
            }
            break;
        case "*":
            if (left.isFloatingPointValue() || right.isFloatingPointValue()) {
                result = cvc5.mkTerm(cvc5.mkOp(Kind.FLOATINGPOINT_MULT), toFP(left), toFP(right));
            } else {
                result = cvc5.mkTerm(cvc5.mkOp(Kind.MULT), left, right);
            }
            break;
        case "/":
            if (left.isFloatingPointValue() || right.isFloatingPointValue()) {
                result = cvc5.mkTerm(cvc5.mkOp(Kind.FLOATINGPOINT_DIV), toFP(left), toFP(right));
            } else {
                result = cvc5.mkTerm(cvc5.mkOp(Kind.INTS_DIVISION), left, right);
            } // should here be a case for reals?
            break;
        case "%":
            if (left.isFloatingPointValue() || right.isFloatingPointValue()) {
                result = cvc5.mkTerm(cvc5.mkOp(Kind.FLOATINGPOINT_REM), toFP(left), toFP(right));
            } else {
                result = cvc5.mkTerm(cvc5.mkOp(Kind.INTS_MODULUS), left, right);
            } // no case for reals
            break;
        }
        if (result == null) {
            fail("Reached unknown operation `" + be.getOperator() + "`");
        }
    }

    @Override
    public void visitFunctionInvocation(FunctionInvocation fi) throws Exception {
        Term[] argsExpr = new Term[fi.getArgs().size()];
        for (int i = 0; i < argsExpr.length; i++) {
            fi.getArgs().get(i).accept(this);

            argsExpr[i] = this.result;
        }
        result = makeFunctionInvocation(fi.getName(), argsExpr);
    }

    @Override
    public void visitITE(Ite ite) throws Exception {
        ite.getCondition().accept(this);
        Term c = result;
        ite.getThen().accept(this);
        Term t = result;
        ite.getElse().accept(this);
        Term e = result;

        if (!c.getSort().equals(cvc5.getBooleanSort())) {
            throw new Exception("Condition is not a boolean expression");
        } else {
            result = cvc5.mkTerm(cvc5.mkOp(Kind.ITE), Stream.of(c, t, e).toArray(Term[]::new));
        }

    }

    @Override
    public void visitLiteralBoolean(LiteralBoolean lb) {
        result = cvc5.mkBoolean(lb.getValue());
    }

    @Override
    public void visitLiteralInt(LiteralInt li) {
        result = cvc5.mkInteger(li.getValue());
    }

    @Override
    public void visitLiteralReal(LiteralReal lr) {
        // result = z3.mkFP(lr.getValue(), z3.mkFPSort64());
        result = cvc5.mkReal((long) lr.getValue());// ????
    }

    @Override
    public void visitLiteralString(LiteralString ls) {
        result = cvc5.mkString(ls.getValue());
    }

    @Override
    public void visitUnaryExpression(UnaryExpression ue) throws Exception {
        super.visitUnaryExpression(ue);
        Term e = result;
        result = null;
        switch (ue.getOp()) {
        case "-":
            if (e.isFloatingPointValue())
                result = cvc5.mkTerm(cvc5.mkOp(Kind.FLOATINGPOINT_NEG), e);
            else {
                result = cvc5.mkTerm(cvc5.mkOp(Kind.NEG), e);
            }
            break;
        case "!":
            result = cvc5.mkTerm(cvc5.mkOp(Kind.NOT), e);
            break;
        }

    }

    @Override
    public void visitVar(Var v) throws Exception {
        result = makeVariable(v.getName());
    }

    public Term getResult() {
        return result;
    }

    public Solver getSolver() {
        return cvc5;
    }
}

class ContextTranslator {

    static void translateVariables(io.github.cvc5.Solver cvc5, Map<String, CtTypeReference<?>> ctx,
            Map<String, Term> varTranslation) throws CVC5ApiException {

        for (String name : ctx.keySet())
            varTranslation.put(name, getExpr(cvc5, name, ctx.get(name)));

        varTranslation.put("true", cvc5.mkBoolean(true));
        varTranslation.put("false", cvc5.mkBoolean(false));

    }

    public static void storeVariablesSubtypes(io.github.cvc5.Solver cvc5, List<RefinedVariable> variables,
            Map<String, List<Term>> varSuperTypes) throws CVC5ApiException {
        for (RefinedVariable v : variables) {
            if (!v.getSuperTypes().isEmpty()) {
                ArrayList<Term> a = new ArrayList<>();
                for (CtTypeReference<?> ctr : v.getSuperTypes())
                    a.add(getExpr(cvc5, v.getName(), ctr));
                varSuperTypes.put(v.getName(), a);
            }
        }

    }

    private static Term getExpr(io.github.cvc5.Solver cvc5, String name, CtTypeReference<?> type)
            throws CVC5ApiException {
        String typeName = type.getQualifiedName();
        if (typeName.contentEquals("int"))
            return cvc5.mkConst(cvc5.getIntegerSort(), name);
        else if (typeName.contentEquals("short"))
            return cvc5.mkConst(cvc5.getIntegerSort(), name);
        else if (typeName.contentEquals("boolean"))
            return cvc5.mkConst(cvc5.getBooleanSort(), name);
        else if (typeName.contentEquals("long"))
            return cvc5.mkConst(cvc5.getRealSort(), name); // ???
        else if (typeName.contentEquals("float")) {
            // Sort fpt32 = cvc5.mkFloatingPointSort(8, 24);
            Sort fpt64 = cvc5.mkFloatingPointSort(11, 52);
            return cvc5.mkConst(getSort(cvc5, "f64"), name);
        } else if (typeName.contentEquals("double")) {
            Sort fpt64 = cvc5.mkFloatingPointSort(11, 52);
            return cvc5.mkConst(fpt64, name);
        } else if (typeName.contentEquals("int[]")) {
            return cvc5.mkConst(cvc5.mkArraySort(cvc5.getIntegerSort(), cvc5.getIntegerSort()), name);
        } else {
            Sort nSort = cvc5.mkUninterpretedSort(typeName);
            return cvc5.mkConst(nSort, name);
        }

    }

    static void addAlias(io.github.cvc5.Solver cvc5, List<AliasWrapper> alias,
            Map<String, AliasWrapper> aliasTranslation) {
        for (AliasWrapper a : alias) {
            aliasTranslation.put(a.getName(), a);
        }
    }

    public static void addGhostFunctions(io.github.cvc5.Solver cvc5, List<GhostFunction> ghosts,
            Map<String, Term> funcTranslation) throws CVC5ApiException {
        addBuiltinFunctions(cvc5, funcTranslation);
        if (!ghosts.isEmpty()) {
            for (GhostFunction gh : ghosts) {
                addGhostFunction(cvc5, gh, funcTranslation);
            }
        }
    }

    private static void addBuiltinFunctions(io.github.cvc5.Solver cvc5, Map<String, Term> funcTranslation)
            throws CVC5ApiException {
        Term lenfun = cvc5.declareFun("length", Stream.of(getSort(cvc5, "int[]")).toArray(Sort[]::new),
                getSort(cvc5, "int"));
        funcTranslation.put("length", lenfun);// ERRRRRRRRRRRRO!!!!!!!!!!!!!
        System.out.println("\nWorks only for int[] now! Change in future. Ignore this message, it is a glorified todo");
        // TODO add built-in function
        Sort[] s = Stream.of(getSort(cvc5, "int[]"), getSort(cvc5, "int"), getSort(cvc5, "int")).toArray(Sort[]::new);
        funcTranslation.put("addToIndex", cvc5.declareFun("addToIndex", s, getSort(cvc5, "void")));

        s = Stream.of(getSort(cvc5, "int[]"), getSort(cvc5, "int")).toArray(Sort[]::new);
        funcTranslation.put("getFromIndex", cvc5.declareFun("getFromIndex", s, getSort(cvc5, "int")));

    }

    static Sort getSort(io.github.cvc5.Solver cvc5, String sort) throws CVC5ApiException {
        switch (sort) {
        case "int":
            return cvc5.getIntegerSort();
        case "boolean":
            return cvc5.getBooleanSort();
        case "long":
            return cvc5.getRealSort();
        case "float":
            return cvc5.mkFloatingPointSort(8, 24);
        case "double":
            return cvc5.mkFloatingPointSort(11, 52);
        case "int[]":
            return cvc5.mkArraySort(cvc5.getIntegerSort(), cvc5.getIntegerSort());
        case "String":
            return cvc5.getStringSort();
        case "void":
            return cvc5.mkUninterpretedSort("void");
        // case "List":return z3.mkListSort(name, elemSort)
        default:
            return cvc5.mkUninterpretedSort(sort);
        }
    }

    public static void addGhostStates(io.github.cvc5.Solver cvc5, List<GhostState> ghostState,
            Map<String, Term> funcTranslation) throws CVC5ApiException {
        for (GhostState g : ghostState) {
            addGhostFunction(cvc5, g, funcTranslation);
        }

    }

    private static void addGhostFunction(io.github.cvc5.Solver cvc5, GhostFunction gh,
            Map<String, Term> funcTranslation) throws CVC5ApiException {
        List<CtTypeReference<?>> paramTypes = gh.getParametersTypes();
        Sort ret = getSort(cvc5, gh.getReturnType().toString());
        List<Sort> list = new ArrayList<>();
        for (CtTypeReference<?> paramType : paramTypes) {
            String t = paramType.toString();
            Sort sort = getSort(cvc5, t);
            list.add(sort);
        }
        Sort[] d = list.toArray(new Sort[0]);
        funcTranslation.put(gh.getName(), cvc5.declareFun(gh.getName(), d, ret));
    }

}
