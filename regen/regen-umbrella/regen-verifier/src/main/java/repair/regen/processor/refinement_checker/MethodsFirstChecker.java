package repair.regen.processor.refinement_checker;

import java.util.ArrayList;
import java.util.List;

import repair.regen.errors.ErrorEmitter;
import repair.regen.processor.context.Context;
import repair.regen.processor.refinement_checker.general_checkers.MethodsFunctionsChecker;
import repair.regen.rj_language.ParsingException;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

/**
 * First visit to Spoon AST to get the method's refinements
 */
public class MethodsFirstChecker extends TypeChecker {
    MethodsFunctionsChecker mfc;
    List<String> visitedClasses;

    public MethodsFirstChecker(Context c, Factory fac, ErrorEmitter errorEmitter) {
        super(c, fac, errorEmitter);
        mfc = new MethodsFunctionsChecker(this);
        visitedClasses = new ArrayList<>();
    }

    @Override
    public <T> void visitCtClass(CtClass<T> ctClass) {
        if (errorEmitter.foundError())
            return;

        System.out.println("CTCLASS:" + ctClass.getSimpleName());
        context.reinitializeContext();
        if (visitedClasses.contains(ctClass.getQualifiedName()))
            return;
        else
            visitedClasses.add(ctClass.getQualifiedName());
        // visitInterfaces
        if (!ctClass.getSuperInterfaces().isEmpty())
            for (CtTypeReference<?> t : ctClass.getSuperInterfaces()) {
                if (t.isInterface()) {
                    CtType ct = t.getDeclaration();
                    if (ct instanceof CtInterface)
                        visitCtInterface((CtInterface<?>) ct);
                }

            }
        // visitSubclasses
        CtTypeReference<?> sup = ctClass.getSuperclass();
        if (sup != null && sup.isClass()) {
            CtType ct = sup.getDeclaration();
            if (ct instanceof CtClass)
                visitCtClass((CtClass<?>) ct);
        }
        try {
            getRefinementFromAnnotation(ctClass);
        } catch (ParsingException e) {
            return;// error already in ErrorEmitter
        }
        handleStateSetsFromAnnotation(ctClass);
        super.visitCtClass(ctClass);
    }

    @Override
    public <T> void visitCtInterface(CtInterface<T> intrface) {
        if (errorEmitter.foundError())
            return;

        if (visitedClasses.contains(intrface.getQualifiedName()))
            return;
        else
            visitedClasses.add(intrface.getQualifiedName());
        if (getExternalRefinement(intrface).isPresent())
            return;

        try {
            getRefinementFromAnnotation(intrface);
        } catch (ParsingException e) {
            return;// error already in ErrorEmitter
        }
        handleStateSetsFromAnnotation(intrface);
        super.visitCtInterface(intrface);
    }

    @Override
    public <T> void visitCtConstructor(CtConstructor<T> c) {
        if (errorEmitter.foundError())
            return;

        context.enterContext();
        try {
            getRefinementFromAnnotation(c);
            mfc.getConstructorRefinements(c);
        } catch (ParsingException e) {
            return;
        }
        super.visitCtConstructor(c);
        context.exitContext();
    }

    public <R> void visitCtMethod(CtMethod<R> method) {
        if (errorEmitter.foundError())
            return;

        context.enterContext();
        try {
            mfc.getMethodRefinements(method);
        } catch (ParsingException e) {
            return;
        }
        super.visitCtMethod(method);
        context.exitContext();

    }

}
