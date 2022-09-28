package repair.regen.processor.constraints;

import java.util.List;

import repair.regen.ast.BinaryExpression;
import repair.regen.ast.Expression;
import repair.regen.errors.ErrorEmitter;
import repair.regen.processor.context.GhostState;

public class Implication extends Constraint {

    private Constraint c1;
    private Constraint c2;

    public Implication(Constraint c1, Constraint c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    @Override
    public Constraint substituteVariable(String from, String to) {
        return new Implication(c1.substituteVariable(from, to), c2.substituteVariable(from, to));
    }

    @Override
    public Constraint clone() {
        return new Implication(c1.clone(), c2.clone());
    }

    @Override
    public List<String> getVariableNames() {
        List<String> l1 = c1.getVariableNames();
        l1.addAll(c2.getVariableNames());
        return l1;
    }

    @Override
    public String toString() {
        return "(" + c1.toString() + " --> " + c2.toString() + ")";
    }

    @Override
    public Expression getExpression() {
        return new BinaryExpression(c1.getExpression(), "->", c2.getExpression());
    }

    @Override
    public boolean isBooleanTrue() {
        return c1.isBooleanTrue() && c2.isBooleanTrue();
    }

    @Override
    public Constraint changeOldMentions(String previousName, String newName, ErrorEmitter ee) {
        Constraint c1_ = c1.changeOldMentions(previousName, newName, ee);
        Constraint c2_ = c2.changeOldMentions(previousName, newName, ee);
        return new Implication(c1_, c2_);
    }

    @Override
    public Constraint changeStatesToRefinements(List<GhostState> ghostState, String[] ls, ErrorEmitter ee) {
        return new Implication(c1.changeStatesToRefinements(ghostState, ls, ee),
                c2.changeStatesToRefinements(ghostState, ls, ee));
    }
}
