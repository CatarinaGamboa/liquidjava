package liquidjava.rj_language.opt;

import liquidjava.rj_language.ast.Expression;

public class DerivationNode {

    private DerivationNode from;
    private Expression exp;

    public DerivationNode(Expression exp) {
        this.exp = exp;
    }

    public DerivationNode getFrom() {
        return from;
    }

    public void setFrom(DerivationNode from) {
        this.from = from;
    }

    public DerivationNode addNode(Expression exp) {
        DerivationNode newNode = new DerivationNode(exp);
        newNode.setFrom(this);
        return newNode;
    }

    @Override
    public String toString() {
        return String.format("%s <=\n%s", exp, from);
    }
}
