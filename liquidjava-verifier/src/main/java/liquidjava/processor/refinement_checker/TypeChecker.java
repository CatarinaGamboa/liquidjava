package liquidjava.processor.refinement_checker;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import liquidjava.errors.ErrorEmitter;
import liquidjava.errors.ErrorHandler;
import liquidjava.processor.context.AliasWrapper;
import liquidjava.processor.context.Context;
import liquidjava.processor.context.GhostFunction;
import liquidjava.processor.context.GhostState;
import liquidjava.processor.context.RefinedVariable;
import liquidjava.processor.facade.AliasDTO;
import liquidjava.processor.facade.GhostDTO;
import liquidjava.rj_language.Predicate;
import liquidjava.rj_language.parsing.ParsingException;
import liquidjava.rj_language.parsing.RefinementsParser;
import liquidjava.utils.Utils;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtNewArray;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.CtScanner;

public abstract class TypeChecker extends CtScanner {
    public final String REFINE_KEY = "refinement";
    public final String TARGET_KEY = "target";
    // public final String STATE_KEY = "state";
    public final String THIS = "this";
    public final String WILD_VAR = "_";
    public final String freshFormat = "#fresh_%d";
    public final String instanceFormat = "#%s_%d";
    public final String thisFormat = "this#%s";
    public String[] implementedTypes = { "boolean", "int", "short", "long", "float", "double" }; // TODO add
    // types e.g., "int[]"

    Context context;
    Factory factory;
    VCChecker vcChecker;
    ErrorEmitter errorEmitter;

    public TypeChecker(Context c, Factory fac, ErrorEmitter errorEmitter) {
        this.context = c;
        this.factory = fac;
        this.errorEmitter = errorEmitter;
        vcChecker = new VCChecker(errorEmitter);
    }

    public Context getContext() {
        return context;
    }

    public Factory getFactory() {
        return factory;
    }

    public Predicate getRefinement(CtElement elem) {
        Predicate c = (Predicate) elem.getMetadata(REFINE_KEY);
        return c == null ? new Predicate() : c;
    }

    public Optional<Predicate> getRefinementFromAnnotation(CtElement element) throws ParsingException {
        Optional<Predicate> constr = Optional.empty();
        Optional<String> ref = Optional.empty();
        for (CtAnnotation<? extends Annotation> ann : element.getAnnotations()) {
            String an = ann.getActualAnnotation().annotationType().getCanonicalName();
            if (an.contentEquals("liquidjava.specification.Refinement")) {
                String st = TypeCheckingUtils.getStringFromAnnotation(ann.getValue("value"));
                // CtLiteral<String> s = (CtLiteral<String>) ann.getAllValues().get("value");
                ref = Optional.of(st);

            } else if (an.contentEquals("liquidjava.specification.RefinementPredicate")) {
                String st = TypeCheckingUtils.getStringFromAnnotation(ann.getValue("value"));
                getGhostFunction(st, element);

            } else if (an.contentEquals("liquidjava.specification.RefinementAlias")) {
                String st = TypeCheckingUtils.getStringFromAnnotation(ann.getValue("value"));
                handleAlias(st, element);
            }
        }
        if (ref.isPresent()) {
            Predicate p = new Predicate(ref.get(), element, errorEmitter);
            if (errorEmitter.foundError())
                return Optional.empty();
            constr = Optional.of(p);
        }
        return constr;
    }

    @SuppressWarnings("unchecked")
    public void handleStateSetsFromAnnotation(CtElement element) {
        int set = 0;
        for (CtAnnotation<? extends Annotation> ann : element.getAnnotations()) {
            String an = ann.getActualAnnotation().annotationType().getCanonicalName();
            if (an.contentEquals("liquidjava.specification.StateSet")) {
                set++;
                createStateSet((CtNewArray<String>) ann.getAllValues().get("value"), set, element);
            }
            if (an.contentEquals("liquidjava.specification.Ghost")) {
                CtLiteral<String> s = (CtLiteral<String>) ann.getAllValues().get("value");
                createStateGhost(s.getValue(), ann, an, element);
            }
        }
    }

