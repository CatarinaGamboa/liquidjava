package liquidjava.rj_language.opt.derivation_node;

import java.util.LinkedHashMap;
import java.util.Map;

import liquidjava.rj_language.ast.Expression;
import liquidjava.rj_language.ast.LiteralBoolean;
import liquidjava.rj_language.ast.LiteralInt;
import liquidjava.rj_language.ast.LiteralReal;
import liquidjava.rj_language.ast.Var;

public abstract class DerivationNode {
    private Expression exp;

    public DerivationNode(Expression exp) {
        this.exp = exp;
    }

    public Expression getExp() {
        return exp;
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

    public abstract Map<String, Object> toJson();

    protected Map<String, Object> baseJson() {
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("exp", expressionToValue(exp));
        return json;
    }

    protected Object expressionToValue(Expression expression) {
        if (expression == null)
            return null;
        if (expression instanceof LiteralInt)
            return ((LiteralInt) expression).getValue();
        if (expression instanceof LiteralReal)
            return ((LiteralReal) expression).getValue();
        if (expression instanceof LiteralBoolean)
            return ((LiteralBoolean) expression).isBooleanTrue();
        if (expression instanceof Var)
            return ((Var) expression).getName();
        return expression.toString();
    }
}