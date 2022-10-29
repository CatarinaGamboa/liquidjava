package liquidjava.smt.solver_wrapper;

import com.martiansoftware.jsap.SyntaxException;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FuncDecl;
import liquidjava.processor.context.AliasWrapper;
import liquidjava.processor.context.Context;
import liquidjava.smt.NotFoundError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SMTWrapper {
    protected final Map<String, ExprWrapper> varTranslation = new HashMap<>();
    protected final Map<String, List<ExprWrapper>> varSuperTypes = new HashMap<>();
    protected final Map<String, AliasWrapper> aliasTranslation = new HashMap<>();
    protected final Map<String, FuncDecl<?>> funcTranslation = new HashMap<>();

    public static SMTWrapper getZ3(Context c) {
        return new TranslatorToZ3(c);
    }

    public static SMTWrapper getCVC4(Context c) {
        return new TranslatorToCVC5(c);
    }

    protected ExprWrapper getVariableTranslation(String name) throws Exception {
        if (!varTranslation.containsKey(name))
            throw new NotFoundError("Variable '" + name.toString() + "' not found");
        ExprWrapper e = varTranslation.get(name);
        if (e == null)
            e = varTranslation.get(String.format("this#%s", name));
        if (e == null)
            throw new SyntaxException("Unknown variable:" + name);
        return e;
    }

    public ExprWrapper makeVariable(String name) throws Exception {
        return getVariableTranslation(name);// int[] not in varTranslation
    }

    abstract public Status verifyExpression(ExprWrapper e) throws Exception;

    // #####################Literals and Variables#####################

    abstract public ExprWrapper makeIntegerLiteral(int value);

    abstract public ExprWrapper makeLongLiteral(long value);

    abstract public ExprWrapper makeDoubleLiteral(double value);

    abstract public ExprWrapper makeString(String s);

    abstract public ExprWrapper makeBooleanLiteral(boolean value);

    abstract public ExprWrapper makeFunctionInvocation(String name, ExprWrapper[] params) throws Exception;

    abstract public ExprWrapper makeSelect(String name, ExprWrapper[] params);

    abstract public ExprWrapper makeStore(String name, ExprWrapper[] params);

    // #####################Boolean Operations#####################

    abstract public ExprWrapper makeEquals(ExprWrapper e1, ExprWrapper e2);

    abstract public ExprWrapper makeLt(ExprWrapper e1, ExprWrapper e2);

    abstract public ExprWrapper makeLtEq(ExprWrapper e1, ExprWrapper e2);

    abstract public ExprWrapper makeGt(ExprWrapper e1, ExprWrapper e2);

    abstract public ExprWrapper makeGtEq(ExprWrapper e1, ExprWrapper e2);

    abstract public ExprWrapper makeImplies(ExprWrapper e1, ExprWrapper e2);

    abstract public ExprWrapper makeBiconditional(ExprWrapper eval, ExprWrapper eval2);

    abstract public ExprWrapper makeAnd(ExprWrapper eval, ExprWrapper eval2);

    abstract public ExprWrapper mkNot(ExprWrapper e1);

    abstract public ExprWrapper makeOr(ExprWrapper eval, ExprWrapper eval2);

    // ##################### Unary Operations #####################

    abstract public ExprWrapper makeMinus(ExprWrapper eval);

    // #####################Arithmetic Operations#####################

    abstract public ExprWrapper makeAdd(ExprWrapper eval, ExprWrapper eval2);

    abstract public ExprWrapper makeSub(ExprWrapper eval, ExprWrapper eval2);

    abstract public ExprWrapper makeMul(ExprWrapper eval, ExprWrapper eval2);

    abstract public ExprWrapper makeDiv(ExprWrapper eval, ExprWrapper eval2);

    abstract public ExprWrapper makeMod(ExprWrapper eval, ExprWrapper eval2);

    abstract public ExprWrapper makeIte(ExprWrapper c, ExprWrapper t, ExprWrapper e);
}
