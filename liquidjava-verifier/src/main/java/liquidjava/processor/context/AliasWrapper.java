package liquidjava.processor.context;

import java.util.ArrayList;
import java.util.List;
import liquidjava.processor.facade.AliasDTO;
import liquidjava.rj_language.Predicate;
import liquidjava.utils.Utils;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class AliasWrapper {
    private final String name;
    private final List<CtTypeReference<?>> varTypes;
    private final List<String> varNames;
    private final Predicate expression;

    public AliasWrapper(AliasDTO a, Factory factory, String klass, String path) {
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
        return expression.clone();
    }

    public AliasDTO createAliasDTO() {
        return new AliasDTO(name, varTypes, varNames, expression.getExpression());
    }
}
