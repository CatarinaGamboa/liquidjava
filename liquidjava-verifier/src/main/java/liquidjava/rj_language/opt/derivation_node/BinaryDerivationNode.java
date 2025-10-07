package liquidjava.rj_language.opt.derivation_node;

public class BinaryDerivationNode extends DerivationNode {

    private final String op;
    private final ValDerivationNode left;
    private final ValDerivationNode right;

    public BinaryDerivationNode(ValDerivationNode left, ValDerivationNode right, String op) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public ValDerivationNode getLeft() {
        return left;
    }

    public ValDerivationNode getRight() {
        return right;
    }

    public String getOp() {
        return op;
    }
}
