package repair.regen.processor;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtVariableReference;
import spoon.reflect.visitor.CtScanner;
import spoon.support.reflect.code.CtVariableWriteImpl;

public class RefinementTypeChecker extends CtScanner {
	// This class should do the following:

	// 1. Keep track of the context variable types
	// 2. Do type checking and inference
	final String REFINE_KEY = "refinement";
	final String WILD_VAR = "\\v";
	final String FRESH = "FRESH_";

	Context context = Context.getInstance();
	VCChecker vcChecker = new VCChecker();
	Utils utils = new Utils();
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
		//super.visitCtMethod(method); //-- first we need the signature refinements
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
			Optional<String> a = getRefinementFromAnnotation(localVariable);
			context.addVarToContext(localVariable.getSimpleName(), localVariable.getType(), 
					a.isPresent()? a.get() : "true");
		}else {
			String varName = localVariable.getSimpleName();
			CtExpression<?> e = localVariable.getAssignment();
			String refinementFound = getRefinement(localVariable.getAssignment());

			CtExpression a = localVariable.getAssignment();
			if (refinementFound == null)
				refinementFound = "true";
			context.addVarToContext(varName, localVariable.getType(), "true");
			checkVariableRefinements(refinementFound,varName, localVariable);
			context.removeRefinementFromVariableInContext(varName, "true");
			context.addMainRefinementVariable(varName, "("+context.getVariableRefinements(varName)+")");
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

			String refinementFound = getRefinement(assignement.getAssignment());
			if (refinementFound == null) {
				refinementFound = "true";
			}
			vcChecker.removeFreshVariableThatIncludes(name);
			checkVariableRefinements(refinementFound, name, varDecl);

		}
	}

	@Override
	public <T> void visitCtLiteral(CtLiteral<T> lit) {
		List<String> types = Arrays.asList(implementedTypes);
		if (types.contains(lit.getType().getQualifiedName())) {
			lit.putMetadata(REFINE_KEY,"("+ WILD_VAR+" == " + lit.getValue()+")");
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
				fieldRead.putMetadata(REFINE_KEY, lib.getRefinement(var).get());
			}
		}
		super.visitCtFieldRead(fieldRead);
	}


	@Override
	public <T> void visitCtVariableRead(CtVariableRead<T> variableRead) {
		super.visitCtVariableRead(variableRead);
		CtVariable<T> varDecl = variableRead.getVariable().getDeclaration();
		String name = varDecl.getSimpleName();
		addRefinementVariable(name);
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

		enterContexts();
		
		String expRefs = getExpressionRefinements(exp);
		String freshVarName = FRESH+context.getCounter();
		vcChecker.setPathVariables(freshVarName);		
		
		context.addVarToContext(freshVarName, factory.Type().INTEGER_PRIMITIVE, 
				expRefs.replace(WILD_VAR, freshVarName));
		vcChecker.addRefinementVariable(freshVarName);
		enterContexts();
		visitCtBlock(ifElement.getThenStatement());
		exitContexts();

		if(ifElement.getElseStatement() != null) {
			context.getVariableByName(freshVarName);
			context.newRefinementToVariableInContext(freshVarName, "!("+expRefs+")");
			enterContexts();
			visitCtBlock(ifElement.getElseStatement());
			exitContexts();
		}
		//end
		vcChecker.removePathVariable(freshVarName);
		vcChecker.renewVariables();
		exitContexts();
	}



	@Override
	public <T> void visitCtConditional(CtConditional<T> conditional) {
		super.visitCtConditional(conditional);

		String condRefs = getRefinement(conditional.getCondition());
		String condThen = "!(" + condRefs + ") || ("+ getRefinement(conditional.getThenExpression())+")", //!A or B
				notCondElse = "("+condRefs + ") || ("+ getRefinement(conditional.getElseExpression())+")";//A or C

		conditional.putMetadata(REFINE_KEY, "("+condThen+") && ("+notCondElse+")");
	}


	//------------------------------- Auxiliary Methods ----------------------------------------
	//############################### Variables and Context handling  ##########################################
	private void renewVariables() {
		vcChecker.renewVariables();
	}
	void addRefinementVariable(String varName) {
		vcChecker.addRefinementVariable(varName);
	}
	private void enterContexts() {
		context.enterContext();
		vcChecker.enterContext();
	}
	private void exitContexts() {
		context.exitContext();
		vcChecker.exitContext();
	}

	//############################### Inner Visitors  ##########################################

	private String getExpressionRefinements(CtExpression element) {
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
			return l.getValue().toString();
		}else if(element instanceof CtInvocation<?>) {
			CtInvocation<?> inv = (CtInvocation<?>) element;
			visitCtInvocation(inv);
			return getRefinement(inv);
		}
		return getRefinement(element);
	}


	//############################### SMT Evaluation ##########################################
	<T> void checkVariableRefinements(String refinementFound, String simpleName, CtVariable<T> variable) {
		Optional<String> expectedType = variable.getAnnotations().stream()
				.filter(
						ann -> ann.getActualAnnotation().annotationType().getCanonicalName()
						.contentEquals("repair.regen.specification.Refinement")
						).map(
								ann -> (CtLiteral<String>) ann.getAllValues().get("value")
								).map(
										str -> str.getValue().replace(WILD_VAR, simpleName)
										).findAny();
		expectedType.ifPresent((et) -> {
			//create new variable for validation
			//Ex: @Ref(a>5) a = 10; VC becomes: a__0 == 10 -> a__0 > 5
			String newName = simpleName+"_"+context.getCounter()+"_";
			String correctRefinement = refinementFound.replace(WILD_VAR, simpleName);
			String correctNewRefinement = refinementFound.replace(WILD_VAR, newName);
			String etNew = et.replaceAll(simpleName, newName);//TODO: Change replaceAll for better
			addReferencedVars(etNew, newName);

			//Substitute variable in verification
			context.addVarToContext(newName, variable.getType(), correctNewRefinement);
			context.addRefinementInstanceToVariable(simpleName, newName);
			addRefinementVariable(newName);
			//smt check
			checkSMTVariable(correctNewRefinement, etNew, variable, simpleName);
			context.addRefinementToVariableInContext(variable, "("+et+")");
		});
	}
	
	<T> void checkSMT(String correctRefinement, String expectedType, CtElement element) {
		vcChecker.processSubtyping(expectedType, element);
		element.putMetadata(REFINE_KEY, expectedType);	
		renewVariables();
	}
	
	private void checkSMTVariable(String completeRefinement, String expectedType, 
			CtVariable<?> element, String simpleName) {
		vcChecker.processSubtyping(expectedType, simpleName, element);
		element.putMetadata(REFINE_KEY, expectedType);	
		renewVariables();

	}


	//############################### Get Metadata ##########################################
	String getRefinement(CtElement elem) {
		return (String) elem.getMetadata(REFINE_KEY);
	}

	private List<VariableInfo> addReferencedVars(String string, String differentFrom) {
		List<VariableInfo> vis = utils.searchForVars(string, differentFrom);
		for(VariableInfo vi: vis)
			addRefinementVariable(vi.getName());
		return vis;
	}



	/**
	 * 
	 * @param <T>
	 * @param variable
	 * @param varDecl Cannot be null
	 */
	private <T> void getPutVariableMetadada(CtElement variable, CtVariable<T> varDecl) {
//		String refinementFound = getRefinement(varDecl);
		String name = varDecl.getSimpleName();

		String ref = "("+WILD_VAR+" == " + varDecl.getSimpleName()+ ")";
		Optional<VariableInfo> ovi = context.getLastVariableInstance(name);
		if(ovi.isPresent()) {
			VariableInfo vi = ovi.get();
			addRefinementVariable(vi.getName());
			ref = ref + "&& ("+WILD_VAR+" == "+vi.getName()+")";
		}
		
		variable.putMetadata(REFINE_KEY, ref);
	}
	

	public Optional<String> getRefinementFromAnnotation(CtElement element) {
		Optional<String> ref = Optional.empty();
		for(CtAnnotation<? extends Annotation> ann :element.getAnnotations()) 
			if( ann.getActualAnnotation().annotationType().getCanonicalName()
					.contentEquals("repair.regen.specification.Refinement")) {
				CtLiteral<String> s = (CtLiteral<String>) ann.getAllValues().get("value");
				ref = Optional.of(s.getValue());
			}
		return ref;
	}

}
