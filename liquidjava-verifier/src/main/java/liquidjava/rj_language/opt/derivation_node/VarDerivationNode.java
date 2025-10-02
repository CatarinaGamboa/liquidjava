package liquidjava.rj_language.opt.derivation_node;

import java.util.Map;

import liquidjava.rj_language.ast.Expression;

public class VarDerivationNode extends DerivationNode {

    private String var;

    public VarDerivationNode(Expression exp, String var) {
        super(exp);
        this.var = var;
    }

    public String getVar() {
        return var;
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> json = baseJson();
        json.put("var", var);
        return json;
    }
}
