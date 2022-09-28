package repair.regen.processor.context;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import repair.regen.errors.ErrorHandler;
import repair.regen.processor.constraints.Conjunction;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.reference.CtTypeReference;

public class RefinedFunction extends Refined {

    private List<Variable> argRefinements;
    private String targetClass;
    private List<ObjectState> stateChange;

    public RefinedFunction() {
        argRefinements = new ArrayList<>();
        stateChange = new ArrayList<>();
    }

    public List<Variable> getArguments() {
        return argRefinements;
    }

    public void addArgRefinements(String varName, CtTypeReference<?> type, Constraint refinement) {
        Variable v = new Variable(varName, type, refinement);
        this.argRefinements.add(v);

    }

    public void addArgRefinements(Variable vi) {
        this.argRefinements.add(vi);
    }

    public Constraint getRefReturn() {
        return super.getRefinement();
    }

    public void setRefReturn(Constraint ref) {
        super.setRefinement(ref);
    }

    public void setClass(String klass) {
        this.targetClass = klass;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public Constraint getRenamedRefinements(Context c, CtElement element) {
        return getRenamedRefinements(getAllRefinements(), c, element);
    }

    private Constraint getRenamedRefinements(Constraint place, Context context, CtElement element) {
        Constraint update = place.clone();
        for (Variable p : argRefinements) {
            String varName = p.getName();
            Constraint c = p.getRefinement();
            Optional<VariableInstance> ovi = p.getLastInstance();
            if (ovi.isPresent()) {
                varName = ovi.get().getName();
                c = p.getRenamedRefinements(varName);
            }
            context.addVarToContext(varName, p.getType(), c, element);
            update = update.substituteVariable(p.getName(), varName);
        }
        return update;
    }

    public Constraint getAllRefinements() {
        Constraint c = new Predicate();
        for (RefinedVariable p : argRefinements)
            c = Conjunction.createConjunction(c, p.getRefinement());// joinArgs
        c = Conjunction.createConjunction(c, super.getRefinement());// joinReturn
        return c;
    }

    /**
     * Gives the Constraint for a certain parameter index and regards all the previous parameters' Constraints
     *
     * @param index
     *
     * @return
     */
    public Constraint getRefinementsForParamIndex(int index) {
        Constraint c = new Predicate();
        for (int i = 0; i <= index && i < argRefinements.size(); i++)
            c = Conjunction.createConjunction(c, argRefinements.get(i).getRefinement());
        return c;
    }

    public boolean allRefinementsTrue() {
        boolean t = true;
        Predicate p = new Predicate(getRefReturn().getExpression());
        t = t && p.isBooleanTrue();
        for (Variable v : argRefinements) {
            p = new Predicate(v.getRefinement().getExpression());
            t = t && p.isBooleanTrue();
        }
        return t;
    }

    public List<ObjectState> getAllStates() {
        return stateChange;
    }

    public void setAllStates(List<ObjectState> l) {
        stateChange = l;
    }

    public void addStates(ObjectState e) {
        stateChange.add(e);
    }

    public boolean hasStateChange() {
        return stateChange.size() > 0;
    }

    public List<Constraint> getFromStates() {
        List<Constraint> lc = new ArrayList<>();
        for (ObjectState os : stateChange)
            lc.add(os.getFrom());
        return lc;
    }

    public List<Constraint> getToStates() {
        List<Constraint> lc = new ArrayList<>();
        for (ObjectState os : stateChange)
            lc.add(os.getTo());
        return lc;
    }

    @Override
    public String toString() {
        return "Function [name=" + super.getName() + ", argRefinements=" + argRefinements + ", refReturn="
                + super.getRefinement() + ", targetClass=" + targetClass + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((argRefinements == null) ? 0 : argRefinements.hashCode());
        result = prime * result + ((targetClass == null) ? 0 : targetClass.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        RefinedFunction other = (RefinedFunction) obj;
        if (argRefinements == null) {
            if (other.argRefinements != null)
                return false;
        } else if (argRefinements.size() != other.argRefinements.size())
            return false;
        if (targetClass == null) {
            if (other.targetClass != null)
                return false;
        } else if (!targetClass.equals(other.targetClass))
            return false;
        return true;
    }

}
