package liquidjava.processor;

import java.util.ArrayList;
import java.util.List;

import liquidjava.diagnostics.Diagnostics;
import liquidjava.diagnostics.errors.LJError;
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
    Diagnostics diagnostics = Diagnostics.getInstance();

    public RefinementProcessor(Factory factory) {
        this.factory = factory;
    }

    @Override
    public void process(CtPackage pkg) {
        if (!visitedPackages.contains(pkg)) {
            visitedPackages.add(pkg);
            Context c = Context.getInstance();
            c.reinitializeAllContext();

            try {
                // process types in this package only, not sub-packages
                // first pass: gather refinements
                pkg.getTypes().forEach(type -> {
                    type.accept(new FieldGhostsGeneration(c, factory)); // generate annotations for field ghosts
                    type.accept(new ExternalRefinementTypeChecker(c, factory)); // process external refinements
                    type.accept(new MethodsFirstChecker(c, factory)); // double passing idea (instead of headers)
                });

                // second pass: check refinements
                pkg.getTypes().forEach(type -> {
                    type.accept(new RefinementTypeChecker(c, factory));
                });
            } catch (LJError e) {
                diagnostics.add(e);
            }
        }
    }
}
