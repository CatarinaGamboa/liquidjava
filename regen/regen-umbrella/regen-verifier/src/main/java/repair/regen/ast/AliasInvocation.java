package repair.regen.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public class AliasInvocation extends Expression {
    String name;

    public AliasInvocation(String name, List<Expression> args) {
        this.name = name;
        for (Expression e : args)
            addChild(e);
    }

    public String getName() {
        return name;
    }

    public List<Expression> getArgs() {
        return children;
    }

    @Override
    public Expr eval(TranslatorToZ3 ctx) throws Exception {
        Expr[] argsExpr = new Expr[getArgs().size()];
        for (int i = 0; i < argsExpr.length; i++) {
            argsExpr[i] = getArgs().get(i).eval(ctx);
        }
        return ctx.makeFunctionInvocation(name, argsExpr);
    }

    @Override
    public String toString() {
        return name + "(" + getArgs().stream().map(p -> p.toString()).collect(Collectors.joining(", ")) + ")";
    }

    @Override
    public void getVariableNames(List<String> toAdd) {
        for (Expression e : getArgs())
            e.getVariableNames(toAdd);

    }

    @Override
    public void getStateInvocations(List<String> toAdd, List<String> all) {
        for (Expression e : getArgs())
            e.getStateInvocations(toAdd, all);

    }

    @Override
    public Expression clone() {
        List<Expression> le = new ArrayList<>();
        for (Expression e : getArgs())
            le.add(e.clone());
        return new AliasInvocation(name, le);
    }

    @Override
    public boolean isBooleanTrue() {
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getArgs() == null) ? 0 : getArgs().hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AliasInvocation other = (AliasInvocation) obj;
        if (getArgs() == null) {
            if (other.getArgs() != null)
                return false;
        } else if (!getArgs().equals(other.getArgs()))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
