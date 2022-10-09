package liquidjava.processor.ann_generation;

import liquidjava.errors.ErrorEmitter;
import liquidjava.processor.context.Context;
import liquidjava.specification.Ghost;
import spoon.reflect.declaration.*;
import spoon.reflect.factory.Factory;

import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.CtScanner;

public class FieldGhostsGeneration extends CtScanner {
    Context context;
    Factory factory;
    ErrorEmitter errorEmitter;

    public FieldGhostsGeneration(Context c, Factory fac, ErrorEmitter errorEmitter) {
        this.context = c;
        this.factory = fac;
        this.errorEmitter = errorEmitter;
    }

    public Context getContext() {
        return context;
    }

    public Factory getFactory() {
        return factory;
    }

    @Override
    public <T> void visitCtClass(CtClass<T> ctClass) {
        if (errorEmitter.foundError()) {
            return;
        }

        ctClass.getDeclaredFields().stream().filter(fld -> fld.getType().getQualifiedName().equals("int"))
                .forEach(fld -> {
                    System.out.println("generated field ghost in class " + ctClass.getQualifiedName() + " for field " +
                            fld.getSimpleName());
                    CtTypeReference<?> fld_type = fld.getType();
                    CtAnnotation<?> gen_ann = factory.createAnnotation(factory.createCtTypeReference(Ghost.class));
                    gen_ann.addValue("value", fld_type.getSimpleName() + " " + fld.getSimpleName());
                    ctClass.addAnnotation(gen_ann);
                });

        super.visitCtClass(ctClass);
    }
}
