package repair.regen.processor;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.CorrectionEngine;

import repair.regen.smt.SMTEvaluator;
import repair.regen.smt.TypeCheckError;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtConditional;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtUnaryOperator;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.code.CtVariableWrite;
import spoon.reflect.code.UnaryOperatorKind;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtAnnotationType;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.declaration.ParentNotInitializedException;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.reference.CtVariableReference;
import spoon.reflect.visitor.CtScanner;
import spoon.support.reflect.code.CtVariableWriteImpl;

public class RefinementTypeChecker extends CtScanner {
	// This class should do the following:

	// 1. Keep track of the context variable types
	// 2. Do type checking and inference
	private final String REFINE_KEY = "refinement";
	private final String WILD_VAR = "\\v";

	private Context context = Context.getInstance();
	private VCChecker vcChecker = new VCChecker();
	//private List<String> variables = new ArrayList<>();
	private Factory factory;

	public RefinementTypeChecker(Factory factory) {
		this.factory = factory;		
	}

	private void renewVariables() {
		vcChecker.renewVariables();
	}
	private void addRefinementVariable(String varName) {
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

	//--------------------- Visitors -----------------------------------
	@Override
	public <T> void visitCtClass(CtClass<T> ctClass) {
		System.out.println("CTCLASS:"+ctClass.getSimpleName());
		context.reinitializeContext();
		super.visitCtClass(ctClass);
	}
	@Override
	public <A extends Annotation> void visitCtAnnotation(CtAnnotation<A> annotation) {
		//VISITS THE ANNOTATION
		//		System.out.println("ANNOTATION:"+annotation.getType());
		//		System.out.println("ANNOTATION:"+annotation.getMetadata(REFINE_KEY));
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
		getMethodRefinements(method);
		super.visitCtMethod(method);
		exitContexts();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> void visitCtLocalVariable(CtLocalVariable<T> localVariable) {
		super.visitCtLocalVariable(localVariable);
		//only declaration, no assignment
		if(localVariable.getAssignment() == null) return;
		String refinementFound = getRefinement(localVariable.getAssignment());
		System.out.println("REFINEMENT LOCAL VAR "+localVariable.getSimpleName()+":"+refinementFound);
		System.out.println(context.getAllVariables());

		CtExpression a = localVariable.getAssignment();
		if (refinementFound == null)
			refinementFound = "true";
		checkVariableRefinements(refinementFound, localVariable.getSimpleName(), localVariable);

	}

	@Override
	public <T,A extends T> void visitCtAssignment(CtAssignment<T,A> assignement) {
		super.visitCtAssignment(assignement);
		CtExpression<T> ex =  assignement.getAssigned();

		if (ex instanceof CtVariableWriteImpl) {
			CtVariableReference<?> var = ((CtVariableAccess<?>) ex).getVariable();
			CtVariable<T> varDecl = (CtVariable<T>) var.getDeclaration();
			String name = var.getSimpleName();

			if(varDecl != null)	getPutVariableMetadada(ex, varDecl);
			else getVariableMetadada(ex, var);

			String refinementFound = getRefinement(assignement.getAssignment());
			if (refinementFound == null) {
				refinementFound = "true";
			}
			checkVariableRefinements(refinementFound, name, varDecl);
		}
	}

	@Override
	public <T> void visitCtLiteral(CtLiteral<T> lit) {
		if (lit.getType().getQualifiedName().contentEquals("int")) {
			lit.putMetadata(REFINE_KEY, WILD_VAR+" == " + lit.getValue());
		}else if (lit.getType().getQualifiedName().contentEquals("boolean")) {
			lit.putMetadata(REFINE_KEY, WILD_VAR+" == " + lit.getValue());
		}
		//TODO ADD LITERAL TYPES
	}	


	@Override
	public <T> void visitCtVariableRead(CtVariableRead<T> variableRead) {
		super.visitCtVariableRead(variableRead);
		CtVariable<T> varDecl = variableRead.getVariable().getDeclaration();
		addRefinementVariable(varDecl.getSimpleName());
		getPutVariableMetadada(variableRead, varDecl);
	}

	/**
	 * Visitor for binary operations
	 * Adds metadata to the binary operations from the operands
	 */
	@Override
	public <T> void visitCtBinaryOperator(CtBinaryOperator<T> operator) {
		super.visitCtBinaryOperator(operator);
		getBinaryOpRefinements(operator);

	}

	@Override
	public <T> void visitCtUnaryOperator(CtUnaryOperator<T> operator) {
		super.visitCtUnaryOperator(operator);
		getUnaryOpRefinements(operator);

	}

	public <R> void visitCtInvocation(CtInvocation<R> invocation) {
		super.visitCtInvocation(invocation);
		getInvocationRefinements(invocation);
	}

	@Override
	public <R> void visitCtReturn(CtReturn<R> ret) {
		super.visitCtReturn(ret);
		getReturnRefinements(ret);

	}


	@Override
	public void visitCtIf(CtIf ifElement) {
		CtExpression<Boolean> exp = ifElement.getCondition();
		String expRefs = getExpressionRefinements(exp);


		String freshVarName = "fresh_"+context.getCounter();
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
		vcChecker.renewVariables();

		//		List<VariableInfo> l = searchForVars(expRefs, "");
		//		//THEN
		//		insideIfBlocks(ifElement.getThenStatement(), expRefs, l);
		//		//ELSE
		//		if(ifElement.getElseStatement() != null) {
		//			CtBlock elseStatements = ifElement.getElseStatement();
		//			CtUnaryOperator<Boolean> negExp = factory.createUnaryOperator();
		//			negExp.setKind(UnaryOperatorKind.NOT);
		//			negExp.setOperand(exp);
		//			CtIf elseIf = factory.createIf();
		//			elseIf.setCondition(negExp);
		//			elseIf.setThenStatement(elseStatements);
		//			
		//			ifElement.insertAfter(elseIf);			
		//			
		//			visitCtIf(elseIf);
		////			ifElement.updateAllParentsBelow();
		////			String negExpRefs = getExpressionRefinements(negExp);
		////			insideIfBlocks(ifElement.getElseStatement(), negExpRefs, l);
		////			
		////			ifElement.updateAllParentsBelow();
		//			
		////			insideIfBlocks(ifElement.getElseStatement(), negRefs, l);
		//		}
		//		System.out.println("--------------End if-------------"/*+getExpressionRefinements(exp)*/);
		//		System.out.println(context.getAllVariables());
	}



	@Override
	public <T> void visitCtConditional(CtConditional<T> conditional) {
		super.visitCtConditional(conditional);

		String condRefs = getRefinement(conditional.getCondition());
		//		String freshVarName = "fresh_"+context.getCounter();
		//		context.addVarToContext(freshVarName, factory.Type().INTEGER_PRIMITIVE, 
		//				condRefs.replace(WILD_VAR, freshVarName));
		String condThen = "!(" + condRefs + ") || ("+ getRefinement(conditional.getThenExpression())+")", //!A or B
				notCondElse = "("+condRefs + ") || ("+ getRefinement(conditional.getElseExpression())+")";//A or C

		conditional.putMetadata(REFINE_KEY, "("+condThen+") && ("+notCondElse+")");
	}


	//------------------------------- Auxiliary Methods ----------------------------------------
	//############################### Inner Visitors  ##########################################

	private <R> void getReturnRefinements(CtReturn<R> ret) {
		if(ret.getReturnedExpression() != null) {
			//check if there are refinements
			if(getRefinement(ret.getReturnedExpression())== null)
				ret.getReturnedExpression().putMetadata(REFINE_KEY, "true");
			CtMethod method = ret.getParent(CtMethod.class);
			//check if method has refinements
			if(getRefinement(method) == null)
				return;
			FunctionInfo fi = context.getFunctionByName(method.getSimpleName());
			for(VariableInfo vi:fi.getArgRefinements())
				addRefinementVariable(vi.getName());

			//Both return and the method have metadata
			String returnVarName = "RET_"+context.getCounter(); 
			String retRef = "("+getRefinement(ret.getReturnedExpression())
			.replace(WILD_VAR, returnVarName)+")";
			String expectedType = fi.getRefReturn().replace(WILD_VAR, returnVarName);

			context.addVarToContext(returnVarName, method.getType(), retRef);
			addRefinementVariable(returnVarName);
			checkSMT(retRef, expectedType, ret);
			context.removeRefinementFromVariableInContext(returnVarName, retRef);
			context.newRefinementToVariableInContext(returnVarName, expectedType);
		}
	}

	private <T> void getBinaryOpRefinements(CtBinaryOperator<T> operator) {
		StringBuilder sb = new StringBuilder(); 
		CtExpression<?> right = operator.getRightHandOperand();
		CtExpression<?> left = operator.getLeftHandOperand();
		String oper = operator.toString();
		CtElement parent = operator.getParent();
		if(parent instanceof CtAssignment<?, ?>) {
			CtVariableWriteImpl<?> parentVar = (CtVariableWriteImpl<?>)((CtAssignment) parent)
					.getAssigned();
			oper = getOperationRefinements(operator, parentVar, operator, sb);
		}else {

			String varRight = getOperationRefinements(operator, right, sb);
			String varLeft = getOperationRefinements(operator, left, sb);
			oper =  varLeft +" "+ getOperatorFromKind(operator.getKind()) +" "+ varRight;

		}
		if (operator.getType().getQualifiedName().contentEquals("int")) {
			operator.putMetadata(REFINE_KEY, WILD_VAR+" == " + oper+ sb.toString());
		}else if(operator.getType().getQualifiedName().contentEquals("boolean")) {
			operator.putMetadata(REFINE_KEY, oper+ sb.toString());
			if (parent instanceof CtLocalVariable<?> || parent instanceof CtUnaryOperator<?> ||
					parent instanceof CtReturn<?>)
				operator.putMetadata(REFINE_KEY, WILD_VAR+" == (" + oper+")"+ sb.toString());
		}
		//TODO ADD TYPES
	}

	private <T> void getUnaryOpRefinements(CtUnaryOperator<T> operator) {
		CtExpression ex = operator.getOperand();
		String name, all;
		if(ex instanceof CtVariableWrite) {
			CtVariableWrite w = (CtVariableWrite) ex;
			name = w.getVariable().getSimpleName();
			all = getRefinementUnaryVariableWrite(ex, operator, w, name);
			checkVariableRefinements(all, name, w.getVariable().getDeclaration());
			return;

		}else if (ex instanceof CtVariableRead){
			CtVariableRead var = (CtVariableRead) ex;
			name = var.getVariable().getSimpleName();
			//If the variable is the same, the refinements need to be changed
			try {
				CtAssignment assign = operator.getParent(CtAssignment.class);
				if(assign!= null && assign.getAssigned() instanceof CtVariableWrite<?>) {
					CtVariableWrite<?> w = (CtVariableWrite<?>) assign.getAssigned();
					String parentName = w.getVariable().getSimpleName();
					if(name.equals(parentName)) {
						all = getRefinementUnaryVariableWrite(ex, operator, w, name);
						operator.putMetadata(REFINE_KEY, all);
						return;
					}
				}
			}catch(ParentNotInitializedException e) {
				System.out.println("Parent not initialized");
			}
		}
		String metadata = getRefinement(ex);
		String newName = "VV_"+context.getCounter();
		String newMeta = "("+metadata.replace(WILD_VAR, newName)+")";
		String unOp = getOperatorFromKind(operator.getKind());

		CtElement p = operator.getParent();
		String opS = unOp.replace(WILD_VAR, newName);
		if(p instanceof CtIf)
			all = unOp.replace(WILD_VAR, newName);
		else
			all ="("+WILD_VAR+" == " + opS + ")";
		System.out.println(newMeta + " && "+all);
		context.addVarToContext(newName, ex.getType(), newMeta);
		addRefinementVariable(newName);
		operator.putMetadata(REFINE_KEY, all);

	}

	private <R> void getMethodRefinements(CtMethod<R> method) {
		FunctionInfo f = new FunctionInfo();
		f.setName(method.getSimpleName());
		f.setType(method.getType());
		f.setRefReturn("true");
		context.addFunctionToContext(f);
		for(CtAnnotation<? extends Annotation> ann :method.getAnnotations()) {
			if( !ann.getActualAnnotation().annotationType().getCanonicalName()
					.contentEquals("repair.regen.specification.Refinement"))
				continue;
			CtLiteral<String> s = (CtLiteral<String>) ann.getAllValues().get("value");
			String methodRef = s.getValue();
			List<CtParameter<?>> params = method.getParameters();
			String[] r = methodRef.split("->");
			StringBuilder sb = new StringBuilder();

			//For syntax {param1} -> {param2} -> ... -> {return}
			for (int i = 0; i < params.size(); i++) {
				CtParameter<?> param = params.get(i);
				String name = param.getSimpleName();
				String metRef = r[i].replace("{", "(").replace("}", ")").replace(WILD_VAR, name);
				param.putMetadata(REFINE_KEY, metRef);
				sb.append(sb.length() == 0? metRef : " && "+metRef);

				f.addArgRefinements(name,param.getType(), metRef);
				context.addVarToContext(name, param.getType(), metRef);
				addRefinementVariable(name);
			}
			String retRef = r[r.length-1].replace("{", "(").replace("}", ")");
			f.setRefReturn(retRef);

			method.putMetadata(REFINE_KEY, sb.append(" && "+ retRef).toString());

		}

	}

	private <R> void getInvocationRefinements(CtInvocation<R> invocation) {
		CtExecutable<?> method = invocation.getExecutable().getDeclaration();
		if(method != null) {
			FunctionInfo f = context.getFunctionByName(method.getSimpleName());
			String methodRef = f.getRenamedReturn();
			String metRef = f.getRenamedReturn();
			List<String> saveVars = new ArrayList<>();
			for(String v: vcChecker.getVariables())
				saveVars.add(v);

			if(methodRef != null) {
				//Checking Parameters
				List<CtExpression<?>> exps = invocation.getArguments();
				List<VariableInfo> params = f.getArgRefinements();
				for (int i = 0; i < params.size(); i++) {
					VariableInfo pinfo = params.get(i);
					CtExpression<?> exp = exps.get(i);
					String newParamName = pinfo.getIncognitoName();
					String refPar = f.getRefinementsForParamIndex(i);
					String refInv = (getRefinement(exp)).replace(WILD_VAR, newParamName);
					System.out.println("ref par:"+refPar);

					context.addVarToContext(newParamName, pinfo.getType(), refInv);
					context.newRefinementToVariableInContext(newParamName, refInv);
					addRefinementVariable(newParamName);
					for(String s:saveVars)
						addRefinementVariable(s);

					if(exp instanceof CtVariableRead<?>) {
						String name = ((CtVariableRead) exp).getVariable().getSimpleName();
						context.addVarToContext(name,
								((CtVariableRead) exp).getType(), refPar);
						addRefinementVariable(name);
						metRef = metRef.replaceAll(newParamName, name);
					}
					checkSMT(refInv, refPar, (CtVariable<?>)method.getParameters().get(i));
					saveVars.add(newParamName);
				}


				for(VariableInfo vi:params) 
					addRefinementVariable(vi.getIncognitoName());
				for(String s: saveVars)
					addRefinementVariable(s);
				//Checking Return
				String s = methodRef;// sb.length() == 0? methodRef:  sb.append(" && "+methodRef).toString();
				//String s = methodRef;
				invocation.putMetadata(REFINE_KEY, metRef);
			}
		}
	}



	private String getOperationRefinements(CtBinaryOperator<?> operator, 
			CtExpression<?> element, StringBuilder sb) {
		return getOperationRefinements(operator, null, element, sb);
	}

	/**
	 * Retrieves all the refinements for the Operation including the refinements of all operands
	 * @param operator Binary Operator that started the operation
	 * @param parentVar Parent of Binary Operator, usually a CtAssignment or CtLocalVariable
	 * @param element CtExpression that represent an Binary Operation or one of the operands
	 * @param sb StringBuilder that joins the refinements of all the elements in the operation
	 * @return
	 */
	private String getOperationRefinements(CtBinaryOperator<?> operator, CtVariableWriteImpl<?> parentVar, 
			CtExpression<?> element, StringBuilder sb) {
		if(element instanceof CtVariableRead<?>) {
			CtVariableRead<?> elemVar = (CtVariableRead<?>) element;
			String elemName = elemVar.getVariable().getSimpleName();
			String elem_ref = context.getVariableRefinements(elemName);
			//getRefinement(element);

			//same name as caller k = k +...
			CtElement parent = operator.getParent();
			if(parent instanceof CtAssignment) {
				CtExpression<?> parent_var = ((CtAssignment) parent).getAssigned();
				if(parent_var instanceof CtVariableWriteImpl) {
					String parentName = parentVar.getVariable().getSimpleName();
					if(parentName.equals(elemName)) {
						elemName = "VV_"+context.getCounter();//+parentName;
						elem_ref = elem_ref.replaceAll(parentName, elemName);
						context.addVarToContext(elemName, parentVar.getType(), elem_ref);
						addRefinementVariable(elemName);
					}
				}
			}
			context.addVarToContext(elemName, elemVar.getType(), elem_ref);
			addRefinementVariable(elemName);
			//sb.append(" && "+elem_ref.replace(WILD_VAR, elemName));
			return elemName;
		}

		else if(element instanceof CtBinaryOperator<?>) {
			CtBinaryOperator<?> binop = (CtBinaryOperator<?>) element;
			String right = getOperationRefinements(operator, parentVar, binop.getRightHandOperand(), sb);
			String left = getOperationRefinements(operator, parentVar, binop.getLeftHandOperand(), sb);
			return left +" "+ getOperatorFromKind(binop.getKind()) +" "+ right;

		}else if (element instanceof CtUnaryOperator<?>) {
			String a = (String) element.getMetadata(REFINE_KEY);
			String b = a.replace(WILD_VAR, "").replace("(", "").replace(")", "")
					.replace("==", "").replace(" ", "");
			return b;
		}else if (element instanceof CtLiteral<?>) {
			CtLiteral<?> l = (CtLiteral<?>) element;
			return l.getValue().toString();

		}else if(element instanceof CtInvocation<?>) {
			CtInvocation<?> inv = (CtInvocation<?>) element;
			CtExecutable<?> method = inv.getExecutable().getDeclaration();
			//Get function refinements with non_used variables
			FunctionInfo fi = context.getFunctionByName(method.getSimpleName());
			String innerRefs = fi.getRenamedRefinements();
			//Substitute \\v by the variable that we send
			String newName = "VV_"+context.getCounter();
			innerRefs = innerRefs.replace("\\v", newName);
			context.addVarToContext(newName, fi.getType(), innerRefs);
			addRefinementVariable(newName);
			//sb.append(" && "+innerRefs);//Add refinements
			return newName;//Return variable that represents the invocation
		}
		return getRefinement(element);
		//TODO Maybe add cases
	}

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

	private <T> String getRefinementUnaryVariableWrite(CtExpression ex, CtUnaryOperator<T> operator, CtVariableWrite w,
			String name) {
		String newName = "VV_"+context.getCounter();
		CtVariable varDecl = w.getVariable().getDeclaration();
		//		if(varDecl != null)	getPutVariableMetadada(ex, w.getVariable().getDeclaration());
		//		else getVariableMetadada(ex, w.getVariable());

		String metadada = context.getVariableRefinements(varDecl.getSimpleName());
		addRefinementVariable(newName);
		String binOperation = getOperatorFromKind(operator.getKind()).replace(WILD_VAR, newName);
		String metaOper = metadada.replace(WILD_VAR, newName).replace(name, newName);
		context.addVarToContext(newName, w.getType(), metaOper);
		return WILD_VAR+" == "+binOperation;
	}



	//############################### SMT Evaluation ##########################################
	private <T> void checkVariableRefinements(String refinementFound, String simpleName, CtVariable<T> variable) {
		String correctRefinement = refinementFound.replace(WILD_VAR, simpleName);
		//variable.putMetadata(REFINE_KEY, correctRefinement);
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
			String a = dependentRefinements(et, simpleName);
			//String completeRefinement = a.length() == 0? correctRefinement:correctRefinement+" && "+ a; 
			String completeRefinement = correctRefinement + a; 
			System.out.println("correctRefinement:"+completeRefinement);
			//FOR VCS
			addRefinementVariable(simpleName);
			//context.addRefinementToVariableInContext(variable, correctRefinement);
			context.newRefinementToVariableInContext(variable, correctRefinement);
			checkSMT(completeRefinement, et, variable);
			context.removeRefinementFromVariableInContext(variable, correctRefinement);
			context.addRefinementToVariableInContext(variable, et);
		});
	}



