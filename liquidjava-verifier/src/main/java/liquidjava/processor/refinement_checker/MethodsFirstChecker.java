package liquidjava.processor.refinement_checker;

import java.util.ArrayList;
import java.util.List;

import static liquidjava.diagnostics.Diagnostics.diagnostics;

import liquidjava.diagnostics.errors.LJError;
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
        try {
            getRefinementFromAnnotation(ctClass);
            handleStateSetsFromAnnotation(ctClass);
        } catch (LJError e) {
            diagnostics.add(e);
            return;
        }
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

        try {
            getRefinementFromAnnotation(intrface);
            handleStateSetsFromAnnotation(intrface);
        } catch (LJError e) {
            diagnostics.add(e);
            return;
        }
        super.visitCtInterface(intrface);
    }

    @Override
    public <T> void visitCtConstructor(CtConstructor<T> c) {
        context.enterContext();
        try {
            getRefinementFromAnnotation(c);
            mfc.getConstructorRefinements(c);
        } catch (LJError e) {
            diagnostics.add(e);
            return;
        }
        super.visitCtConstructor(c);
        context.exitContext();
    }

    public <R> void visitCtMethod(CtMethod<R> method) {
        context.enterContext();
        try {
            mfc.getMethodRefinements(method);
        } catch (LJError e) {
            diagnostics.add(e);
            return;
        }
        super.visitCtMethod(method);
        context.exitContext();
    }
}
