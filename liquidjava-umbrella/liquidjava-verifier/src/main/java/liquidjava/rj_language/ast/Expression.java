package liquidjava.rj_language.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import liquidjava.processor.context.Context;
import liquidjava.processor.facade.AliasDTO;
import liquidjava.rj_language.ast.typing.TypeInfer;
import liquidjava.rj_language.visitors.ExpressionVisitor;
import liquidjava.smt.solver_wrapper.ExprWrapper;
import liquidjava.smt.solver_wrapper.SMTWrapper;
import spoon.reflect.factory.Factory;

public abstract class Expression {

    public abstract void accept(ExpressionVisitor v);

    public abstract ExprWrapper eval(SMTWrapper ctx) throws Exception;

    public abstract void getVariableNames(List<String> toAdd);

    public abstract void getStateInvocations(List<String> toAdd, List<String> all);

    public abstract boolean isBooleanTrue();

    public abstract int hashCode();

    public abstract boolean equals(Object obj);

    public abstract Expression clone();

    public abstract String toString();

    List<Expression> children = new ArrayList<>();

    public void addChild(Expression e) {
        children.add(e);
    }

    public List<Expression> getChildren() {
        return children;
    }

    public boolean hasChildren() {
        return children.size() > 0;
    }

    public void setChild(int index, Expression element) {
        children.set(index, element);
    }

    /**
     * Substitutes the expression first given expression by the second
     *
     * @param from
     * @param to
     *
     * @return
     */
    public Expression substitute(Expression from, Expression to) {
        Expression e = clone();
        if (this.equals(from))
            e = to;
        e.auxSubstitute(from, to);
        return e;
    }

    private void auxSubstitute(Expression from, Expression to) {
        if (hasChildren()) {
            for (int i = 0; i < children.size(); i++) {
                Expression exp = children.get(i);
                if (exp.equals(from))
                    setChild(i, to);
                exp.auxSubstitute(from, to);
            }
        }
    }

    /**
     * Substitutes the function call with the given parameter to the expression e
     *
     */
    public void substituteFunction(String functionName, List<Expression> parameters, Expression sub) {
        if (hasChildren())
            for (int i = 0; i < children.size(); i++) {
                Expression exp = children.get(i);
                if (exp instanceof FunctionInvocation) {
                    FunctionInvocation fi = (FunctionInvocation) exp;
                    if (fi.name.equals(functionName) && fi.argumentsEqual(parameters)) {
                        // substitute by sub in parent
                        setChild(i, sub);
                    }

                }
                exp.substituteFunction(functionName, parameters, sub);
            }
    }

    public Expression substituteState(Map<String, Expression> subMap, String[] toChange) {
        Expression e = clone();
        if (this instanceof FunctionInvocation) {
            FunctionInvocation fi = (FunctionInvocation) this;
            if (subMap.containsKey(fi.name) && fi.children.size() == 1 && fi.children.get(0) instanceof Var) {// object
                                                                                                              // state
                Var v = (Var) fi.children.get(0);
                Expression sub = subMap.get(fi.name).clone();
                for (String s : toChange) {
                    sub = sub.substitute(new Var(s), v);
                }
                // substitute by sub in parent
                e = new GroupExpression(sub);
            }
        }
        e.auxSubstituteState(subMap, toChange);
        return e;
    }

    private void auxSubstituteState(Map<String, Expression> subMap, String[] toChange) {
        if (hasChildren()) {
            for (int i = 0; i < children.size(); i++) {
                Expression exp = children.get(i);
                if (exp instanceof FunctionInvocation) {
                    FunctionInvocation fi = (FunctionInvocation) exp;
                    if (subMap.containsKey(fi.name) && fi.children.size() == 1 && fi.children.get(0) instanceof Var) {// object
                                                                                                                      // state
                        Var v = (Var) fi.children.get(0);
                        Expression sub = subMap.get(fi.name).clone();
                        for (String s : toChange) {
                            sub = sub.substitute(new Var(s), v);
                        }
                        // substitute by sub in parent
                        setChild(i, (sub instanceof GroupExpression) ? sub : new GroupExpression(sub));
                    }

                }
                exp.auxSubstituteState(subMap, toChange);
            }
        }

    }

    public Expression changeAlias(Map<String, AliasDTO> alias, Context ctx, Factory f) throws Exception {
        Expression e = clone();
        if (this instanceof AliasInvocation) {
            AliasInvocation ai = (AliasInvocation) this;
            if (alias.containsKey(ai.name)) {// object state
                AliasDTO dto = alias.get(ai.name);
                Expression sub = dto.getExpression().clone();
                for (int i = 0; i < children.size(); i++) {
                    Expression varExp = new Var(dto.getVarNames().get(i));
                    String varType = dto.getVarTypes().get(i);
                    Expression aliasExp = children.get(i);

                    boolean checks = TypeInfer.checkCompatibleType(varType, aliasExp, ctx, f);
                    if (!checks)
                        throw new Exception("Type Mismatch: Cannoy substitute " + aliasExp + " : "
                                + TypeInfer.getType(ctx, f, aliasExp).get().getQualifiedName() + " by " + varExp + " : "
                                + TypeInfer.getType(ctx, f, varExp).get().getQualifiedName());

                    sub = sub.substitute(varExp, aliasExp);
                }
                e = new GroupExpression(sub);
            }
        }
        e.auxChangeAlias(alias, ctx, f);
        return e;
    }

    private void auxChangeAlias(Map<String, AliasDTO> alias, Context ctx, Factory f) throws Exception {
        if (hasChildren())
            for (int i = 0; i < children.size(); i++) {
                if (children.get(i) instanceof AliasInvocation) {
                    AliasInvocation ai = (AliasInvocation) children.get(i);
                    if (!alias.containsKey(ai.name))
                        throw new Exception("Alias '" + ai.getName() + "' not found");
                    AliasDTO dto = alias.get(ai.name);
                    Expression sub = dto.getExpression().clone();
                    if (ai.hasChildren())
                        for (int j = 0; j < ai.children.size(); j++) {
                            Expression varExp = new Var(dto.getVarNames().get(j));
                            String varType = dto.getVarTypes().get(j);
                            Expression aliasExp = ai.children.get(j);

                            boolean checks = TypeInfer.checkCompatibleType(varType, aliasExp, ctx, f);
                            if (!checks)
                                throw new Exception(
                                        "Type Mismatch: Cannot substitute " + varExp + ":" + varType + " by " + aliasExp
                                                + ":" + TypeInfer.getType(ctx, f, aliasExp).get().getQualifiedName()
                                                + " in alias '" + ai.name + "'");

                            sub = sub.substitute(varExp, aliasExp);
                        }
                    setChild(i, sub);
                }
                children.get(i).auxChangeAlias(alias, ctx, f);
            }

    }

}