	//############################### SMT CHECKING  ##########################################
	private <T> void checkSMT(String correctRefinement, String expectedType, CtElement element) {
		vcChecker.processSubtyping(expectedType, element);
		//		System.out.println("SMT subtyping:" + correctRefinement + " <: " + expectedType);
		//		System.out.println("-----------------------------------------------");
		//
		//		if (element instanceof CtVariable<?>) {
		//			addVariableToContext((CtVariable<?>)element, expectedType);	
		//		}
		//		//System.out.println("Context:\n"+context);
		//		try {
		//			new SMTEvaluator().verifySubtype(correctRefinement, expectedType, context.getContext());
		//		} catch (TypeCheckError e) {
		//			printError(element, expectedType, correctRefinement);
		//
		//		}
		element.putMetadata(REFINE_KEY, expectedType);	
		renewVariables();
	}


	//############################### Get Metadata ##########################################
	private String getRefinement(CtElement elem) {
		return (String) elem.getMetadata(REFINE_KEY);
	}

	private String dependentRefinements(String et, String name) {
		System.out.println(et);
		StringBuilder sb = new StringBuilder();
		List<VariableInfo> l = searchForVars(et, name);
		for(VariableInfo vi: l) {
			sb.append(" && " + vi.getRefinement());
			System.out.println(vi.getRefinement());
		}
		String pathRefs = context.getPathRefinements();
		if(pathRefs.length() > 0)
			sb.append(" && "+pathRefs);

		if(pathRefs.length() != 0) {
			System.out.println("pathRefs:"+pathRefs+", len:"+pathRefs.length());
			sb.append(sb.length()==0?pathRefs:" && "+pathRefs);
		}
		System.out.println(context.getAllVariables());
		//System.out.println("SB:"+sb.toString());
		return sb.toString();
	}


