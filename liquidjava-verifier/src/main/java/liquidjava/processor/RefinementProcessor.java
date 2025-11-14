package liquidjava.processor;

import java.util.ArrayList;
import java.util.List;

import liquidjava.processor.ann_generation.FieldGhostsGeneration;
import liquidjava.processor.context.Context;
import liquidjava.processor.refinement_checker.ExternalRefinementTypeChecker;
import liquidjava.processor.refinement_checker.MethodsFirstChecker;
import liquidjava.processor.refinement_checker.RefinementTypeChecker;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.factory.Factory;

/** Finds circular dependencies between packages */
public class RefinementProcessor extends AbstractProcessor<CtPackage> {

    List<CtPackage> visitedPackages = new ArrayList<>();
    Factory factory;

    public RefinementProcessor(Factory factory) {
        this.factory = factory;
    }

    @Override
    public void process(CtPackage pkg) {
        if (!visitedPackages.contains(pkg)) {
            visitedPackages.add(pkg);
            Context c = Context.getInstance();
            c.reinitializeAllContext();

            pkg.accept(new FieldGhostsGeneration(c, factory)); // generate annotations for field ghosts
            pkg.accept(new ExternalRefinementTypeChecker(c, factory));
            pkg.accept(new MethodsFirstChecker(c, factory)); // double passing idea (instead of headers)
            pkg.accept(new RefinementTypeChecker(c, factory));
        }
    }
}
