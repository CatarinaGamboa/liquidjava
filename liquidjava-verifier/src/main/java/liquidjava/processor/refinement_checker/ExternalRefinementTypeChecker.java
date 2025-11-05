package liquidjava.processor.refinement_checker;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import liquidjava.diagnostics.ErrorEmitter;
import liquidjava.diagnostics.ErrorHandler;
import liquidjava.processor.context.Context;
import liquidjava.processor.context.GhostFunction;
import liquidjava.processor.facade.GhostDTO;
import liquidjava.processor.refinement_checker.general_checkers.MethodsFunctionsChecker;
import liquidjava.rj_language.Predicate;
import liquidjava.rj_language.parsing.ParsingException;
import liquidjava.rj_language.parsing.RefinementsParser;
import liquidjava.utils.Utils;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.CtTypeParameter;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class ExternalRefinementTypeChecker extends TypeChecker {
    String prefix;
    MethodsFunctionsChecker m;

    public ExternalRefinementTypeChecker(Context context, Factory fac, ErrorEmitter errorEmitter) {
        super(context, fac, errorEmitter);
    }

    @Override
    public <T> void visitCtClass(CtClass<T> ctClass) {
        return;
    }

    @Override
    public <T> void visitCtInterface(CtInterface<T> intrface) {
        if (errorEmitter.foundError())
            return;

        Optional<String> externalRefinements = getExternalRefinement(intrface);
        if (externalRefinements.isPresent()) {
            this.prefix = externalRefinements.get();
            if (!classExists(prefix)) {
                ErrorHandler.printCustomError(intrface, "Could not find class '" + prefix + "'", errorEmitter);
                return;
            }
            try {
                getRefinementFromAnnotation(intrface);
            } catch (ParsingException e) {
                return; // error already in ErrorEmitter
            }
            handleStateSetsFromAnnotation(intrface);
            super.visitCtInterface(intrface);
        }
    }

    @Override
    public <T> void visitCtField(CtField<T> f) {
        if (errorEmitter.foundError())
            return;

        Optional<Predicate> oc;
        try {
            oc = getRefinementFromAnnotation(f);
        } catch (ParsingException e) {
            return; // error already in ErrorEmitter
        }
        Predicate c = oc.orElse(new Predicate());
        context.addGlobalVariableToContext(f.getSimpleName(), prefix, f.getType(), c);
        super.visitCtField(f);
    }

    public <R> void visitCtMethod(CtMethod<R> method) {
        if (errorEmitter.foundError())
            return;

        if (!methodExists(method)) {
            ErrorHandler.printCustomError(method,
                    "Could not find method '" + method.getSignature() + "' in class '" + prefix + "'", errorEmitter);
            return;
        }
        MethodsFunctionsChecker mfc = new MethodsFunctionsChecker(this);
        try {
            mfc.getMethodRefinements(method, prefix);
        } catch (ParsingException e) {
            return;
        }
        super.visitCtMethod(method);

        //
        // System.out.println("visited method external");
    }

    protected void getGhostFunction(String value, CtElement element) {
        try {
            // Optional<FunctionDeclaration> ofd =
            // RefinementParser.parseFunctionDecl(value);
            GhostDTO f = RefinementsParser.getGhostDeclaration(value);
            if (f != null && element.getParent() instanceof CtInterface<?>) {
                GhostFunction gh = new GhostFunction(f, factory, prefix);
                context.addGhostFunction(gh);
            }

        } catch (ParsingException e) {
            ErrorHandler.printCustomError(element, "Could not parse the Ghost Function" + e.getMessage(), errorEmitter);
            // e.printStackTrace();
        }
    }

    @Override
    protected Optional<GhostFunction> createStateGhost(int order, CtElement element) {
        String klass = Utils.getSimpleName(prefix);
        if (klass != null) {
            CtTypeReference<?> ret = factory.Type().INTEGER_PRIMITIVE;
            List<String> params = Arrays.asList(klass);
            String name = String.format("state%d", order);
            GhostFunction gh = new GhostFunction(name, params, ret, factory, prefix);
            return Optional.of(gh);
        }
        return Optional.empty();
    }

    @Override
    protected String getQualifiedClassName(CtElement elem) {
        return prefix;
    }

    @Override
    protected String getSimpleClassName(CtElement elem) {
        return Utils.getSimpleName(prefix);
    }

    private boolean classExists(String className) {
        return factory.Type().createReference(className).getTypeDeclaration() != null;
    }

    private boolean methodExists(CtMethod<?> method) {
        CtType<?> targetType = factory.Type().createReference(prefix).getTypeDeclaration();

        // find a method with matching name and parameter count
        boolean methodFound = targetType.getMethods().stream()
                .anyMatch(m -> m.getSimpleName().equals(method.getSimpleName())
                        && m.getParameters().size() == method.getParameters().size());

        if (!methodFound) {
            // check if constructor method
            return method.getSimpleName().equals(targetType.getSimpleName());
        }
        return true;
    }
}
