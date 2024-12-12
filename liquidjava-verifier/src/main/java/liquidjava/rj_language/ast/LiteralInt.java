package liquidjava.rj_language.ast;

import com.microsoft.z3.Expr;
import java.util.List;
import liquidjava.smt.TranslatorToZ3;

public class LiteralInt extends Expression {

    private int value;

    public LiteralInt(int v) {
        value = v;
    }

    public LiteralInt(String v) {
        value = Integer.parseInt(v);
    }

    @Override
    public Expr<?> eval(TranslatorToZ3 ctx) {
        return ctx.makeIntegerLiteral(value);
    }

    public String toString() {
        return Integer.toString(value);
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
        return new LiteralInt(value);
    }

    @Override
    public boolean isBooleanTrue() {
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + value;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        LiteralInt other = (LiteralInt) obj;
        if (value != other.value) return false;
        return true;
    }
}
