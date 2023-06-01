package liquidjava.processor.refinement_checker;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import liquidjava.errors.ErrorEmitter;
import liquidjava.errors.ErrorHandler;
import liquidjava.processor.context.Context;
import liquidjava.processor.context.GhostFunction;
import liquidjava.processor.facade.GhostDTO;
import liquidjava.processor.refinement_checker.general_checkers.MethodsFunctionsChecker;
import liquidjava.rj_language.Predicate;
import liquidjava.rj_language.parsing.ParsingException;
import liquidjava.rj_language.parsing.RefinementsParser;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class ExternalRefinementTypeChecker extends TypeChecker {
    String prefix;
    MethodsFunctionsChecker m;

    public ExternalRefinementTypeChecker(Context context, Factory fac, ErrorEmitter errorEmitter) {
        super(context, fac, errorEmitter);
        System.out.println("ExternalRefinementTypeChecker");
    }

    @Override
    public <T> void visitCtClass(CtClass<T> ctClass) {
    }

    @Override
    public <T> void visitCtInterface(CtInterface<T> intrface) {
        if (errorEmitter.foundError())
            return;

        getExternalRefinement(intrface).ifPresent(p -> {
            prefix = p;
            try {
                getRefinementFromAnnotation(intrface);
            } catch (ParsingException e) {
                return;// error already in ErrorEmitter
            }
            handleStateSetsFromAnnotation(intrface);
            super.visitCtInterface(intrface);
        });
    }

    @Override
    public <T> void visitCtField(CtField<T> f) {
        if (errorEmitter.foundError())
            return;

        Predicate c;
        try {
            c = getRefinementFromAnnotation(f).orElse(new Predicate());
        } catch (ParsingException e) {
            return;// error already in ErrorEmitter
        }
        context.addGlobalVariableToContext(f.getSimpleName(), prefix, f.getType(), c);
        super.visitCtField(f);
    }

    public <R> void visitCtMethod(CtMethod<R> method) {
        if (errorEmitter.foundError())
            return;

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
                String[] a = prefix.split("\\.");
                String d = a[a.length - 1];
                GhostFunction gh = new GhostFunction(f, factory, prefix, d);
                context.addGhostFunction(gh);
                System.out.println(gh.toString());
            }

        } catch (ParsingException e) {
            ErrorHandler.printCostumeError(element, "Could not parse the Ghost Function" + e.getMessage(),
                    errorEmitter);
            // e.printStackTrace();
        }
    }

    @Override
    protected Optional<GhostFunction> createStateGhost(int order, CtElement element) {
        String[] a = prefix.split("\\.");
        String klass = a[a.length - 1];
        if (klass != null) {
            CtTypeReference<?> ret = factory.Type().INTEGER_PRIMITIVE;
            List<String> params = Arrays.asList(klass);
            GhostFunction gh = new GhostFunction(String.format("%s_state%d", klass.toLowerCase(), order), params, ret,
                    factory, prefix, klass);
            System.out.println(gh.toString());
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
        String[] a = prefix.split("\\.");
        return a[a.length - 1];
    }
}
