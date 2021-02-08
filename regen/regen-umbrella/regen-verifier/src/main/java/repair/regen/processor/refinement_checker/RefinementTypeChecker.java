package repair.regen.processor.refinement_checker;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import repair.regen.processor.built_ins.RefinementsLibrary;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.EqualsPredicate;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.context.Context;
import repair.regen.processor.context.RefinedVariable;
import repair.regen.processor.context.VariableInstance;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtConditional;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFieldRead;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtUnaryOperator;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtAnnotationType;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtVariableReference;
import spoon.reflect.visitor.CtScanner;
import spoon.support.reflect.code.CtVariableWriteImpl;

public class RefinementTypeChecker extends CtScanner {
	// This class should do the following:

	// 1. Keep track of the context variable types
	// 2. Do type checking and inference
	final String REFINE_KEY = "refinement";
	final String WILD_VAR = "_";
	final String FRESH = "FRESH_";

	Context context = Context.getInstance();
	VCChecker vcChecker = new VCChecker();
	RefinementsLibrary lib = new RefinementsLibrary(WILD_VAR);

	Factory factory;
	
	//Auxiliar TypeCheckers
	OperationsChecker otc;
	MethodsFunctionsChecker mfc;
	
	String[] implementedTypes = {"boolean", "int", "short", "long", "float","double"}; //TODO add types

	public RefinementTypeChecker(Factory factory) {
		this.factory = factory;
		otc = new OperationsChecker(this);
		mfc = new MethodsFunctionsChecker(this);
	}

	//--------------------- Visitors -----------------------------------
	@Override
	public <T> void visitCtClass(CtClass<T> ctClass) {
		System.out.println("CTCLASS:"+ctClass.getSimpleName());
		context.reinitializeContext();
		super.visitCtClass(ctClass);
	}
	@Override
	public <A extends Annotation> void visitCtAnnotation(CtAnnotation<A> annotation) {
		super.visitCtAnnotation(annotation);
	}

	@Override
	public <A extends Annotation> void visitCtAnnotationType(CtAnnotationType<A> annotationType) {
		//VISITS CREATION OF ANNOTATIONS
		//Probably will have to use the context to store annotations
		//		System.out.println("AnnTYpe:"+annotationType);
		//		for(CtAnnotation ann : annotationType.getAnnotations())
		//			if( ann.getActualAnnotation().annotationType().getCanonicalName()
		//				.contentEquals("repair.regen.specification.Refinement")) {
		//				CtLiteral<String> s = (CtLiteral<String>) ann.getAllValues().get("value");
		//				annotationType.putMetadata(REFINE_KEY, s);
		//			}

		super.visitCtAnnotationType(annotationType);
	}


