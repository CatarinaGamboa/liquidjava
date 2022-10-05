package liquidjava.rj_language.ast;

import java.util.List;

import com.microsoft.z3.Expr;

import liquidjava.smt.TranslatorToZ3;

public class LiteralBoolean extends Expression {

    boolean value;

    public LiteralBoolean(boolean value) {
        this.value = value;
    }

    public LiteralBoolean(String value) {
        this.value = Boolean.parseBoolean(value);
    }

    public Expr<?> eval(TranslatorToZ3 ctx) {
        return ctx.makeBooleanLiteral(value);
    }

    public String toString() {
        return Boolean.toString(value);
    }

    @Override
    public void getVariableNames(List<String> toAdd) {
        // end leaf
    }

    @Override
    public void getStateInvocations(List<String> toAdd, List<String> all) {
        // end leaf

    }

    @Override
    public Expression clone() {
        return new LiteralBoolean(value);
    }

    @Override
    public boolean isBooleanTrue() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (value ? 1231 : 1237);
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
        LiteralBoolean other = (LiteralBoolean) obj;
        if (value != other.value)
            return false;
        return true;
    }

}
