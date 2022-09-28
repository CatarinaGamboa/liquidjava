package repair.regen.ast;

import java.util.List;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public class Ite extends Expression {

    public Ite(Expression e1, Expression e2, Expression e3) {
        addChild(e1);
        addChild(e2);
        addChild(e3);
    }

    public Expression getCondition() {
        return children.get(0);
    }

    public Expression getThen() {
        return children.get(1);
    }

    public Expression getElse() {
        return children.get(2);
    }

    @Override
    public Expr eval(TranslatorToZ3 ctx) throws Exception {
        return ctx.makeIte(getCondition().eval(ctx), getThen().eval(ctx), getElse().eval(ctx));
    }

    @Override
    public String toString() {
        return getCondition().toString() + "?" + getThen().toString() + ":" + getElse().toString();
    }

    @Override
    public void getVariableNames(List<String> toAdd) {
        getCondition().getVariableNames(toAdd);
        getThen().getVariableNames(toAdd);
        getElse().getVariableNames(toAdd);
    }

    @Override
    public void getStateInvocations(List<String> toAdd, List<String> all) {
        getCondition().getStateInvocations(toAdd, all);
        getThen().getStateInvocations(toAdd, all);
        getElse().getStateInvocations(toAdd, all);
    }

    @Override
    public Expression clone() {
        return new Ite(getCondition().clone(), getThen().clone(), getElse().clone());
    }

    @Override
    public boolean isBooleanTrue() {
        return getCondition().isBooleanTrue() && getThen().isBooleanTrue() && getElse().isBooleanTrue();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCondition() == null) ? 0 : getCondition().hashCode());
        result = prime * result + ((getElse() == null) ? 0 : getElse().hashCode());
        result = prime * result + ((getThen() == null) ? 0 : getThen().hashCode());
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
        Ite other = (Ite) obj;
        if (getCondition() == null) {
            if (other.getCondition() != null)
                return false;
        } else if (!getCondition().equals(other.getCondition()))
            return false;
        if (getElse() == null) {
            if (other.getElse() != null)
                return false;
        } else if (!getElse().equals(other.getElse()))
            return false;
        if (getThen() == null) {
            if (other.getThen() != null)
                return false;
        } else if (!getThen().equals(other.getThen()))
            return false;
        return true;
    }

}
