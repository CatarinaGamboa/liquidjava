package liquidjava.rj_language.ast;

import liquidjava.rj_language.visitors.ExpressionVisitor;

import java.util.List;

public class SepEmp extends Expression {

    @Override
    public void accept(ExpressionVisitor v) throws Exception {
        v.visitSepEmp(this);
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
        return "sep.emp".hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SepEmp;
    }

    @Override
    public Expression clone() {
        return new SepEmp();
    }

    @Override
    public String toString() {
        return "sep.emp";
    }
}
