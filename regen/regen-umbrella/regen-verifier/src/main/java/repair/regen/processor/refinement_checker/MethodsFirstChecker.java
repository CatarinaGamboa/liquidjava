package repair.regen.processor.refinement_checker;

import java.util.Optional;

import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.context.Context;
import repair.regen.processor.context.GhostFunction;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class MethodsFirstChecker extends TypeChecker{

	public MethodsFirstChecker(Context c, Factory fac) {
		super(c, fac);
	}
	
	@Override
	public <T> void visitCtClass(CtClass<T> ctClass) {
		System.out.println("First check on methods of:"+ctClass.getSimpleName());
		super.visitCtClass(ctClass);
	}
	
	@Override
	public <T> void visitCtConstructor(CtConstructor<T> c) {
		context.enterContext();
		MethodsFunctionsChecker mfc = new MethodsFunctionsChecker(this);
		mfc.getConstructorRefinements(c);
		getRefinementFromAnnotation(c); 
		super.visitCtConstructor(c);
		context.exitContext();
	}
	
	public <R> void visitCtMethod(CtMethod<R> method) {
		context.enterContext();
		MethodsFunctionsChecker mfc = new MethodsFunctionsChecker(this);
		mfc.getMethodRefinements(method);
		super.visitCtMethod(method);
		context.exitContext();

	}

	@Override
	void checkVariableRefinements(Constraint refinementFound, String simpleName, CtTypeReference type,
			CtElement variable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void checkSMT(Constraint expectedType, CtElement element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean checkStateSMT(Constraint prevState, Constraint expectedState, CtElement target) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Optional<GhostFunction> createGhostFunction(String value, int set, CtElement element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void getGhostFunction(String value, CtElement element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleAlias(String value, CtElement element) {
		// TODO Auto-generated method stub
		
	}

}
