package liquidjava.rj_language.opt.derivation_node;

public class UnaryDerivationNode extends DerivationNode {

    private final String op;
    private final ValDerivationNode operand;

    public UnaryDerivationNode(ValDerivationNode operand, String op) {
        this.operand = operand;
        this.op = op;
    }

    public ValDerivationNode getOperand() {
        return operand;
    }

    public String getOp() {
        return op;
    }
}
