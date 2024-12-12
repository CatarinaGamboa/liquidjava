package liquidjava.rj_language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import liquidjava.errors.ErrorEmitter;
import liquidjava.errors.ErrorHandler;
import liquidjava.processor.context.AliasWrapper;
import liquidjava.processor.context.Context;
import liquidjava.processor.context.GhostState;
import liquidjava.processor.facade.AliasDTO;
import liquidjava.rj_language.ast.BinaryExpression;
import liquidjava.rj_language.ast.Expression;
import liquidjava.rj_language.ast.FunctionInvocation;
import liquidjava.rj_language.ast.GroupExpression;
import liquidjava.rj_language.ast.Ite;
import liquidjava.rj_language.ast.LiteralBoolean;
import liquidjava.rj_language.ast.LiteralInt;
import liquidjava.rj_language.ast.LiteralReal;
import liquidjava.rj_language.ast.UnaryExpression;
import liquidjava.rj_language.ast.Var;
import liquidjava.rj_language.parsing.ParsingException;
import liquidjava.rj_language.parsing.RefinementsParser;
import liquidjava.utils.Utils;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.factory.Factory;

/**
 * Acts as a wrapper for Expression AST
 *
 * @author cgamboa
 */
public class Predicate {

    protected Expression exp;

    /** Create a predicate with the expression true */
    public Predicate() {
        exp = new LiteralBoolean(true);
    }

    /**
     * Create a new predicate with a refinement
     *
     * @param ref
     * @param element
     * @param e
     *
     * @throws ParsingException
     */
    public Predicate(String ref, CtElement element, ErrorEmitter e) throws ParsingException {
        exp = parse(ref, element, e);
        if (e.foundError()) return;
        if (!(exp instanceof GroupExpression)) {
            exp = new GroupExpression(exp);
        }
    }

    /** Create a predicate with the expression true */
    public Predicate(Expression e) {
        exp = e;
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

    public Predicate changeAliasToRefinement(Context context, Factory f) throws Exception {
        Expression ref = getExpression();

        Map<String, AliasDTO> alias = new HashMap<>();
        for (AliasWrapper aw : context.getAlias()) {
            alias.put(aw.getName(), aw.createAliasDTO());
        }

        ref = ref.changeAlias(alias, context, f);
        return new Predicate(ref);
    }

    public Predicate negate() {
        return new Predicate(new UnaryExpression("!", exp));
    }

    public Predicate substituteVariable(String from, String to) {
        Expression ec = exp.clone();
        ec = ec.substitute(new Var(from), new Var(to));
        return new Predicate(ec);
    }

    public List<String> getVariableNames() {
        List<String> l = new ArrayList<>();
        exp.getVariableNames(l);
        return l;
    }

    public List<GhostState> getStateInvocations(List<GhostState> lgs) {
        if (lgs == null) return new ArrayList<>();
        List<String> all = lgs.stream().map(p -> p.getName()).collect(Collectors.toList());
        List<String> toAdd = new ArrayList<>();
        exp.getStateInvocations(toAdd, all);

        List<GhostState> gh = new ArrayList<>();
        for (String n : toAdd) {
            for (GhostState g : lgs) if (g.getName().equals(n)) gh.add(g);
        }

        return gh;
    }

    /** Change old mentions of previous name to the new name e.g., old(previousName) -> newName */
    public Predicate changeOldMentions(String previousName, String newName, ErrorEmitter ee) {
        Expression e = exp.clone();
        Expression prev = createVar(previousName).getExpression();
        List<Expression> le = new ArrayList<>();
        le.add(createVar(newName).getExpression());
        e.substituteFunction(Utils.OLD, le, prev);
        return new Predicate(e);
    }

    public List<String> getOldVariableNames() {
        List<String> ls = new ArrayList<>();
        expressionGetOldVariableNames(this.exp, ls);
        return ls;
    }

    private void expressionGetOldVariableNames(Expression exp, List<String> ls) {
        if (exp instanceof FunctionInvocation) {
            FunctionInvocation fi = (FunctionInvocation) exp;
            if (fi.getName().equals(Utils.OLD)) {
                List<Expression> le = fi.getArgs();
                for (Expression e : le) {
                    if (e instanceof Var) ls.add(((Var) e).getName());
                }
            }
        }
        if (exp.hasChildren()) {
            for (var ch : exp.getChildren()) expressionGetOldVariableNames(ch, ls);
        }
    }

    public Predicate changeStatesToRefinements(List<GhostState> ghostState, String[] toChange, ErrorEmitter ee) {
        Map<String, Expression> nameRefinementMap = new HashMap<>();
        for (GhostState gs : ghostState)
            if (gs.getRefinement() != null) // is a state and not a ghost state
            nameRefinementMap.put(gs.getName(), innerParse(gs.getRefinement().toString(), ee));

        Expression e = exp.substituteState(nameRefinementMap, toChange);
        return new Predicate(e);
    }

    public boolean isBooleanTrue() {
        return exp.isBooleanTrue();
    }

    @Override
    public String toString() {
        return exp.toString();
    }

    @Override
    public Predicate clone() {
        Predicate c = new Predicate(exp.clone());
        return c;
    }

    public Expression getExpression() {
        return exp;
    }

    public static Predicate createConjunction(Predicate c1, Predicate c2) {
        return new Predicate(new BinaryExpression(c1.getExpression(), Utils.AND, c2.getExpression()));
    }

    public static Predicate createDisjunction(Predicate c1, Predicate c2) {
        return new Predicate(new BinaryExpression(c1.getExpression(), Utils.OR, c2.getExpression()));
    }

    public static Predicate createEquals(Predicate c1, Predicate c2) {
        return new Predicate(new BinaryExpression(c1.getExpression(), Utils.EQ, c2.getExpression()));
    }

    public static Predicate createITE(Predicate c1, Predicate c2, Predicate c3) {
        return new Predicate(new Ite(c1.getExpression(), c2.getExpression(), c3.getExpression()));
    }

    public static Predicate createLit(String value, String type) {
        Expression ex;
        if (type.equals(Utils.BOOLEAN)) ex = new LiteralBoolean(value);
        else if (type.equals(Utils.INT)) ex = new LiteralInt(value);
        else if (type.equals(Utils.DOUBLE)) ex = new LiteralReal(value);
        else if (type.equals(Utils.SHORT)) ex = new LiteralInt(value);
        else if (type.equals(Utils.LONG)) ex = new LiteralReal(value);
        else // if(type.equals(Utils.DOUBLE))
        ex = new LiteralReal(value);
        return new Predicate(ex);
    }

    public static Predicate createOperation(Predicate c1, String op, Predicate c2) {
        return new Predicate(new BinaryExpression(c1.getExpression(), op, c2.getExpression()));
    }

    public static Predicate createVar(String name) {
        return new Predicate(new Var(name));
    }

    public static Predicate createInvocation(String name, Predicate... Predicates) {
        List<Expression> le = new ArrayList<>();
        for (Predicate c : Predicates) le.add(c.getExpression());
        return new Predicate(new FunctionInvocation(name, le));
    }
}
