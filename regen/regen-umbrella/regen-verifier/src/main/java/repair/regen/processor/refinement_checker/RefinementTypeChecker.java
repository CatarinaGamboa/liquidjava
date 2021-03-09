package repair.regen.processor.refinement_checker;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import repair.regen.language.alias.Alias;
import repair.regen.language.function.FunctionDeclaration;
import repair.regen.language.parser.RefinementParser;
import repair.regen.language.parser.SyntaxException;
import repair.regen.processor.constraints.Conjunction;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.EqualsPredicate;
import repair.regen.processor.constraints.FunctionPredicate;
import repair.regen.processor.constraints.IfThenElse;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.constraints.VariablePredicate;
import repair.regen.processor.context.AliasWrapper;
import repair.regen.processor.context.Context;
import repair.regen.processor.context.GhostFunction;
import repair.regen.processor.context.RefinedVariable;
import repair.regen.processor.context.Variable;
import repair.regen.processor.context.VariableInstance;
import spoon.reflect.code.CtArrayRead;
import spoon.reflect.code.CtArrayWrite;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtConditional;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFieldRead;
import spoon.reflect.code.CtFieldWrite;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtNewArray;
import spoon.reflect.code.CtNewClass;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtUnaryOperator;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.declaration.CtAnnotationType;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.reference.CtVariableReference;
import spoon.support.reflect.code.CtVariableWriteImpl;
import spoon.support.sniper.internal.ElementSourceFragment;

public class RefinementTypeChecker extends TypeChecker {
	// This class should do the following:

	// 1. Keep track of the context variable types
	// 2. Do type checking and inference
	VCChecker vcChecker = new VCChecker();

	//Auxiliar TypeCheckers
	OperationsChecker otc;
	MethodsFunctionsChecker mfc;

	public RefinementTypeChecker(Context context,Factory factory) {
		super(context, factory);
		otc = new OperationsChecker(this);
		mfc = new MethodsFunctionsChecker(this);
		System.out.println("In RefinementTypeChecker");
	}

