package liquidjava.rj_language.opt.derivation_node;

import java.util.Map;

public class VarDerivationNode extends DerivationNode {

    private final String var;

    public VarDerivationNode(String var) {
        this.var = var;
    }

    public String getVar() {
        return var;
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> json = baseJson();
        if (var != null)
            json.put("var", var);
        return json;
    }
}
