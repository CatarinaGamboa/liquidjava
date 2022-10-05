package liquidjava.processor;

import java.util.ArrayList;
import java.util.List;

import liquidjava.errors.ErrorEmitter;
import liquidjava.processor.context.Context;
import liquidjava.processor.refinement_checker.ExternalRefinementTypeChecker;
import liquidjava.processor.refinement_checker.MethodsFirstChecker;
import liquidjava.processor.refinement_checker.RefinementTypeChecker;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.factory.Factory;

/**
 * Finds circular dependencies between packages
 */
public class RefinementProcessor extends AbstractProcessor<CtPackage> {

    List<CtPackage> visitedPackages = new ArrayList<>();
    Factory factory;
    ErrorEmitter errorEmitter;

    public RefinementProcessor(Factory factory, ErrorEmitter ee) {
        this.factory = factory;
        errorEmitter = ee;
    }

    @Override
    public void process(CtPackage pkg) {
        if (!visitedPackages.contains(pkg)) {
            visitedPackages.add(pkg);
            Context c = Context.getInstance();
            c.reinitializeAllContext();

            // void spoon.reflect.visitor.CtVisitable.accept(CtVisitor arg0)
            pkg.accept(new ExternalRefinementTypeChecker(c, factory, errorEmitter));

            pkg.accept(new MethodsFirstChecker(c, factory, errorEmitter));// double passing idea

            pkg.accept(new RefinementTypeChecker(c, factory, errorEmitter));
            if (errorEmitter.foundError())
                return;
        }

    }

}