	private List<VariableInfo> searchForVars(String met, String name) {
		List<VariableInfo> l = new ArrayList<>();
		String[] a = met.split("&&|<|>|(<=)|(>=)|(==)|=|-|\\+|/|\\*|%|(\\|\\||\\(|\\))");//TODO missing OR and maybe other
		for(String s: a) {
			String t = s.replace(" ", "");
			if(!t.equals(name)) {
				VariableInfo v = context.getVariableByName(t);
				//System.out.println(t+": variable :"+v);
				if(v != null && !l.contains(v)) {
					l.add(v);
					addRefinementVariable(t);
				}
			}
		}
		return l;
	}


	/**
	 * 
	 * @param <T>
	 * @param variable
	 * @param varDecl Cannot be null
	 */
	private <T> void getPutVariableMetadada(CtElement variable, CtVariable<T> varDecl) {
		String refinementFound = getRefinement(varDecl);
		String name = varDecl.getSimpleName();

		String ref = getVariableMetadata(refinementFound, name, varDecl.getType());
		variable.putMetadata(REFINE_KEY, "("+WILD_VAR+" == " + 
				varDecl.getSimpleName()+ ")");//&& ("+ ref + ")");

	}
	private <T> void getVariableMetadada(CtElement elem,CtVariableReference variable) {
		String name = variable.getSimpleName();
		String ref = getVariableMetadata(null, name, variable.getType());
		elem.putMetadata(REFINE_KEY, "("+WILD_VAR+" == " + 
				name + ") ");//&& ("+ ref + ")");
	}

