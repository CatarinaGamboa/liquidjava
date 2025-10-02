package liquidjava.rj_language.opt.derivation_node;

import java.util.Map;

import liquidjava.rj_language.ast.Expression;

public class ValDerivationNode extends DerivationNode {

    public ValDerivationNode(Expression exp) {
        super(exp);
    }

    @Override
    public Map<String, Object> toJson() {
        return baseJson();
    }
}
