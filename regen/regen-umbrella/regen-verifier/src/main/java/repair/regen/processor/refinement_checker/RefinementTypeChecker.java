package repair.regen.processor.refinement_checker;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import repair.regen.language.function.FunctionDeclaration;
import repair.regen.language.parser.RefinementParser;
import repair.regen.language.parser.SyntaxException;
import repair.regen.processor.constraints.Conjunction;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.EqualsPredicate;
import repair.regen.processor.constraints.IfThenElse;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.context.Context;
import repair.regen.processor.context.GhostFunction;
import repair.regen.processor.context.RefinedVariable;
import repair.regen.processor.context.Variable;
import repair.regen.processor.context.VariableInstance;
import spoon.reflect.code.CtArrayWrite;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtConditional;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFieldRead;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtNewArray;
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
import spoon.support.reflect.code.CtVariableWriteImpl;
import spoon.support.reflect.reference.CtArrayTypeReferenceImpl;

public class RefinementTypeChecker extends TypeChecker {
	// This class should do the following:

	// 1. Keep track of the context variable types
	// 2. Do type checking and inference
	VCChecker vcChecker = new VCChecker();
	Factory factory;

	//Auxiliar TypeCheckers
	OperationsChecker otc;
	MethodsFunctionsChecker mfc;

	public RefinementTypeChecker(Context context,Factory factory) {
		super(context);
		this.factory = factory;
		otc = new OperationsChecker(this);
		mfc = new MethodsFunctionsChecker(this);
		System.out.println("In RefinementTypeChecker");
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
		context.enterContext();
		mfc.getMethodRefinements(method);
		super.visitCtMethod(method);
		context.exitContext();

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

//			if(localVariable.getType() instanceof CtArrayTypeReferenceImpl)
//				checkArray(localVariable);
		}
	}


	@Override
	public <T> void visitCtNewArray(CtNewArray<T> newArray) {
		super.visitCtNewArray(newArray);
		List<CtExpression<Integer>> l = newArray.getDimensionExpressions();
		//TODO only working for 1 dimension
		for(CtExpression<?> exp:l) {
			Constraint c = getExpressionRefinements(exp);
			EqualsPredicate ep = new EqualsPredicate("length("+WILD_VAR+")", c);
			newArray.putMetadata(REFINE_KEY, ep);
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

//			if(varDecl.getType() instanceof CtArrayTypeReferenceImpl)
//				checkArray(varDecl);

		}
		if(ex instanceof CtArrayWrite) {
			Constraint c = getRefinement(ex);
			//TODO continue
			//c.substituteVariable(WILD_VAR, );
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
		System.out.println("fieldRead:"+fieldRead);
		System.out.println(fieldRead.getVariable().getSimpleName());
		String fieldName = fieldRead.toString().replace("(", "").replace(")", "");
		if(context.hasVariable(fieldName)) {
			Constraint c = context.getVariableRefinements(fieldName);
			fieldRead.putMetadata(REFINE_KEY, c);
		} else if(fieldRead.getVariable().getSimpleName().equals("length")) {
			String targetName = fieldRead.getTarget().toString();
			System.out.println("Target:"+targetName);
			fieldRead.putMetadata(REFINE_KEY, new Predicate("length("+targetName+")"));
		}else{
			fieldRead.putMetadata(REFINE_KEY, new Predicate());
			//TODO DO WE WANT THIS OR TO SHOW ERROR MESSAGE
		}

		super.visitCtFieldRead(fieldRead);
	}


	@Override
	public <T> void visitCtVariableRead(CtVariableRead<T> variableRead) {
		super.visitCtVariableRead(variableRead);
		CtVariable<T> varDecl = variableRead.getVariable().getDeclaration();
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
		CtExpression<Boolean> exp = ifElement.getCondition();
		context.variablesNewIfCombination();
		context.variablesSetBeforeIf();
		context.enterContext();
		
		Constraint expRefs = getExpressionRefinements(exp);
		String freshVarName = FRESH+context.getCounter();
		expRefs = expRefs.substituteVariable(WILD_VAR, freshVarName);
		Constraint lastExpRefs = substituteAllVariablesForLastInstance(expRefs);
		expRefs = Conjunction.createConjunction(expRefs, lastExpRefs);
		
		//TODO Change in future
		if(expRefs.getVariableNames().contains("null"))
			expRefs = new Predicate();
		
				
		RefinedVariable freshRV = context.addInstanceToContext(freshVarName, 
				factory.Type().INTEGER_PRIMITIVE, expRefs);
		vcChecker.addPathVariable(freshRV);

		//VISIT THEN
		context.enterContext();
		visitCtBlock(ifElement.getThenStatement());
		context.variablesSetThenIf();
		context.exitContext();
		

		//VISIT ELSE
		if(ifElement.getElseStatement() != null) {
			context.getVariableByName(freshVarName);
			//expRefs = expRefs.negate();
			context.newRefinementToVariableInContext(freshVarName, expRefs.negate());
			
			context.enterContext();
			visitCtBlock(ifElement.getElseStatement());		
			context.variablesSetElseIf();
			context.exitContext();
		}
		//end
		vcChecker.removePathVariable(freshRV);
		context.exitContext();
		context.variablesCombineFromIf(expRefs);
		context.variablesFinishIfCombination();
	}
	
	
	@Override
	public <T> void visitCtArrayWrite(CtArrayWrite<T> arrayWrite) {
		super.visitCtArrayWrite(arrayWrite);
		CtExpression<?> index = arrayWrite.getIndexExpression();
		arrayWrite.putMetadata(REFINE_KEY, 
				String.format("addToIndex(%s, %s, %s)", arrayWrite.getTarget(), 
						index, WILD_VAR));
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
		Constraint c= new IfThenElse(cond, getRefinement(conditional.getThenExpression()), 
				getRefinement(conditional.getElseExpression()));
		conditional.putMetadata(REFINE_KEY, c);

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

	@Override
	void checkVariableRefinements(Constraint refinementFound, String simpleName, CtVariable<?> variable) {
		Optional<Constraint> expectedType = getRefinementFromAnnotation(variable);
		Constraint cEt = expectedType.isPresent()?expectedType.get():new Predicate();

		cEt = cEt.substituteVariable(WILD_VAR, simpleName);
		Constraint cet = cEt.substituteVariable(WILD_VAR, simpleName);

		String newName = String.format(instanceFormat, simpleName, context.getCounter());
		Constraint correctNewRefinement = refinementFound.substituteVariable(WILD_VAR, newName);
		cEt = cEt.substituteVariable(simpleName, newName);

		//Substitute variable in verification
		RefinedVariable rv= context.addInstanceToContext(newName, variable.getType(), correctNewRefinement);
		context.addRefinementInstanceToVariable(simpleName, newName);
		//smt check
		checkSMT(cEt, variable);//TODO CHANGE
		context.addRefinementToVariableInContext(variable, cet);




	}





	//############################### Get Metadata ##########################################

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
		if(ovi.isPresent())
			cref = new EqualsPredicate(WILD_VAR, ovi.get().getName());

		variable.putMetadata(REFINE_KEY, cref);
	}


	public Optional<Constraint> getRefinementFromAnnotation(CtElement element) {
		Optional<Constraint> constr = Optional.empty();
		Optional<String> ref = Optional.empty();
		for(CtAnnotation<? extends Annotation> ann :element.getAnnotations()) { 
			String an = ann.getActualAnnotation().annotationType().getCanonicalName();
			if( an.contentEquals("repair.regen.specification.Refinement")) {
				CtLiteral<String> s = (CtLiteral<String>) ann.getAllValues().get("value");
				ref = Optional.of(s.getValue());
			}else if( an.contentEquals("repair.regen.specification.RefinementFunction")) {
				CtLiteral<String> s = (CtLiteral<String>) ann.getAllValues().get("value");
				getGhostFunction(s.getValue());
				ref = Optional.empty();
			}
		}
		if(ref.isPresent()) 
			constr = Optional.of(new Predicate(ref.get()));

		return constr;
	}

	private void getGhostFunction(String value) {
		try {
			Optional<FunctionDeclaration> ofd = 
					RefinementParser.parseFunctionDecl(value);
			if(ofd.isPresent()) {
				GhostFunction gh = new GhostFunction(ofd.get(), factory); 
				context.addGhostFunction(gh);
				System.out.println(gh.toString());
			}

		} catch (SyntaxException e) {
			System.out.println("Ghost Function not well written");//TODO REVIEW MESSAGE
			e.printStackTrace();
		}

	}

	@Override
	void checkSMT(Constraint expectedType, CtElement element) {
		vcChecker.processSubtyping(expectedType, element);
		element.putMetadata(REFINE_KEY, expectedType);	
	}




	//	private Optional<String> getExternalRefinement(CtInterface<?> intrface) {
	//		Optional<String> ref = Optional.empty();
	//		for(CtAnnotation<? extends Annotation> ann :intrface.getAnnotations()) 
	//			if( ann.getActualAnnotation().annotationType().getCanonicalName()
	//					.contentEquals("repair.regen.specification.ExternalRefinementsFor")) {
	//				CtLiteral<String> s = (CtLiteral<String>) ann.getAllValues().get("value");
	//				ref = Optional.of(s.getValue());
	//			}		
	//		return ref;
	//	}

}
