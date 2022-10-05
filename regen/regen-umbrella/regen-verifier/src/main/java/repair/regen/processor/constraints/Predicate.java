package repair.regen.processor.constraints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import repair.regen.errors.ErrorEmitter;
import repair.regen.processor.context.GhostState;
import repair.regen.rj_language.ast.BinaryExpression;
import repair.regen.rj_language.ast.Expression;
import repair.regen.rj_language.ast.GroupExpression;
import repair.regen.rj_language.ast.Ite;
import repair.regen.rj_language.ast.LiteralBoolean;
import repair.regen.rj_language.ast.LiteralInt;
import repair.regen.rj_language.ast.LiteralReal;
import repair.regen.rj_language.ast.LiteralString;
import repair.regen.rj_language.ast.UnaryExpression;
import repair.regen.rj_language.ast.Var;
import repair.regen.rj_language.parsing.ParsingException;
import repair.regen.utils.Utils;
import spoon.reflect.declaration.CtElement;

public class Predicate extends Constraint {
    private final String OLD = "old";

    protected Expression exp;

    /**
     * Create a predicate with the expression true
     */
    public Predicate() {
        exp = new LiteralBoolean(true);
    }

    /**
     * Create a new predicate with a refinement
     * @param ref
     * @param element
     * @param e
     * @throws ParsingException
     */
    public Predicate(String ref, CtElement element, ErrorEmitter e) throws ParsingException {
        exp = parse(ref, element, e);
        if (e.foundError())
            return;
        if (!(exp instanceof GroupExpression)) {
            exp = new GroupExpression(exp);
        }
    }

    /**
     * Create a predicate with the expression true
     */
    public Predicate(Expression e) {
        exp = e;
    }

    @Override
    public Constraint negate() {
        return new Predicate(new UnaryExpression("!", exp));
    }

    @Override
    public Constraint substituteVariable(String from, String to) {
        Expression ec = exp.clone();
        ec = ec.substitute(new Var(from), new Var(to));
        return new Predicate(ec);
    }

    @Override
    public List<String> getVariableNames() {
        List<String> l = new ArrayList<>();
        exp.getVariableNames(l);
        return l;
    }

    public List<GhostState> getStateInvocations(List<GhostState> lgs) {
        if (lgs == null)
            return new ArrayList<>();
        List<String> all = lgs.stream().map(p -> p.getName()).collect(Collectors.toList());
        List<String> toAdd = new ArrayList<>();
        exp.getStateInvocations(toAdd, all);

        List<GhostState> gh = new ArrayList<>();
        for (String n : toAdd) {
            for (GhostState g : lgs)
                if (g.getName().equals(n))
                    gh.add(g);
        }

        return gh;
    }

    /**
     * Change old mentions of previous name to the new name
     * e.g., old(previousName) -> newName
     */
    public Constraint changeOldMentions(String previousName, String newName, ErrorEmitter ee) {
        Expression e = exp.clone();
        Expression prev = innerParse(previousName, ee);
        List<Expression> le = new ArrayList<>();
        le.add(innerParse(newName, ee));
        e.substituteFunction(OLD, le, prev);
        return new Predicate(e);
    }

    @Override
    public Constraint changeStatesToRefinements(List<GhostState> ghostState, String[] toChange, ErrorEmitter ee) {
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
    public Constraint clone() {
        Constraint c = new Predicate(exp.clone());
        return c;
    }

    @Override
    public Expression getExpression() {
        return exp;
    }
    
    
    public static Predicate createConjunction(Constraint c1, Constraint c2) {
    	return new Predicate(new BinaryExpression(c1.getExpression(), 
    			Utils.AND, c2.getExpression()));
    } 
    
    public static Predicate createDisjunction(Constraint c1, Constraint c2) {
    	return new Predicate(new BinaryExpression(c1.getExpression(), 
    			Utils.OR, c2.getExpression()));
    } 
    
    public static Predicate createEquals(Constraint c1, Constraint c2) {
    	return new Predicate(new BinaryExpression(c1.getExpression(), 
    			Utils.EQ, c2.getExpression()));
    } 
    
    public static Predicate createITE(Constraint c1, Constraint c2, Constraint c3) {
    	 return new Predicate( new Ite(c1.getExpression(), c2.getExpression(), 
    			 c3.getExpression()));
    } 
    
    public static Predicate createLit(String value, String type) {
   	 	Expression ex;
    	if(type.equals(Utils.BOOLEAN))
   	 		ex = new LiteralBoolean(value);
    	else if(type.equals(Utils.INT))
    		ex = new LiteralInt(value);
    	else if(type.equals(Utils.DOUBLE))
    		ex = new LiteralReal(value);
    	else if(type.equals(Utils.SHORT))
    		ex = new LiteralInt(value);
    	else if(type.equals(Utils.LONG))
    		ex = new LiteralReal(value);
    	else // if(type.equals(Utils.DOUBLE))
    		ex = new LiteralReal(value);
    	return new Predicate(ex);
   } 
    
    public static Predicate createOperation(Constraint c1, String op, Constraint c2) {
    	return new Predicate(new BinaryExpression(c1.getExpression(), op, c2.getExpression()));
    }

    public static Predicate createVar(String name) {
    	return new Predicate(new Var(name));
    }

}