	private String getVariableMetadata(String ref, String name, CtTypeReference type) {
		String refinementFound = ref;
		if(context.hasVariable(name))
			refinementFound = context.getVariableRefinements(name);

		if (refinementFound == null) {
			refinementFound = "true";
			context.addVarToContext(name, type, refinementFound);
			addRefinementVariable(name);
		}
		return refinementFound;
	}


	//############################### Operations Auxiliaries ##########################################

	/**
	 * Get the String value of the operator from the enum
	 * @param kind
	 * @return
	 */
	private String getOperatorFromKind(BinaryOperatorKind kind) {
		switch(kind) {
		case PLUS:	return "+";
		case MINUS: return "-";
		case MUL: 	return "*";
		case DIV: 	return "/";
		case MOD: 	return "%";

		case AND: 	return "&&";
		case OR: 	return "||";

		case EQ: 	return "==";
		case NE: 	return "!=";
		case GE: 	return ">=";
		case GT: 	return ">";
		case LE: 	return "<=";
		case LT: 	return "<";
		default:
			return null;
			//TODO COMPLETE
		}
	}

	private String getOperatorFromKind(UnaryOperatorKind kind) {
		switch(kind) {
		case POSTINC:	return WILD_VAR+" + 1";
		case POSTDEC: 	return WILD_VAR+" - 1";
		case PREINC:	return WILD_VAR+" + 1";
		case PREDEC: 	return WILD_VAR+" - 1";
		//TODO FILL WITH CORRECT
		case NOT: 	return "!"+WILD_VAR;
		case POS: 	return "0 + "+ WILD_VAR;
		case NEG: 	return "-"+WILD_VAR;
		default:	return null;
		}
	}

}
