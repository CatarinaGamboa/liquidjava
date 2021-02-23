package repair.regen.processor.refinement_checker;

import java.lang.annotation.Annotation;
import java.util.Optional;

import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.context.Context;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.visitor.CtScanner;

public class ExternalRefinementTypeChecker extends TypeChecker{
	Context context;
	String prefix;
	MethodsFunctionsChecker m;
	
	public ExternalRefinementTypeChecker(Context context) {
		super(context);
		this.context = context;
		System.out.println("ExternalRefinementTypeChecker");
	}

	@Override
	public <T> void visitCtClass(CtClass<T> ctClass) {
		return;
	}

	@Override
	public <T> void visitCtInterface(CtInterface<T> intrface) {
		Optional<String> externalRefinements = getExternalRefinement(intrface);
		if(externalRefinements.isPresent()) {
			prefix = externalRefinements.get();
			super.visitCtInterface(intrface);
		}
	}

	@Override
	public <T> void visitCtField(CtField<T> f) {
		Optional<Constraint> oc = getRefinementFromAnnotation(f);
		Constraint c = oc.isPresent()?oc.get():new Predicate();
		context.addGlobalVariableToContext(f.getSimpleName(), prefix, 
				f.getType(), c);
		super.visitCtField(f);
	}
	
	public <R> void visitCtMethod(CtMethod<R> method) {
		MethodsFunctionsChecker mfc = new MethodsFunctionsChecker(this);
		mfc.getMethodRefinements(method, prefix);
		super.visitCtMethod(method);
	}


	private Optional<String> getExternalRefinement(CtInterface<?> intrface) {
		Optional<String> ref = Optional.empty();
		for(CtAnnotation<? extends Annotation> ann :intrface.getAnnotations()) 
			if( ann.getActualAnnotation().annotationType().getCanonicalName()
					.contentEquals("repair.regen.specification.ExternalRefinementsFor")) {
				CtLiteral<String> s = (CtLiteral<String>) ann.getAllValues().get("value");
				ref = Optional.of(s.getValue());
			}		
		return ref;
	}

	@Override
	protected void checkSMT(Constraint refPar, CtElement invocation) {
		// TODO Auto-generated method stub
	}

	@Override
	void checkVariableRefinements(Constraint all, String name, CtVariable<?> declaration) {
		// TODO Auto-generated method stub
		
	}
	


	

}
