package repair.regen.processor.refinement_checker;

import java.lang.annotation.Annotation;
import java.util.Optional;

import repair.regen.language.function.FunctionDeclaration;
import repair.regen.language.parser.RefinementParser;
import repair.regen.language.parser.SyntaxException;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.context.Context;
import repair.regen.processor.context.GhostFunction;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.CtScanner;

public class ExternalRefinementTypeChecker extends TypeChecker{
	String prefix;
	MethodsFunctionsChecker m;
	
	public ExternalRefinementTypeChecker(Context context, Factory fac) {
		super(context, fac);
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
		System.out.println("visited method external");
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
	
	protected void getGhostFunction(String value, CtElement element) {
		try {
			Optional<FunctionDeclaration> ofd = 
					RefinementParser.parseFunctionDecl(value);
			if(ofd.isPresent() && element.getParent() instanceof CtInterface<?>) {
				String[] a = prefix.split("\\.");
				String d =  a[a.length-1];
				GhostFunction gh = new GhostFunction(ofd.get(), factory,prefix,a[a.length-1]); 
				context.addGhostFunction(gh);
				System.out.println(gh.toString());
			}

		} catch (SyntaxException e) {
			System.out.println("Ghost Function not well written");//TODO REVIEW MESSAGE
			e.printStackTrace();
		}
	}

	@Override
	protected void checkSMT(Constraint refPar, CtElement invocation) {
		// TODO Auto-generated method stub
	}
	
	@Override
	protected void handleAlias(String value, CtElement element) {
		// TODO Auto-generated method stub
		
	}
	


	@Override
	void checkVariableRefinements(Constraint refinementFound, String simpleName, CtTypeReference type,
			CtElement variable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void checkStateSMT(Constraint prevState, Constraint expectedState, CtExpression<?> target) {
		// TODO Auto-generated method stub
		
	}




	

}
