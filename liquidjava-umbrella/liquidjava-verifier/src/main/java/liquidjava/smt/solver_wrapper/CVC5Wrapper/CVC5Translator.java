package liquidjava.smt.solver_wrapper.CVC5Wrapper;

import com.martiansoftware.jsap.SyntaxException;
import io.github.cvc5.Solver;
import io.github.cvc5.Sort;
import liquidjava.processor.context.*;
import liquidjava.processor.context.Context;
import liquidjava.rj_language.ast.*;
import liquidjava.rj_language.visitors.AbstractExpressionVisitor;
import liquidjava.smt.NotFoundError;
import spoon.reflect.reference.CtTypeReference;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.github.cvc5.*;

import static io.github.cvc5.RoundingMode.ROUND_NEAREST_TIES_TO_EVEN;

public class CVC5Translator extends AbstractExpressionVisitor {

    static public final String pointeeSingletonName = "sep.()";

    protected final Map<String, Term> varTranslation = new HashMap<>();
    protected final Map<String, List<Term>> varSuperTypes = new HashMap<>();
    protected final Map<String, AliasWrapper> aliasTranslation = new HashMap<>();
    protected final Map<String, Term> funcTranslation = new HashMap<>();

    protected final io.github.cvc5.Solver cvc5 = new io.github.cvc5.Solver();
    protected final Term rounding_mod = cvc5.mkRoundingMode(ROUND_NEAREST_TIES_TO_EVEN);

    protected final Map<String, Sort> sortMap = Stream
            .of(new Pair<>("void", cvc5.mkUninterpretedSort("void")), new Pair<>("int", cvc5.getIntegerSort()),
                    new Pair<>("long", cvc5.getRealSort()), new Pair<>("boolean", cvc5.getBooleanSort()),
                    new Pair<>("float", cvc5.mkFloatingPointSort(8, 24)),
                    new Pair<>("double", cvc5.mkFloatingPointSort(11, 53)),
                    new Pair<>("int[]", cvc5.mkArraySort(cvc5.getIntegerSort(), cvc5.getIntegerSort())),
                    new Pair<>("String", cvc5.getStringSort()), new Pair<>("Loc", cvc5.declareSort("Loc", 0)),
                    new Pair<>("Data", cvc5.declareSort("Data", 0)))
            .collect(Collectors.toMap(p -> p.first, p -> p.second));

    protected final Map<String, Kind> opmap = Stream.of(new Pair<>("&&", Kind.AND), new Pair<>("||", Kind.OR),
            new Pair<>("-->", Kind.IMPLIES), new Pair<>("==", Kind.EQUAL), new Pair<>(">=", Kind.GEQ),
            new Pair<>(">", Kind.GT), new Pair<>("<=", Kind.LEQ), new Pair<>("<", Kind.LT), new Pair<>("+", Kind.ADD),
            new Pair<>("-", Kind.SUB), new Pair<>("*", Kind.MULT), new Pair<>("/", Kind.INTS_DIVISION),
            new Pair<>("%", Kind.INTS_MODULUS), new Pair<>("|*", Kind.SEP_STAR), new Pair<>("|->", Kind.SEP_PTO))
            .collect(Collectors.toMap(p -> p.first, p -> p.second));

    protected final Map<String, Kind> fp_rm_map = Stream
            .of(new Pair<>("+", Kind.FLOATINGPOINT_ADD), new Pair<>("-", Kind.FLOATINGPOINT_SUB),
                    new Pair<>("*", Kind.FLOATINGPOINT_MULT), new Pair<>("/", Kind.FLOATINGPOINT_DIV))
            .collect(Collectors.toMap(p -> p.first, p -> p.second));

    protected final Map<String, Kind> fp_map = Stream.of(new Pair<>("%", Kind.FLOATINGPOINT_REM),
            new Pair<>("==", Kind.FLOATINGPOINT_EQ), new Pair<>(">=", Kind.FLOATINGPOINT_GEQ),
            new Pair<>(">", Kind.FLOATINGPOINT_GT), new Pair<>("<=", Kind.FLOATINGPOINT_LEQ),
            new Pair<>("<", Kind.FLOATINGPOINT_LT), new Pair<>("+", Kind.FLOATINGPOINT_ADD))
            .collect(Collectors.toMap(p -> p.first, p -> p.second));
    private Term result;

