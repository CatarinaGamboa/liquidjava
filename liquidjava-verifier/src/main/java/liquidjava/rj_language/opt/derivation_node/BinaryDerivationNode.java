package liquidjava.rj_language.opt.derivation_node;

import java.util.Map;

public class BinaryDerivationNode extends DerivationNode {

    private final ValDerivationNode left;
    private final ValDerivationNode right;
    private final String op;

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

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> json = baseJson();
        json.put("op", op);
        if (left != null)
            json.put("left", left.toJson());
        if (right != null)
            json.put("right", right.toJson());
        return json;
    }
}
