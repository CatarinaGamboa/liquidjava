package repair.regen.processor.constraints;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repair.regen.errors.ErrorEmitter;
import repair.regen.errors.ErrorHandler;
import repair.regen.processor.context.AliasWrapper;
import repair.regen.processor.context.Context;
import repair.regen.processor.context.GhostState;
import repair.regen.processor.facade.AliasDTO;
import repair.regen.rj_language.ast.Expression;
import repair.regen.rj_language.ast.UnaryExpression;
import repair.regen.rj_language.parsing.ParsingException;
import repair.regen.rj_language.parsing.RefinementsParser;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.factory.Factory;

public abstract class Constraint {

    public abstract Constraint substituteVariable(String from, String to);

    public abstract Constraint clone();

    public abstract List<String> getVariableNames();

    public abstract String toString();

    public abstract boolean isBooleanTrue();

    public abstract Constraint changeOldMentions(String previousName, String newName, ErrorEmitter ee);

    public abstract Constraint changeStatesToRefinements(List<GhostState> ghostState, String[] toChange,
            ErrorEmitter ee);

    public abstract Expression getExpression();

    public Constraint negate() {
        Expression e = new UnaryExpression("!", getExpression());
        return new Predicate(e);
    }

    protected Expression parse(String ref, CtElement element, ErrorEmitter e) throws ParsingException {
        try {
            return RefinementsParser.createAST(ref);
        } catch (ParsingException e1) {
            ErrorHandler.printSyntaxError(e1.getMessage(), ref, element, e);
            throw e1;
        }
    }

    protected Expression innerParse(String ref, ErrorEmitter e) {
        try {
            return RefinementsParser.createAST(ref);
        } catch (ParsingException e1) {
            ErrorHandler.printSyntaxError(e1.getMessage(), ref, e);
        }
        return null;
    }

    public Constraint changeAliasToRefinement(Context context, CtElement element, Factory f) throws Exception {
        Expression ref = getExpression();

        Map<String, AliasDTO> alias = new HashMap<>();
        for (AliasWrapper aw : context.getAlias()) {
            alias.put(aw.getName(), aw.createAliasDTO());
        }

        ref = ref.changeAlias(alias, context, f);
        return new Predicate(ref);
    }

}
