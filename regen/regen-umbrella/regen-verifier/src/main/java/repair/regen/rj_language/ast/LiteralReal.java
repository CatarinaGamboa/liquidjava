package repair.regen.rj_language.ast;

import java.util.List;

import com.microsoft.z3.Expr;

import repair.regen.smt.TranslatorToZ3;

public class LiteralReal extends Expression {

    private double value;

    public LiteralReal(double v) {
        value = v;
    }

    public LiteralReal(String v) {
        value = Double.parseDouble(v);
    }

    @Override
    public Expr<?> eval(TranslatorToZ3 ctx) {
        return ctx.makeDoubleLiteral(value);
    }

    public String toString() {
        return Double.toString(value);
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
        return new LiteralReal(value);
    }

    @Override
    public boolean isBooleanTrue() {
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(value);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        LiteralReal other = (LiteralReal) obj;
        if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
            return false;
        return true;
    }
}
