package liquidjava.processor.refinement_checker;

import static liquidjava.diagnostics.Diagnostics.diagnostics;

import java.util.ArrayList;
import java.util.List;

import liquidjava.processor.context.Context;
import liquidjava.processor.refinement_checker.general_checkers.MethodsFunctionsChecker;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

/** First visit to Spoon AST to get the method's refinements */
public class MethodsFirstChecker extends TypeChecker {
    MethodsFunctionsChecker mfc;
    List<String> visitedClasses;

    public MethodsFirstChecker(Context context, Factory factory) {
        super(context, factory);
        mfc = new MethodsFunctionsChecker(this);
        visitedClasses = new ArrayList<>();
    }

    @Override
    public <T> void visitCtClass(CtClass<T> ctClass) {
        context.reinitializeContext();
        if (visitedClasses.contains(ctClass.getQualifiedName()))
            return;
        else
            visitedClasses.add(ctClass.getQualifiedName());
        // visitInterfaces
        if (!ctClass.getSuperInterfaces().isEmpty())
            for (CtTypeReference<?> t : ctClass.getSuperInterfaces()) {
                if (t.isInterface()) {
                    CtType<?> ct = t.getDeclaration();
                    if (ct instanceof CtInterface)
                        visitCtInterface((CtInterface<?>) ct);
                }
            }
        // visitSubclasses
        CtTypeReference<?> sup = ctClass.getSuperclass();
        if (sup != null && sup.isClass()) {
            CtType<?> ct = sup.getDeclaration();
            if (ct instanceof CtClass)
                visitCtClass((CtClass<?>) ct);
        }
        getRefinementFromAnnotation(ctClass);
        handleStateSetsFromAnnotation(ctClass);
        super.visitCtClass(ctClass);
    }

    @Override
    public <T> void visitCtInterface(CtInterface<T> intrface) {
        if (visitedClasses.contains(intrface.getQualifiedName()))
            return;
        else
            visitedClasses.add(intrface.getQualifiedName());
        if (getExternalRefinement(intrface).isPresent())
            return;

        getRefinementFromAnnotation(intrface);
        handleStateSetsFromAnnotation(intrface);
        super.visitCtInterface(intrface);
    }

    @Override
    public <T> void visitCtConstructor(CtConstructor<T> c) {
        context.enterContext();
        getRefinementFromAnnotation(c);
        mfc.getConstructorRefinements(c);
        super.visitCtConstructor(c);
        context.exitContext();
    }

    public <R> void visitCtMethod(CtMethod<R> method) {
        context.enterContext();
        mfc.getMethodRefinements(method);
        super.visitCtMethod(method);
        context.exitContext();
    }
}
