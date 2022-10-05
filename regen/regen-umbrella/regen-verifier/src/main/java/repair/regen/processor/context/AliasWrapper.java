package repair.regen.processor.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repair.regen.errors.ErrorEmitter;
import repair.regen.processor.facade.AliasDTO;
import repair.regen.rj_language.Predicate;
import repair.regen.rj_language.ast.Expression;
import repair.regen.rj_language.parsing.ParsingException;
import repair.regen.utils.Utils;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class AliasWrapper {
    private String name;
    private List<CtTypeReference<?>> varTypes;
    private List<String> varNames;
    private Predicate expression;
    // private Context context;

    private String newAliasFormat = "#alias_%s_%d";

    public AliasWrapper(AliasDTO a, Factory factory, String wILD_VAR, Context context2, String klass, String path) {
        name = a.getName();
        expression = new Predicate(a.getExpression());

        varTypes = new ArrayList<>();
        varNames = a.getVarNames();
        for (String s : a.getVarTypes()) {
            CtTypeReference<?> r = Utils.getType(s.equals(klass) ? path : s, factory);
            varTypes.add(r);
        }
    }

    public String getName() {
        return name;
    }

    public List<CtTypeReference<?>> getTypes() {
        return varTypes;
    }

    public List<String> getVarNames() {
        return varNames;
    }

    public Predicate getClonedPredicate() {
        return (Predicate) expression.clone();
    }

    public Expression getNewExpression(List<String> newNames) {
        Predicate expr = getClonedPredicate();
        for (int i = 0; i < newNames.size(); i++) {
            expr = expr.substituteVariable(varNames.get(i), newNames.get(i));
        }
        return expr.getExpression().clone();
    }

    public Predicate getPremises(List<String> list, List<String> newNames, CtElement elem, ErrorEmitter ee)
            throws ParsingException {
        List<Predicate> invocationPredicates = getPredicatesFromExpression(list, elem, ee);
        Predicate prem = new Predicate();
        for (int i = 0; i < invocationPredicates.size(); i++) {
            prem = Predicate.createConjunction(prem,
                    Predicate.createEquals(Predicate.createVar(newNames.get(i)), invocationPredicates.get(i)));
        }
        return prem.clone();
    }

    private List<Predicate> getPredicatesFromExpression(List<String> list, CtElement elem, ErrorEmitter ee)
            throws ParsingException {
        List<Predicate> lp = new ArrayList<>();
        for (String e : list)
            lp.add(new Predicate(e, elem, ee));

        return lp;
    }

    public List<String> getNewVariables(Context context) {
        List<String> n = new ArrayList<>();
        for (int i = 0; i < varNames.size(); i++)
            n.add(String.format(newAliasFormat, varNames.get(i), context.getCounter()));
        return n;
    }

    public Map<String, CtTypeReference<?>> getTypes(List<String> names) {
        Map<String, CtTypeReference<?>> m = new HashMap<>();
        for (int i = 0; i < names.size(); i++) {
            m.put(names.get(i), varTypes.get(i));
        }
        return m;
    }

    public AliasDTO createAliasDTO() {
        return new AliasDTO(name, varTypes, varNames, expression.getExpression());
    }

    // public Expression getSubstitutedExpression(List<String> newNames) {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //

    // TypeKeyword tk;
    // AliasName name;
    //
    // ParenthesisLeft pl;
    // Type type;
    // Var var;
    // ParenthesisRight rl;
    //
    // BraceLeft bl;
    // Expression e;
    // BraceRight br;

}
