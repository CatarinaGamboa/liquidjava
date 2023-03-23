package liquidjava.rj_language.ast;

import liquidjava.rj_language.visitors.ExpressionVisitor;

import java.util.List;

public class SepUnit extends Expression {

    @Override
    public void accept(ExpressionVisitor v) throws Exception {
        v.visitUnit(this);
    }

    @Override
    public void getVariableNames(List<String> toAdd) {
    }

    @Override
    public void getStateInvocations(List<String> toAdd, List<String> all) {
    }

    @Override
    public boolean isBooleanTrue() {
        return false;
    }

    @Override
    public int hashCode() {
        return "sep.()".hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SepUnit;
    }

    @Override
    public Expression clone() {
        return new SepUnit();
    }

    @Override
    public String toString() {
        return "sep.()";
    }
}
