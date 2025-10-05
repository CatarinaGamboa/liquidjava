package liquidjava.rj_language.opt.derivation_node;

import java.util.Map;

public class UnaryDerivationNode extends DerivationNode {

    private final ValDerivationNode operand;
    private final String operator;

    public UnaryDerivationNode(ValDerivationNode operand, String operator) {
        this.operand = operand;
        this.operator = operator;
    }

    public ValDerivationNode getOperand() {
        return operand;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> json = baseJson();
        json.put("op", operator);
        if (operand != null)
            json.put("operand", operand.toJson());
        return json;
    }
}
