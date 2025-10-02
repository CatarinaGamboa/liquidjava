package liquidjava.rj_language.opt.derivation_node;

import java.util.Map;

import liquidjava.rj_language.ast.Expression;

public class BinaryDerivationNode extends DerivationNode {
    private DerivationNode left;
    private DerivationNode right;
    private String op;

    public BinaryDerivationNode(Expression exp, DerivationNode left, DerivationNode right, String operator) {
        super(exp);
        this.left = left;
        this.right = right;
        this.op = operator;
    }

    public DerivationNode getLeft() {
        return left;
    }

    public DerivationNode getRight() {
        return right;
    }

    public String getOp() {
        return op;
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> json = baseJson();
        json.put("op", op);
        if (left != null) {
            json.put("left", left.toJson());
        }
        if (right != null) {
            json.put("right", right.toJson());
        }
        return json;
    }
}
