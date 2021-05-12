package repair.regen.processor.refinement_checker;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import repair.regen.errors.ErrorEmitter;
import repair.regen.errors.ErrorHandler;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.context.Context;
import repair.regen.processor.context.GhostFunction;
import repair.regen.processor.facade.GhostDTO;
import repair.regen.processor.refinement_checker.general_checkers.MethodsFunctionsChecker;
import repair.regen.rj_language.ParsingException;
import repair.regen.rj_language.RefinementsParser;
import repair.regen.utils.Pair;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class ExternalRefinementTypeChecker extends TypeChecker{
	String prefix;
	MethodsFunctionsChecker m;
	
	public ExternalRefinementTypeChecker(Context context, Factory fac, ErrorEmitter errorEmitter) {
		super(context, fac, errorEmitter);
		System.out.println("ExternalRefinementTypeChecker");
	}

	@Override
	public <T> void visitCtClass(CtClass<T> ctClass) {
		return;
	}

	@Override
	public <T> void visitCtInterface(CtInterface<T> intrface) {
		if(errorEmitter.foundError()) return;
		
		Optional<String> externalRefinements = getExternalRefinement(intrface);
		if(externalRefinements.isPresent()) {
			prefix = externalRefinements.get();
			getRefinementFromAnnotation(intrface);
			handleStateSetsFromAnnotation(intrface);
			super.visitCtInterface(intrface);
		}
	}

	@Override
	public <T> void visitCtField(CtField<T> f) {
		if(errorEmitter.foundError()) return;
		
		Optional<Constraint> oc = getRefinementFromAnnotation(f);
		Constraint c = oc.isPresent()?oc.get():new Predicate();
		context.addGlobalVariableToContext(f.getSimpleName(), prefix, 
				f.getType(), c);
		super.visitCtField(f);
	}
	
	public <R> void visitCtMethod(CtMethod<R> method) {
		if(errorEmitter.foundError()) return;
		
		MethodsFunctionsChecker mfc = new MethodsFunctionsChecker(this);
		mfc.getMethodRefinements(method, prefix);
		super.visitCtMethod(method);
	
//		
//		System.out.println("visited method external");
	}



	protected void getGhostFunction(String value, CtElement element) {
		try {
//			Optional<FunctionDeclaration> ofd = 
//					RefinementParser.parseFunctionDecl(value);
			GhostDTO f = RefinementsParser.getGhostDeclaration(value);
			if(f != null && element.getParent() instanceof CtInterface<?>) {
				String[] a = prefix.split("\\.");
				String d =  a[a.length-1];
				GhostFunction gh = new GhostFunction(f, factory,prefix,a[a.length-1]); 
				context.addGhostFunction(gh);
				System.out.println(gh.toString());
			}

		} catch (ParsingException e) {
			ErrorHandler.printCostumeError(element, 
					"Could not parse the Ghost Function"+e.getMessage(), errorEmitter);
//			e.printStackTrace();
		}
	}
	

	@Override
	protected Optional<GhostFunction> createStateGhost(int order, CtElement element){
		String[] a = prefix.split("\\.");
		String klass =  a[a.length-1];
		if(klass != null) {
			CtTypeReference<?> ret = factory.Type().INTEGER_PRIMITIVE;
			List<String> params = Arrays.asList(klass);
			GhostFunction gh = new GhostFunction(String.format("%s_state%d", klass.toLowerCase(), order), 
					params, ret , factory, 
					prefix, klass); 
			System.out.println(gh.toString());
			return Optional.of(gh);
		}
		return Optional.empty();
	}

	
	@Override
	protected String getQualifiedClassName(CtElement elem) {
		return prefix;
	}
	
	@Override
	protected String getSimpleClassName(CtElement elem) {
		String[] a = prefix.split("\\.");
		return a[a.length-1];
	}
}