	//--------------------- Visitors -----------------------------------
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
		super.visitCtClass(ctClass);
	}

	@Override
	public <T> void visitCtInterface(CtInterface<T> intrface) {
		System.out.println("CT INTERFACE: " +intrface.getSimpleName());
		super.visitCtInterface(intrface);
	}


	//	@Override
	//	public <A extends Annotation> void visitCtAnnotation(CtAnnotation<A> annotation) {
	//		super.visitCtAnnotation(annotation);
	//	}

	@Override
	public <A extends Annotation> void visitCtAnnotationType(CtAnnotationType<A> annotationType) {
		super.visitCtAnnotationType(annotationType);
	}

	@Override
	public <T> void visitCtConstructor(CtConstructor<T> c) {
		context.enterContext();
		mfc.getConstructorRefinements(c);
		getRefinementFromAnnotation(c); 
		super.visitCtConstructor(c);
		context.exitContext();
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

			checkVariableRefinements(refinementFound, varName, localVariable.getType(), localVariable);

			addStateRefinements(varName, e);
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
			String name = String.format(freshFormat, context.getCounter());
			if(c.getVariableNames().contains(WILD_VAR))
				c = c.substituteVariable(WILD_VAR, name);
			else
				c = new EqualsPredicate(new VariablePredicate(name), c);
			context.addVarToContext(name, factory.Type().INTEGER_PRIMITIVE, c);
			EqualsPredicate ep = new EqualsPredicate(FunctionPredicate.builtin_length(WILD_VAR), new VariablePredicate(name));
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
			checkAssignment(name, varDecl.getType(), ex, assignement.getAssignment(), varDecl);

			//			if(varDecl.getType() instanceof CtArrayTypeReferenceImpl)
			//				checkArray(varDecl);

		}else if(ex instanceof CtFieldWrite) {
			CtFieldReference<?> cr = ((CtFieldWrite<?>) ex).getVariable();
			CtField<?> f= ((CtFieldWrite<?>) ex).getVariable().getDeclaration();
			String name = String.format(thisFormat, cr.getSimpleName());
			checkAssignment(name, cr.getType(), ex, assignement.getAssignment(), f);

		}
		if(ex instanceof CtArrayWrite) {
			Constraint c = getRefinement(ex);
			//TODO continue
			//c.substituteVariable(WILD_VAR, );
		}
	}
	
	@Override
	public <T> void visitCtArrayRead(CtArrayRead<T> arrayRead) {
		super.visitCtArrayRead(arrayRead);
		String name = String.format(instanceFormat, "arrayAccess", context.getCounter());
		context.addVarToContext(name, arrayRead.getType(), new Predicate());
		arrayRead.putMetadata(REFINE_KEY, new VariablePredicate(name));
		//TODO predicate for now is always TRUE
	}

	private void checkAssignment(String name, CtTypeReference<?> type, CtExpression<?> ex, 
			CtExpression<?> assignment, CtElement elem) {
		getPutVariableMetadada(ex, name);

		Constraint refinementFound = getRefinement(assignment);
		if (refinementFound == null) {
			RefinedVariable rv =context.getVariableByName(name);
			if(rv instanceof Variable)
				refinementFound = rv.getMainRefinement();
			else
				refinementFound = new Predicate();
		}
		Optional<VariableInstance> r = context.getLastVariableInstance(name);
		if(r.isPresent())
			vcChecker.removePathVariableThatIncludes(r.get().getName());//AQUI!!

		vcChecker.removePathVariableThatIncludes(name);//AQUI!!
		checkVariableRefinements(refinementFound, name, type, elem);

	}

	@Override
	public <T> void visitCtLiteral(CtLiteral<T> lit) {
		List<String> types = Arrays.asList(implementedTypes);
		if (types.contains(lit.getType().getQualifiedName())) {
			lit.putMetadata(REFINE_KEY, new EqualsPredicate(new VariablePredicate(WILD_VAR), lit.getValue().toString()));
		}else if(lit.getType().getQualifiedName().contentEquals("java.lang.String")){
			//Only taking care of strings inside refinements
		}else {
			System.out.println(String.format("Literal of type %s not implemented:",
					lit.getType().getQualifiedName()));
		}
	}	


	@Override
	public <T> void visitCtField(CtField<T> f) {
		super.visitCtField(f);
		Optional<Constraint> c = getRefinementFromAnnotation(f);
		//		context.addVarToContext(f.getSimpleName(), f.getType(), 
		//				c.isPresent() ? c.get().substituteVariable(WILD_VAR, f.getSimpleName()) 
		//						      : new Predicate());
		String nname = String.format(thisFormat, f.getSimpleName());
		Constraint ret = new Predicate();
		if(c.isPresent())
			ret = c.get().substituteVariable(WILD_VAR, nname).substituteVariable(f.getSimpleName(), nname);
		RefinedVariable v = context.addVarToContext(nname, f.getType(),ret);
		if(v instanceof Variable)
			((Variable)v).setLocation("this");

	}



	@Override
	public <T> void visitCtFieldRead(CtFieldRead<T> fieldRead) {
		String fieldName =  fieldRead.getVariable().getSimpleName();
		if(context.hasVariable(fieldName)) {
			RefinedVariable rv = context.getVariableByName(fieldName);
			if(rv instanceof Variable && ((Variable)rv).getLocation().isPresent() &&
					((Variable)rv).getLocation().get().equals(fieldRead.getTarget().toString())) {
				fieldRead.putMetadata(REFINE_KEY, context.getVariableRefinements(fieldName));
			}else
				fieldRead.putMetadata(REFINE_KEY, 	new EqualsPredicate(new VariablePredicate(WILD_VAR), 
						new VariablePredicate(fieldName)));

		}else if(context.hasVariable(String.format(thisFormat, fieldName))){
			String thisName = String.format(thisFormat, fieldName);
			fieldRead.putMetadata(REFINE_KEY, new EqualsPredicate(new VariablePredicate(WILD_VAR), new VariablePredicate(thisName)));

		} else if(fieldRead.getVariable().getSimpleName().equals("length")) {
			String targetName = fieldRead.getTarget().toString();
			fieldRead.putMetadata(REFINE_KEY, 
					new EqualsPredicate(new VariablePredicate(WILD_VAR), FunctionPredicate.builtin_length(targetName)));
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
		getPutVariableMetadada(variableRead, varDecl.getSimpleName());
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
		String freshVarName = String.format(freshFormat ,context.getCounter());
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
		FunctionPredicate fp = FunctionPredicate.builtin_addToIndex(
				arrayWrite.getTarget().toString(), index.toString(), WILD_VAR);
		arrayWrite.putMetadata(REFINE_KEY, fp);
		//TODO fazer mais...? faz sentido
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



	@Override
	public <T> void visitCtConstructorCall(CtConstructorCall<T> ctConstructorCall) {
		super.visitCtConstructorCall(ctConstructorCall);
		mfc.getConstructorInvocationRefinements(ctConstructorCall);
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
	void checkVariableRefinements(Constraint refinementFound, String simpleName, CtTypeReference type, CtElement variable) {
		Optional<Constraint> expectedType = getRefinementFromAnnotation(variable);
		Constraint cEt = expectedType.isPresent()?expectedType.get():new Predicate();

		cEt = cEt.substituteVariable(WILD_VAR, simpleName);
		Constraint cet = cEt.substituteVariable(WILD_VAR, simpleName);

		String newName = String.format(instanceFormat, simpleName, context.getCounter());
		Constraint correctNewRefinement = refinementFound.substituteVariable(WILD_VAR, newName);
		cEt = cEt.substituteVariable(simpleName, newName);

		//Substitute variable in verification
		RefinedVariable rv= context.addInstanceToContext(newName, type, correctNewRefinement);
		context.addRefinementInstanceToVariable(simpleName, newName);
		//smt check
		checkSMT(cEt, variable);//TODO CHANGE
		context.addRefinementToVariableInContext(simpleName,type , cet);
	}





	//############################### Get Metadata ##########################################

	/**
	 * 
	 * @param <T>
	 * @param elem
	 * @param varDecl Cannot be null
	 */
	private <T> void getPutVariableMetadada(CtElement elem, String name) {
		Constraint cref = new EqualsPredicate(new VariablePredicate(WILD_VAR), new VariablePredicate(name));
		Optional<VariableInstance> ovi = context.getLastVariableInstance(name);
		if(ovi.isPresent())
			cref = new EqualsPredicate(new VariablePredicate(WILD_VAR), new VariablePredicate(ovi.get().getName()));

		elem.putMetadata(REFINE_KEY, cref);
	}


	protected void getGhostFunction(String value, CtElement element) {
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

	protected void handleAlias(String value, CtElement element) {
		try {
			Optional<Alias> oa = RefinementParser.parseAlias(value);
			if(oa.isPresent() && element instanceof CtClass) {
				String klass = ((CtClass) element).getSimpleName();
				String path = ((CtClass) element).getQualifiedName();

				AliasWrapper a = new AliasWrapper(oa.get(), factory, WILD_VAR, context, klass, path);
				context.addAlias(a);
			}
			//			System.out.println(oa);
		} catch (SyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void addStateRefinements(String varName, CtExpression<?> e) {
		Optional<VariableInstance> vi = context.getLastVariableInstance(varName);
		if(vi.isPresent() && e.getMetadata(STATE_KEY) != null) {
			Constraint c = (Constraint)e.getMetadata(STATE_KEY);
			c = c.substituteVariable("this", varName);
			vi.get().setState(c);
		}
	}


	@Override
	public <T> void visitCtNewClass(CtNewClass<T> newClass) {
		super.visitCtNewClass(newClass);
		System.out.println("new class");
	}

	@Override
	void checkSMT(Constraint expectedType, CtElement element) {
		vcChecker.processSubtyping(expectedType, element);
		element.putMetadata(REFINE_KEY, expectedType);	
	}

	@Override
	protected void checkStateSMT(Constraint prevState, Constraint expectedState, CtExpression<?> target) {
		vcChecker.smtChecking(prevState, expectedState, target);		
	}


}
