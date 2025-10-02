package liquidjava.rj_language.opt.derivation_node;

import java.util.Map;

import liquidjava.rj_language.ast.Expression;

public class UnaryDerivationNode extends DerivationNode {
    private DerivationNode operand;
    private String operator;

    public UnaryDerivationNode(Expression exp, DerivationNode operand, String operator) {
        super(exp);
        this.operator = operator;
    }

    public DerivationNode getOperand() {
        return operand;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> json = baseJson();
        json.put("op", operator);
        if (operand != null) {
            json.put("operand", operand.toJson());
        }
        return json;
    }
}