	public <R> void visitCtMethod(CtMethod<R> method) {
		enterContexts();
		mfc.getMethodRefinements(method);
		super.visitCtMethod(method);
		exitContexts();

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> void visitCtLocalVariable(CtLocalVariable<T> localVariable) {
		super.visitCtLocalVariable(localVariable);
		//only declaration, no assignment
		if(localVariable.getAssignment() == null) {
			Optional<Constraint> a = getRefinementFromAnnotation(localVariable);
			context.addVarToContext(localVariable.getSimpleName(), localVariable.getType(), 
					a.isPresent()? a.get() : new Predicate());
		}else {
			String varName = localVariable.getSimpleName();
			CtExpression<?> e = localVariable.getAssignment();
			Constraint refinementFound = getRefinement(e);

			if (refinementFound == null)
				refinementFound = new Predicate();
			context.addVarToContext(varName, localVariable.getType(), new Predicate());
			checkVariableRefinements(refinementFound, varName, localVariable);
				
		}
	}

	@Override
	public <T,A extends T> void visitCtAssignment(CtAssignment<T,A> assignement) {
		super.visitCtAssignment(assignement);
		CtExpression<T> ex =  assignement.getAssigned();

		if (ex instanceof CtVariableWriteImpl) {
			CtVariableReference<?> var = ((CtVariableAccess<?>) ex).getVariable();
			CtVariable<T> varDecl = (CtVariable<T>) var.getDeclaration();
			String name = var.getSimpleName();

			getPutVariableMetadada(ex, varDecl);

			Constraint refinementFound = getRefinement(assignement.getAssignment());
			if (refinementFound == null) {
				refinementFound = new Predicate();
			}
			Optional<VariableInstance> r = context.getLastVariableInstance(name);
			if(r.isPresent())
				vcChecker.removePathVariableThatIncludes(r.get().getName());//AQUI!!

			vcChecker.removePathVariableThatIncludes(name);//AQUI!!
			checkVariableRefinements(refinementFound, name, varDecl);

		}
	}

	@Override
	public <T> void visitCtLiteral(CtLiteral<T> lit) {
		List<String> types = Arrays.asList(implementedTypes);
		if (types.contains(lit.getType().getQualifiedName())) {
			lit.putMetadata(REFINE_KEY, new EqualsPredicate(WILD_VAR, lit.getValue().toString()));
		}else if(lit.getType().getQualifiedName().contentEquals("java.lang.String")){
			//Only taking care of strings inside refinements
		}else {
			System.out.println(String.format("Literal of type %s not implemented:",
					lit.getType().getQualifiedName()));
		}
	}	
	
	
	@Override
	public <T> void visitCtFieldRead(CtFieldRead<T> fieldRead) {
		if(fieldRead.getTarget().toString().equals("java.lang.Math")) {
			String var = fieldRead.getVariable().toString();
			if(lib.getRefinement(var).isPresent()) {
				System.out.println(lib.getRefinement(var).get());
				fieldRead.putMetadata(REFINE_KEY, new Predicate(lib.getRefinement(var).get()));
			}
		}
		super.visitCtFieldRead(fieldRead);
	}


	@Override
	public <T> void visitCtVariableRead(CtVariableRead<T> variableRead) {
		super.visitCtVariableRead(variableRead);
		CtVariable<T> varDecl = variableRead.getVariable().getDeclaration();
		String name = varDecl.getSimpleName();
		addRefinementVariable(context.getVariableByName(name));
		getPutVariableMetadada(variableRead, varDecl);
	}

	/**
	 * Visitor for binary operations
	 * Adds metadata to the binary operations from the operands
	 */
	@Override
	public <T> void visitCtBinaryOperator(CtBinaryOperator<T> operator) {
		super.visitCtBinaryOperator(operator);
		otc.getBinaryOpRefinements(operator);

	}

	@Override
	public <T> void visitCtUnaryOperator(CtUnaryOperator<T> operator) {
		super.visitCtUnaryOperator(operator);
		otc.getUnaryOpRefinements(operator);

	}

	public <R> void visitCtInvocation(CtInvocation<R> invocation) {
		super.visitCtInvocation(invocation);
		mfc.getInvocationRefinements(invocation);
	}

	@Override
	public <R> void visitCtReturn(CtReturn<R> ret) {
		super.visitCtReturn(ret);
		mfc.getReturnRefinements(ret);

	}


	@Override
	public void visitCtIf(CtIf ifElement) {
		//TODO REVER
		CtExpression<Boolean> exp = ifElement.getCondition();
		context.variablesSetBeforeIf();
		enterContexts();
		
		Constraint expRefs = getExpressionRefinements(exp);
		String freshVarName = FRESH+context.getCounter();
		Constraint nExpRefs = expRefs.substituteVariable(WILD_VAR, freshVarName);
		nExpRefs = substituteAllVariablesForLastInstance(nExpRefs);
		
		
		RefinedVariable freshRV = context.addVarToContext(freshVarName, 
				factory.Type().INTEGER_PRIMITIVE, nExpRefs);
		vcChecker.addPathVariable(freshRV);
		
		//VISIT THEN
		enterContexts();
		visitCtBlock(ifElement.getThenStatement());
		context.variablesSetThenIf();
		exitContexts();

		//VISIT ELSE
		if(ifElement.getElseStatement() != null) {
			context.getVariableByName(freshVarName);
			expRefs = expRefs.negate();
			context.newRefinementToVariableInContext(freshVarName, expRefs);
			enterContexts();
			visitCtBlock(ifElement.getElseStatement());
			context.variablesSetElseIf();
			exitContexts();
		}
		//end
		vcChecker.removePathVariable(freshRV);
		exitContexts();
		context.variablesCombineFromIf();
	}



	private Constraint substituteAllVariablesForLastInstance(Constraint c) {
		Constraint ret = c;
		List<String> ls = c.getVariableNames();
		for(String s:ls) {
			Optional<VariableInstance> rv = context.getLastVariableInstance(s);
			if(rv.isPresent()) {
				VariableInstance vi = rv.get();
				ret = ret.substituteVariable(s, vi.getName());
			}
		}
		return ret;
	}

	@Override
	public <T> void visitCtConditional(CtConditional<T> conditional) {
		super.visitCtConditional(conditional);
		Constraint cond = getRefinement(conditional.getCondition());
		String condRefs = cond.toString(); 
		String condThen = "!(" + condRefs + ") || ("+ getRefinement(conditional.getThenExpression())+")", //!A or B
				notCondElse = "("+condRefs + ") || ("+ getRefinement(conditional.getElseExpression())+")";//A or C

		conditional.putMetadata(REFINE_KEY, new Predicate("("+condThen+") && ("+notCondElse+")"));//TODO CHANGE TO CONJUNCTION
	}


	//------------------------------- Auxiliary Methods ----------------------------------------
	//############################### Variables and Context handling  ##########################################
	private void renewVariables() {
		//vcChecker.renewVariables();
	}
	void addRefinementVariable(RefinedVariable var) {
		//vcChecker.addRefinementVariable(var);
	}
	private void enterContexts() {
		context.enterContext();
		//vcChecker.enterContext();
	}
	private void exitContexts() {
		context.exitContext();
		//vcChecker.exitContext();
	}

	//############################### Inner Visitors  ##########################################

	private Constraint getExpressionRefinements(CtExpression element) {
		if(element instanceof CtVariableRead<?>) {
			CtVariableRead<?> elemVar = (CtVariableRead<?>) element;
			return getRefinement(element);
		}else if(element instanceof CtBinaryOperator<?>) {
			CtBinaryOperator<?> binop = (CtBinaryOperator<?>) element;
			visitCtBinaryOperator(binop);
			return getRefinement(binop);
		}else if(element instanceof CtUnaryOperator<?>) {
			CtUnaryOperator<?> op = (CtUnaryOperator<?>) element;
			visitCtUnaryOperator(op);
			return getRefinement(op);
		}else if (element instanceof CtLiteral<?>) {
			CtLiteral<?> l = (CtLiteral<?>) element;
			return new Predicate(l.getValue().toString());
		}else if(element instanceof CtInvocation<?>) {
			CtInvocation<?> inv = (CtInvocation<?>) element;
			visitCtInvocation(inv);
			return getRefinement(inv);
		}
		return getRefinement(element);
	}


	//############################### SMT Evaluation ##########################################
	<T> void checkVariableRefinements(Constraint refinementFound, String simpleName, CtVariable<T> variable) {
		Optional<String> expectedType = variable.getAnnotations().stream()
				.filter(
						ann -> ann.getActualAnnotation().annotationType().getCanonicalName()
						.contentEquals("repair.regen.specification.Refinement")
						).map(
								ann -> (CtLiteral<String>) ann.getAllValues().get("value")
								).map(
										str -> str.getValue()
										).findAny();
		expectedType.ifPresent((et) -> {
			Constraint cEt = new Predicate(et);
			cEt = cEt.substituteVariable(WILD_VAR, simpleName);
			Constraint cet = cEt.substituteVariable(WILD_VAR, simpleName);
			//create new variable for validation
			//Ex: @Ref(a>5) a = 10; VC becomes: a__0 == 10 -> a__0 > 5
			
			String newName = simpleName+"_"+context.getCounter()+"_";
			Constraint correctNewRefinement = refinementFound.substituteVariable(WILD_VAR, newName);
			cEt = cEt.substituteVariable(simpleName, newName);
			
			//Substitute variable in verification
			RefinedVariable rv= context.addInstanceToContext(newName, variable.getType(), correctNewRefinement);
			context.addRefinementInstanceToVariable(simpleName, newName);
			addRefinementVariable(rv);//TODO REMOVE
			//smt check
			checkSMT(cEt, variable);//TODO CHANGE
			context.addRefinementToVariableInContext(variable, cet);
		});
	}
	
	<T> void checkSMT(Constraint expectedType, CtElement element) {
		vcChecker.processSubtyping(expectedType, element);
		element.putMetadata(REFINE_KEY, expectedType);	
		renewVariables();
	}
	


	//############################### Get Metadata ##########################################
	Constraint getRefinement(CtElement elem) {
		return (Constraint)elem.getMetadata(REFINE_KEY);

	}
	/**
	 * 
	 * @param <T>
	 * @param variable
	 * @param varDecl Cannot be null
	 */
	private <T> void getPutVariableMetadada(CtElement variable, CtVariable<T> varDecl) {
		String name = varDecl.getSimpleName();
		Constraint cref = new EqualsPredicate(WILD_VAR, name);
		Optional<VariableInstance> ovi = context.getLastVariableInstance(name);
		if(ovi.isPresent()) {
			RefinedVariable vi = ovi.get();
			addRefinementVariable(vi);
			cref = new EqualsPredicate(WILD_VAR, vi.getName());
		}
		
		variable.putMetadata(REFINE_KEY, cref);
	}
	

	public Optional<Constraint> getRefinementFromAnnotation(CtElement element) {
		Optional<Constraint> constr = Optional.empty();
		Optional<String> ref = Optional.empty();
		for(CtAnnotation<? extends Annotation> ann :element.getAnnotations()) 
			if( ann.getActualAnnotation().annotationType().getCanonicalName()
					.contentEquals("repair.regen.specification.Refinement")) {
				CtLiteral<String> s = (CtLiteral<String>) ann.getAllValues().get("value");
				ref = Optional.of(s.getValue());
			}		
		if(ref.isPresent()) 
			constr = Optional.of(new Predicate(ref.get()));
		
		return constr;
	}

}