    private void createStateSet(CtNewArray<String> e, int set, CtElement element) {

        // if any of the states starts with uppercase, throw error (reserved for alias)
        for (CtExpression<?> ce : e.getElements()) {
            if (ce instanceof CtLiteral<?>) {
                @SuppressWarnings("unchecked")
                CtLiteral<String> s = (CtLiteral<String>) ce;
                String f = s.getValue();
                if (Character.isUpperCase(f.charAt(0))) {
                    ErrorHandler.printCostumeError(s, "State name must start with lowercase in '" + f + "'", errorEmitter);
                }
            }
        }

        Optional<GhostFunction> og = createStateGhost(set, element);
        if (!og.isPresent()) {
            throw new RuntimeException("Error in creation of GhostFunction");
        }
        GhostFunction g = og.get();
        context.addGhostFunction(g);
        context.addGhostClass(g.getParentClassName());

        List<CtExpression<?>> ls = e.getElements();
        Predicate ip = Predicate.createInvocation(g.getName(), Predicate.createVar(THIS));
        int order = 0;
        for (CtExpression<?> ce : ls) {
            if (ce instanceof CtLiteral<?>) {
                @SuppressWarnings("unchecked")
                CtLiteral<String> s = (CtLiteral<String>) ce;
                String f = s.getValue();
                GhostState gs = new GhostState(f, g.getParametersTypes(), factory.Type().BOOLEAN_PRIMITIVE,
                        g.getPrefix());
                gs.setGhostParent(g);
                gs.setRefinement(
                        /* new OperationPredicate(new InvocationPredicate(f, THIS), "<-->", */
                        Predicate.createEquals(ip, Predicate.createLit(Integer.toString(order), Utils.INT))); // open(THIS)
                // ->
                // state1(THIS)
                // == 1
                context.addToGhostClass(g.getParentClassName(), gs);
            }
            order++;
        }
    }

    private void createStateGhost(String string, CtAnnotation<? extends Annotation> ann, String an, CtElement element) {
        GhostDTO gd = null;
        try {
            gd = RefinementsParser.getGhostDeclaration(string);
        } catch (ParsingException e) {
            ErrorHandler.printCostumeError(ann, "Could not parse the Ghost Function" + e.getMessage(), errorEmitter);
            return;
        }
        if (gd.getParam_types().size() > 0) {
            ErrorHandler.printCostumeError(ann, "Ghost States have the class as parameter "
                    + "by default, no other parameters are allowed in '" + string + "'", errorEmitter);
            return;
        }
        // Set class as parameter of Ghost
        String qn = getQualifiedClassName(element);
        String sn = getSimpleClassName(element);
        context.addGhostClass(sn);
        List<CtTypeReference<?>> param = Arrays.asList(factory.Type().createReference(qn));

        CtTypeReference<?> r = factory.Type().createReference(gd.getReturn_type());
        GhostState gs = new GhostState(gd.getName(), param, r, qn);
        context.addToGhostClass(sn, gs);
    }

    protected String getQualifiedClassName(CtElement element) {
        if (element.getParent() instanceof CtClass<?>) {
            return ((CtClass<?>) element.getParent()).getQualifiedName();
        } else if (element instanceof CtClass<?>) {
            return ((CtClass<?>) element).getQualifiedName();
        }
        return null;
    }

    protected String getSimpleClassName(CtElement element) {
        if (element.getParent() instanceof CtClass<?>) {
            return ((CtClass<?>) element.getParent()).getSimpleName();
        } else if (element instanceof CtClass<?>) {
            return ((CtClass<?>) element).getSimpleName();
        }
        return null;
    }

    protected Optional<GhostFunction> createStateGhost(int order, CtElement element) {
        CtClass<?> klass = null;
        if (element.getParent() instanceof CtClass<?>) {
            klass = (CtClass<?>) element.getParent();
        } else if (element instanceof CtClass<?>) {
            klass = (CtClass<?>) element;
        }
        if (klass != null) {
            CtTypeReference<?> ret = factory.Type().INTEGER_PRIMITIVE;
            List<String> params = Arrays.asList(klass.getSimpleName());
            String name = String.format("state%d", order);
            GhostFunction gh = new GhostFunction(name, params, ret, factory, klass.getQualifiedName());
            return Optional.of(gh);
        }
        return Optional.empty();
    }

    protected void getGhostFunction(String value, CtElement element) {
        try {
            GhostDTO f = RefinementsParser.getGhostDeclaration(value);
            if (f != null && element.getParent() instanceof CtClass<?>) {
                CtClass<?> klass = (CtClass<?>) element.getParent();
                GhostFunction gh = new GhostFunction(f, factory, klass.getQualifiedName());
                context.addGhostFunction(gh);
            }
        } catch (ParsingException e) {
            ErrorHandler.printCostumeError(element, "Could not parse the Ghost Function" + e.getMessage(),
                    errorEmitter);
            // e.printStackTrace();
            return;
        }
    }