    CVC5Translator(Context c) throws CVC5ApiException {
        ContextTranslator ct = new ContextTranslator(cvc5, sortMap);
        ct.translateVariables(c.getContext(), varTranslation);

        // constant for sep.()
        varTranslation.put(CVC5Translator.pointeeSingletonName, cvc5.mkConst(getPointeeSort()));

        ct.storeVariablesSubtypes(c.getAllVariablesWithSupertypes(), varSuperTypes);
        ContextTranslator.addAlias(c.getAlias(), aliasTranslation);
        ct.addGhostFunctions(c.getGhosts(), funcTranslation);
        ct.addGhostStates(c.getGhostState(), funcTranslation);
    }

    Sort getSort(String sort) {
        if (!sortMap.containsKey(sort)) {
            sortMap.put(sort, cvc5.mkUninterpretedSort(sort));
            System.out.println("Making new uninterpreted sort: " + sort);
        }

        return sortMap.get(sort);
    }

    Sort getPointerSort() {
        return getSort("Loc");
    }

    Sort getPointeeSort() {
        return getSort("Data");
    }

    protected Term getVariableTranslation(String name) throws Exception {
        if (!varTranslation.containsKey(name))
            throw new NotFoundError("Variable '" + name + "' not found");
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

    private Term toFP(Term t) throws CVC5ApiException {
        // System.out.println("> Converting " + t + " with " + t.getSort() + " and " + t.getKind() + " to floating
        // point");
        if (t.getSort().isFloatingPoint()) {
            // System.out.println("< Nothing to do");
            return t;
        }
        // System.out.println("Constructing real to floating point");
        Op real2fp = cvc5.mkOp(Kind.FLOATINGPOINT_TO_FP_FROM_REAL, 11, 53);
        if (t.getSort().isInteger()) {
            Term res = cvc5.mkTerm(real2fp, rounding_mod, toReal(t));
            // System.out.println("< Constructed invocation of real2fp on integer");
            return res;
        }
        if (t.getSort().isReal()) {
            Term res = cvc5.mkTerm(real2fp, rounding_mod, t);
            // System.out.println("< Constructed invocation of real2fp on real");
            return res;
        }
        throw new RuntimeException(
                "Expected int, real or fp. Got " + t + " of sort " + t.getSort() + " and of kind " + t.getKind());
    }

    private Term toReal(Term t) throws CVC5ApiException {
        if (t.getSort().isReal()) {
            return t;
        }

        if (t.getSort().isInteger()) {
            return cvc5.mkTerm(cvc5.mkOp(Kind.TO_REAL), t);
        }

        throw new RuntimeException(
                "Expected int or real. Got " + t + " of sort " + t.getSort() + " and of kind " + t.getKind());
    }

    private boolean isUnifiable(Term t) {
        return t.getSort().isInteger() || t.getSort().isFloatingPoint() || t.getSort().isInteger()
                || t.getSort().isReal();
    }

    private Pair<Term, Term> unifyTypes(Term left, Term right) throws CVC5ApiException {
        if (Objects.equals(left.getSort(), right.getSort())) {
            return new Pair<>(left, right);
        }
        if (!isUnifiable(right) || !isUnifiable(right)) {
            return new Pair<>(left, right);
        }

        if (left.getSort().isFloatingPoint() || right.getSort().isFloatingPoint()) {
            return new Pair<>(toFP(left), toFP(right));
        }

        if (left.getSort().isReal() || right.getSort().isReal()) {
            return new Pair<>(toReal(left), toReal(right));
        }

        throw new RuntimeException("Can unify only int, real, and fp! "
                + String.format("Got left: %s with sort %s and kind %s, right: %s with sort %s and kind %s", left,
                        left.getSort(), left.getKind(), right, right.getSort(), right.getKind()));
    }

    public Term makeFunctionInvocation(String name, Term[] params) {
        if (name.equals("addToIndex"))
            return makeStore(name, params);
        if (name.equals("getFromIndex"))
            return makeSelect(name, params);

        Term fd = funcTranslation.get(name);
        Sort funcSort = fd.getSort();
        Sort[] argSorts = funcSort.getFunctionDomainSorts();

        for (int i = 0; i < argSorts.length; ++i) {
            Term param = params[i];
            Sort paramSort = param.getSort();
            if (Objects.equals(argSorts[i], paramSort)) {
                continue;
            }
            List<Term> le = varSuperTypes.get(param.toString().replace("|", ""));

            if (le == null) {
                continue;
            }
            for (Term t : le) {
                if (Objects.equals(t.getSort(), argSorts[i])) {
                    params[i] = t;
                }
            }
        }

        Term[] invocation = new Term[params.length + 1];
        System.arraycopy(params, 0, invocation, 1, params.length);
        invocation[0] = fd;

        return cvc5.mkTerm(cvc5.mkOp(Kind.APPLY_UF), invocation);
    }

    public Term makeSelect(String name, Term[] params) {
        if (params.length == 2 && params[0].getSort() == getSort("int[]")) {
            Op selectOp = cvc5.mkOp(Kind.SELECT);
            return cvc5.mkTerm(selectOp, params);
        }
        return null;
    }

    public Term makeStore(String name, Term[] params) {
        if (params.length == 3 && params[0].getSort() == getSort("int[]")) {
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
        // System.out.println(">> Visiting binary expression: " + be);
        be.getFirstOperand().accept(this);
        Term left = result;
        be.getSecondOperand().accept(this);
        Term right = result;
        {
            Pair<Term, Term> u = unifyTypes(left, right);
            left = u.first;
            right = u.second;
        }

        String op = Objects.equals(be.getOperator(), "!=") ? "==" : be.getOperator();

        if (!opmap.containsKey(op)) {
            throw new RuntimeException("unrecognized operation: `" + op + "`");
        }

        if (left.getSort().isFloatingPoint() && fp_rm_map.containsKey(op)) {
            result = cvc5.mkTerm(fp_rm_map.get(op), rounding_mod, left, right);
        } else if (left.getSort().isFloatingPoint() && fp_map.containsKey(op)) {
            result = cvc5.mkTerm(fp_map.get(op), left, right);
        } else {
            result = cvc5.mkTerm(opmap.get(op), left, right);
        }

        if (Objects.equals(be.getOperator(), "!=")) {
            result = cvc5.mkTerm(Kind.NOT, result);
        }
        // System.out.println("<< Result of visiting binary expression: " + result);
    }

    @Override
    public void visitFunctionInvocation(FunctionInvocation fi) throws Exception {
        Term[] argsExpr = new Term[fi.getArgs().size()];
        for (int i = 0; i < argsExpr.length; i++) {
            fi.getArgs().get(i).accept(this);
            // System.out.println(">- Argument: `" + this.result + "`");
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

        if (!c.getSort().isBoolean()) {
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
    public void visitLiteralReal(LiteralReal lr) throws CVC5ApiException {
        result = toFP(cvc5.mkReal(String.valueOf(lr.getValue())));
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
            if (e.getSort().isFloatingPoint()) {
                result = cvc5.mkTerm(Kind.FLOATINGPOINT_NEG, e);
            } else {
                result = cvc5.mkTerm(Kind.NEG, e);
            }
            break;
        case "!":
            result = cvc5.mkTerm(Kind.NOT, e);
            break;
        }

    }

    @Override
    public void visitVar(Var v) throws Exception {
        result = makeVariable(v.getName());
    }

    @Override
    public void visitUnit(SepUnit unit) throws Exception {
        result = makeVariable(CVC5Translator.pointeeSingletonName);
    }

    @Override
    public void visitSepEmp(SepEmp sepEmp) throws Exception {
        result = cvc5.mkSepEmp();
    }

    public Term getResult() {
        return result;
    }

    public Solver getSolver() {
        return cvc5;
    }
}

class ContextTranslator {

    final io.github.cvc5.Solver cvc5;
    final Map<String, Sort> sortMap;

    public ContextTranslator(Solver cvc5, Map<String, Sort> sortMap) {
        this.cvc5 = cvc5;
        this.sortMap = sortMap;
    }

    void translateVariables(Map<String, CtTypeReference<?>> ctx, Map<String, Term> varTranslation) {

        for (String name : ctx.keySet())
            varTranslation.put(name, getExpr(name, ctx.get(name)));

        varTranslation.put("true", cvc5.mkBoolean(true));
        varTranslation.put("false", cvc5.mkBoolean(false));

    }

    public void storeVariablesSubtypes(List<RefinedVariable> variables, Map<String, List<Term>> varSuperTypes) {
        for (RefinedVariable v : variables) {
            if (!v.getSuperTypes().isEmpty()) {
                ArrayList<Term> a = new ArrayList<>();
                for (CtTypeReference<?> ctr : v.getSuperTypes())
                    a.add(getExpr(v.getName(), ctr));
                varSuperTypes.put(v.getName(), a);
            }
        }

    }

    private Term getExpr(String name, CtTypeReference<?> type) {
        String typeName = type.getQualifiedName();
        Sort sort = getSort(typeName);
        return cvc5.mkConst(sort, name);

    }

    static void addAlias(List<AliasWrapper> alias, Map<String, AliasWrapper> aliasTranslation) {
        for (AliasWrapper a : alias) {
            aliasTranslation.put(a.getName(), a);
        }
    }

    public void addGhostFunctions(List<GhostFunction> ghosts, Map<String, Term> funcTranslation) {
        addBuiltinFunctions(funcTranslation);
        if (!ghosts.isEmpty()) {
            for (GhostFunction gh : ghosts) {
                addGhostFunction(gh, funcTranslation);
            }
        }
    }

    private void addBuiltinFunctions(Map<String, Term> funcTranslation) {
        Term lenfun = cvc5.declareFun("length", Stream.of(getSort("int[]")).toArray(Sort[]::new), getSort("int"));
        funcTranslation.put("length", lenfun);// ERRRRRRRRRRRRO!!!!!!!!!!!!!
        // System.out.println("\nWorks only for int[] now! Change in future. Ignore this message, it is a glorified
        // todo");
        // TODO add built-in function
        Sort[] s = Stream.of(getSort("int[]"), getSort("int"), getSort("int")).toArray(Sort[]::new);
        funcTranslation.put("addToIndex", cvc5.declareFun("addToIndex", s, getSort("void")));

        s = Stream.of(getSort("int[]"), getSort("int")).toArray(Sort[]::new);
        funcTranslation.put("getFromIndex", cvc5.declareFun("getFromIndex", s, getSort("int")));

    }

    Sort getSort(String sort) {

        if (!sortMap.containsKey(sort)) {
            sortMap.put(sort, cvc5.mkUninterpretedSort(sort));
        }

        return sortMap.get(sort);
    }

    public void addGhostStates(List<GhostState> ghostState, Map<String, Term> funcTranslation) {
        for (GhostState g : ghostState) {
            addGhostFunction(g, funcTranslation);
        }

    }

    private void addGhostFunction(GhostFunction gh, Map<String, Term> funcTranslation) {
        List<CtTypeReference<?>> paramTypes = gh.getParametersTypes();
        Sort ret = getSort(gh.getReturnType().toString());
        List<Sort> list = new ArrayList<>();
        for (CtTypeReference<?> paramType : paramTypes) {
            String t = paramType.toString();
            Sort sort = getSort(t);
            list.add(sort);
        }
        Sort[] d = list.toArray(new Sort[0]);
        Term dclrd_func = cvc5.declareFun(gh.getName(), d, ret);
        // System.out.println("Adding function: " + dclrd_func);
        funcTranslation.put(gh.getName(), dclrd_func);
    }

}
