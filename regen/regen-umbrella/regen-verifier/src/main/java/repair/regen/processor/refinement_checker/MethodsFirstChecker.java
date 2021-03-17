package repair.regen.processor.refinement_checker;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import repair.regen.language.function.FunctionDeclaration;
import repair.regen.language.parser.RefinementParser;
import repair.regen.language.parser.SyntaxException;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.context.Context;
import repair.regen.processor.context.GhostFunction;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class MethodsFirstChecker extends TypeChecker{

	public MethodsFirstChecker(Context c, Factory fac) {
		super(c, fac);
	}
	
	@Override
	public <T> void visitCtClass(CtClass<T> ctClass) {
		System.out.println("CTCLASS:"+ctClass.getSimpleName());
		context.reinitializeContext();
		//visitInterfaces
		if(!ctClass.getSuperInterfaces().isEmpty())
			for(CtTypeReference<?> t :ctClass.getSuperInterfaces()) {
				if(t.isInterface()) {
					CtType ct = t.getDeclaration();
					if(ct instanceof CtInterface)				
						visitCtInterface((CtInterface<?>) ct);
				}

			}
		//visitSubclasses
		CtTypeReference<?> sup = ctClass.getSuperclass();
		if(sup != null && sup.isClass()){
			CtType ct = sup.getDeclaration();
			if(ct instanceof CtClass)				
				visitCtClass((CtClass<?>) ct);
		}
		getRefinementFromAnnotation(ctClass);
		handleStateSetsFromAnnotation(ctClass);
		super.visitCtClass(ctClass);
	}

	@Override
	public <T> void visitCtInterface(CtInterface<T> intrface) {
		System.out.println("CT INTERFACE: " +intrface.getSimpleName());
		if(getExternalRefinement(intrface).isPresent())
			return;
		getRefinementFromAnnotation(intrface);
		handleStateSetsFromAnnotation(intrface);
		super.visitCtInterface(intrface);
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
	protected Optional<GhostFunction> createGhostFunction(String value, int set, CtElement element) {
	CtClass<?> klass = null; 
		
		if(element.getParent() instanceof CtClass<?>) {
			klass =(CtClass<?>) element.getParent();
		}else if(element instanceof CtClass<?>) {
			klass = (CtClass<?>) element;
		}
		if(klass != null) {
			CtTypeReference<?> ret = factory.Type().BOOLEAN_PRIMITIVE;
			List<String> params = Arrays.asList(klass.getSimpleName());
			GhostFunction gh = new GhostFunction(value, params, ret , factory, 
												klass.getQualifiedName(), klass.getSimpleName(), set); 
//			context.addGhostFunction(gh);
			System.out.println(gh.toString());
			return Optional.of(gh);
		}
		return Optional.empty();
	}

	@Override
	protected void getGhostFunction(String value, CtElement element) {//Same as in Refinement Checker
		try {
			Optional<FunctionDeclaration> ofd = 
					RefinementParser.parseFunctionDecl(value);
			if(ofd.isPresent() && element.getParent() instanceof CtClass<?>) {
				CtClass<?> klass =(CtClass<?>) element.getParent(); 
				GhostFunction gh = new GhostFunction(ofd.get(), factory, klass.getQualifiedName(), klass.getSimpleName()); 
				context.addGhostFunction(gh);
				System.out.println(gh.toString());
			}

		} catch (SyntaxException e) {
			System.out.println("Ghost Function not well written");//TODO REVIEW MESSAGE
			e.printStackTrace();
		}
		
	}



}
