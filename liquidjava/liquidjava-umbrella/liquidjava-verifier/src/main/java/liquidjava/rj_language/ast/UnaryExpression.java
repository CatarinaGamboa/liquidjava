package liquidjava.rj_language.ast;

import java.util.List;

import com.microsoft.z3.Expr;

import liquidjava.smt.TranslatorToZ3;

public class UnaryExpression extends Expression {

    private String op;

    public UnaryExpression(String op, Expression e) {
        this.op = op;
        addChild(e);
    }

    public Expression getExpression() {
        return children.get(0);
    }

    public String getOp() {
        return op;
    }

    @Override
    public Expr<?> eval(TranslatorToZ3 ctx) throws Exception {
        switch (op) {
        case "-":
            return ctx.makeMinus(getExpression().eval(ctx));
        case "!":
            return ctx.mkNot(getExpression().eval(ctx));
        }
        return null;
    }

    @Override
    public String toString() {
        return op + getExpression().toString();
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
        return new UnaryExpression(op, getExpression().clone());
    }

    @Override
    public boolean isBooleanTrue() {
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getExpression() == null) ? 0 : getExpression().hashCode());
        result = prime * result + ((op == null) ? 0 : op.hashCode());
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
        UnaryExpression other = (UnaryExpression) obj;
        if (getExpression() == null) {
            if (other.getExpression() != null)
                return false;
        } else if (!getExpression().equals(other.getExpression()))
            return false;
        if (op == null) {
            if (other.op != null)
                return false;
        } else if (!op.equals(other.op))
            return false;
        return true;
    }

}
