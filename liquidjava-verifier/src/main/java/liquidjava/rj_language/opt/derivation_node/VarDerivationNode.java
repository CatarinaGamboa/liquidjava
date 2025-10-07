package liquidjava.rj_language.opt.derivation_node;

public class VarDerivationNode extends DerivationNode {

    private final String var;

    public VarDerivationNode(String var) {
        this.var = var;
    }

    public String getVar() {
        return var;
    }
}
