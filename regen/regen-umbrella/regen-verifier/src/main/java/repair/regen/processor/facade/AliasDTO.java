package repair.regen.processor.facade;

import java.util.List;
import java.util.stream.Collectors;

import repair.regen.ast.Expression;
import repair.regen.processor.constraints.Predicate;
import repair.regen.rj_language.ParsingException;
import repair.regen.rj_language.RefinementsParser;
import spoon.reflect.reference.CtTypeReference;

public class AliasDTO {
    private String name;
    private List<String> varTypes;
    private List<String> varNames;
    private Expression expression;

    public AliasDTO(String name, List<CtTypeReference<?>> varTypes, List<String> varNames, Expression expression) {
        super();
        this.name = name;
        this.varTypes = varTypes.stream().map(m -> m.getQualifiedName()).collect(Collectors.toList());
        this.varNames = varNames;
        this.expression = expression;
    }

    public AliasDTO(String name2, List<String> varTypes2, List<String> varNames2, String ref) throws ParsingException {
        super();
        this.name = name2;
        this.varTypes = varTypes2;
        this.varNames = varNames2;
        this.expression = RefinementsParser.createAST(ref);

    }

    public String getName() {
        return name;
    }

    public List<String> getVarTypes() {
        return varTypes;
    }

    public List<String> getVarNames() {
        return varNames;
    }

    public Expression getExpression() {
        return expression;
    }

}