    protected void handleAlias(String value, CtElement element) {
        try {
            AliasDTO a = RefinementsParser.getAliasDeclaration(value);

            if (a != null) {
                String klass = null;
                String path = null;
                if (element instanceof CtClass) {
                    klass = ((CtClass<?>) element).getSimpleName();
                    path = ((CtClass<?>) element).getQualifiedName();
                } else if (element instanceof CtInterface<?>) {
                    klass = ((CtInterface<?>) element).getSimpleName();
                    path = ((CtInterface<?>) element).getQualifiedName();
                }
                if (klass != null && path != null) {
                    a.parse(path);
                    AliasWrapper aw = new AliasWrapper(a, factory, WILD_VAR, context, klass, path);
                    context.addAlias(aw);
                }
            }
        } catch (ParsingException e) {
            ErrorHandler.printCostumeError(element, e.getMessage(), errorEmitter);
            return;
            // e.printStackTrace();
        }
    }

    Optional<String> getExternalRefinement(CtInterface<?> intrface) {
        Optional<String> ref = Optional.empty();
        for (CtAnnotation<? extends Annotation> ann : intrface.getAnnotations())
            if (ann.getActualAnnotation().annotationType().getCanonicalName()
                    .contentEquals("liquidjava.specification.ExternalRefinementsFor")) {
                @SuppressWarnings("unchecked")
                CtLiteral<String> s = (CtLiteral<String>) ann.getAllValues().get("value");
                ref = Optional.of(s.getValue());
            }
        return ref;
    }

    public void checkVariableRefinements(Predicate refinementFound, String simpleName, CtTypeReference<?> type,
            CtElement usage, CtElement variable) throws ParsingException {
        Optional<Predicate> expectedType = getRefinementFromAnnotation(variable);
        Predicate cEt;
        RefinedVariable mainRV = null;
        if (context.hasVariable(simpleName))
            mainRV = context.getVariableByName(simpleName);

        if (context.hasVariable(simpleName) && !context.getVariableByName(simpleName).getRefinement().isBooleanTrue())
            cEt = mainRV.getMainRefinement();
        else if (expectedType.isPresent())
            cEt = expectedType.get();
        else
            cEt = new Predicate();

        cEt = cEt.substituteVariable(WILD_VAR, simpleName);
        Predicate cet = cEt.substituteVariable(WILD_VAR, simpleName);

        String newName = String.format(instanceFormat, simpleName, context.getCounter());
        Predicate correctNewRefinement = refinementFound.substituteVariable(WILD_VAR, newName);
        correctNewRefinement = correctNewRefinement.substituteVariable(THIS, newName);
        cEt = cEt.substituteVariable(simpleName, newName);

        // Substitute variable in verification
        RefinedVariable rv = context.addInstanceToContext(newName, type, correctNewRefinement, usage);
        for (CtTypeReference<?> t : mainRV.getSuperTypes())
            rv.addSuperType(t);
        context.addRefinementInstanceToVariable(simpleName, newName);
        // smt check
        checkSMT(cEt, usage); // TODO CHANGE
        context.addRefinementToVariableInContext(simpleName, type, cet, usage);
    }

    public void checkSMT(Predicate expectedType, CtElement element) {
        vcChecker.processSubtyping(expectedType, context.getGhostState(), WILD_VAR, THIS, element, factory);
        element.putMetadata(REFINE_KEY, expectedType);
    }

    public void checkStateSMT(Predicate prevState, Predicate expectedState, CtElement target, String string) {
        vcChecker.processSubtyping(prevState, expectedState, context.getGhostState(), WILD_VAR, THIS, target, string,
                factory);
    }

    public boolean checksStateSMT(Predicate prevState, Predicate expectedState, SourcePosition p) {
        return vcChecker.canProcessSubtyping(prevState, expectedState, context.getGhostState(), WILD_VAR, THIS, p,
                factory);
    }

    public void createError(CtElement element, Predicate expectedType, Predicate foundType, String customeMessage) {
        vcChecker.printSubtypingError(element, expectedType, foundType, customeMessage);
    }

    public void createSameStateError(CtElement element, Predicate expectedType, String klass) {
        vcChecker.printSameStateError(element, expectedType, klass);
    }

    public void createStateMismatchError(CtElement element, String method, Predicate c, String states) {
        vcChecker.printStateMismatchError(element, method, c, states);
    }

    public ErrorEmitter getErrorEmitter() {
        return errorEmitter;
    }
}
