package liquidjava.rj_language.opt.derivation_node;

import java.util.Map;

import liquidjava.rj_language.ast.Expression;
import liquidjava.rj_language.ast.LiteralBoolean;
import liquidjava.rj_language.ast.LiteralInt;
import liquidjava.rj_language.ast.LiteralReal;
import liquidjava.rj_language.ast.Var;

public class ValDerivationNode extends DerivationNode {

    private final Expression value;
    private final DerivationNode origin;

    public ValDerivationNode(Expression exp, DerivationNode origin) {
        this.value = exp;
        this.origin = origin;
    }

    public Expression getValue() {
        return value;
    }

    public DerivationNode getOrigin() {
        return origin;
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> json = baseJson();
        json.put("value", expToValue(value));
        if (origin != null)
            json.put("origin", origin.toJson());
        return json;
    }

    private Object expToValue(Expression exp) {
        if (exp == null)
            return null;
        if (exp instanceof LiteralInt)
            return ((LiteralInt) exp).getValue();
        if (exp instanceof LiteralReal)
            return ((LiteralReal) exp).getValue();
        if (exp instanceof LiteralBoolean)
            return ((LiteralBoolean) exp).isBooleanTrue();
        if (exp instanceof Var)
            return ((Var) exp).getName();
        return exp.toString();
    }
}
