package repair.regen.ast;

import java.util.List;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public class GroupExpression extends Expression {

    public GroupExpression(Expression e) {
        addChild(e);
    }

    public Expression getExpression() {
        return children.get(0);
    }

    @Override
    public Expr<?> eval(TranslatorToZ3 ctx) throws Exception {
        return getExpression().eval(ctx);
    }

    public String toString() {
        return "(" + getExpression().toString() + ")";
    }

    @Override
    public void getVariableNames(List<String> toAdd) {
        getExpression().getVariableNames(toAdd);
    }

    @Override
    public void getStateInvocations(List<String> toAdd, List<String> all) {
        getExpression().getStateInvocations(toAdd, all);
    }

    @Override
    public Expression clone() {
        return new GroupExpression(getExpression().clone());
    }

    @Override
    public boolean isBooleanTrue() {
        return getExpression().isBooleanTrue();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getExpression() == null) ? 0 : getExpression().hashCode());
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
        GroupExpression other = (GroupExpression) obj;
        if (getExpression() == null) {
            if (other.getExpression() != null)
                return false;
        } else if (!getExpression().equals(other.getExpression()))
            return false;
        return true;
    }

}
