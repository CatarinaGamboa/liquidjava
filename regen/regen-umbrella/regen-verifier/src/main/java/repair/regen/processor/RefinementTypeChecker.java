package repair.regen.processor;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBinaryOperator;
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
import spoon.support.reflect.code.CtIfImpl;
import spoon.support.reflect.code.CtVariableWriteImpl;

public class RefinementTypeChecker extends CtScanner {
	// This class should do the following:

	// 1. Keep track of the context variable types
	// 2. Do type checking and inference
	private final String REFINE_KEY = "refinement";
	private final String WILD_VAR = "\\v";
	private final String FRESH = "FRESH_";

	private Context context = Context.getInstance();
	private VCChecker vcChecker = new VCChecker();
	private Utils utils = new Utils();

	private Factory factory;

	public RefinementTypeChecker(Factory factory) {
		this.factory = factory;		
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
		if(localVariable.getAssignment() == null) {
			Optional<String> a = getRefinementFromAnnotation(localVariable);
			context.addVarToContext(localVariable.getSimpleName(), localVariable.getType(), 
					a.isPresent()? a.get() : "true");
		}else {
			String varName = localVariable.getSimpleName();
			String refinementFound = getRefinement(localVariable.getAssignment());

			CtExpression a = localVariable.getAssignment();
			if (refinementFound == null)
				refinementFound = "true";
			context.addVarToContext(varName, localVariable.getType(), 
					"true");
			checkVariableRefinements(refinementFound,varName, localVariable);
			context.removeRefinementFromVariableInContext(varName, "true");
			context.addMainRefinementVariable(varName, context.getVariableRefinements(varName));
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
		String name = varDecl.getSimpleName();
		addRefinementVariable(name);
//		Optional<VariableInfo> ovi = context.getLastVariableInstance(name);
//		if(ovi.isPresent()) addRefinementVariable(ovi.get().getName());
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
		//		String freshVarName = "fresh_"+context.getCounter();
		//		context.addVarToContext(freshVarName, factory.Type().INTEGER_PRIMITIVE, 
		//				condRefs.replace(WILD_VAR, freshVarName));
		String condThen = "!(" + condRefs + ") || ("+ getRefinement(conditional.getThenExpression())+")", //!A or B
				notCondElse = "("+condRefs + ") || ("+ getRefinement(conditional.getElseExpression())+")";//A or C

		conditional.putMetadata(REFINE_KEY, "("+condThen+") && ("+notCondElse+")");
	}


	//------------------------------- Auxiliary Methods ----------------------------------------
	//############################### Variables and Context handling  ##########################################
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
			String retRef = String.format("(%s)", getRefinement(ret.getReturnedExpression())
													.replace(WILD_VAR, returnVarName));
			String expectedType = fi.getRefReturn().replace(WILD_VAR, returnVarName);

			context.addVarToContext(returnVarName, method.getType(), retRef);
			addRefinementVariable(returnVarName);
			checkSMT(retRef, expectedType, ret);
			context.removeRefinementFromVariableInContext(returnVarName, retRef);
			context.newRefinementToVariableInContext(returnVarName, expectedType);
		}
	}

	private <T> void getBinaryOpRefinements(CtBinaryOperator<T> operator) {
		CtExpression<?> right = operator.getRightHandOperand();
		CtExpression<?> left = operator.getLeftHandOperand();
		String oper = operator.toString();
		CtElement parent = operator.getParent();
		if(parent instanceof CtAssignment<?, ?>) {
			CtVariableWriteImpl<?> parentVar = (CtVariableWriteImpl<?>)((CtAssignment) parent)
					.getAssigned();
			oper = getOperationRefinements(operator, parentVar, operator);
		}else {
			String varRight = getOperationRefinements(operator, right);
			String varLeft = getOperationRefinements(operator, left);
			oper = String.format("(%s %s %s)", 
					varLeft, getOperatorFromKind(operator.getKind()),varRight);

		}
		if (operator.getType().getQualifiedName().contentEquals("int")) {
			operator.putMetadata(REFINE_KEY, WILD_VAR+" == " + oper);
		}else if(operator.getType().getQualifiedName().contentEquals("boolean")) {
			operator.putMetadata(REFINE_KEY, oper);
			if (parent instanceof CtLocalVariable<?> || parent instanceof CtUnaryOperator<?> ||
					parent instanceof CtReturn<?>)
				operator.putMetadata(REFINE_KEY, WILD_VAR+" == (" + oper+")");
		}
		//TODO ADD TYPES
	}

	private <T> void getUnaryOpRefinements(CtUnaryOperator<T> operator) {
		CtExpression ex = operator.getOperand();
		String name = FRESH, all;
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
		String newName = name+"_"+context.getCounter()+"_";
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
			CtExpression<?> element) {
		return getOperationRefinements(operator, null, element);
	}

	/**
	 * Retrieves all the refinements for the Operation including the refinements of all operands
	 * @param operator Binary Operator that started the operation
	 * @param parentVar Parent of Binary Operator, usually a CtAssignment or CtLocalVariable
	 * @param element CtExpression that represent an Binary Operation or one of the operands
	 * @return
	 */
	private String getOperationRefinements(CtBinaryOperator<?> operator, CtVariableWriteImpl<?> parentVar, 
			CtExpression<?> element) {
		if(element instanceof CtVariableRead<?>) {
			CtVariableRead<?> elemVar = (CtVariableRead<?>) element;
			String elemName = elemVar.getVariable().getSimpleName();
			String elem_ref = context.getVariableRefinements(elemName);
			
			String returnName = elemName;

			CtElement parent = operator.getParent();
			//No need for specific values
			if(parent != null && !(parent instanceof CtIfImpl)) {
				elem_ref = getRefinement(elemVar);
				String newName = elemName+"_"+context.getCounter()+"_";
				String newElem_ref = elem_ref.replace(WILD_VAR, newName);
				context.addVarToContext(newName, elemVar.getType(), newElem_ref);
				addRefinementVariable(newName);
				returnName = newName;
			}
			
			context.addVarToContext(elemName, elemVar.getType(), elem_ref);
			addRefinementVariable(elemName);
			return returnName;
		}

		else if(element instanceof CtBinaryOperator<?>) {
			CtBinaryOperator<?> binop = (CtBinaryOperator<?>) element;
			String right = getOperationRefinements(operator, parentVar, binop.getRightHandOperand());
			String left = getOperationRefinements(operator, parentVar, binop.getLeftHandOperand());
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
			String newName = FRESH+context.getCounter();
			innerRefs = innerRefs.replace("\\v", newName);
			context.addVarToContext(newName, fi.getType(), innerRefs);
			addRefinementVariable(newName);
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
		String newName = name+"__"+context.getCounter();
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
			context.addRefinementToVariableInContext(variable, et);
		});
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


	//############################### SMT CHECKING  ##########################################
	private <T> void checkSMT(String correctRefinement, String expectedType, CtElement element) {
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
	private String getRefinement(CtElement elem) {
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
		String refinementFound = getRefinement(varDecl);
		String name = varDecl.getSimpleName();

		String ref = "("+WILD_VAR+" == " + varDecl.getSimpleName()+ ")";
		Optional<VariableInfo> ovi = context.getLastVariableInstance(name);
		if(ovi.isPresent()) {
			VariableInfo vi = ovi.get();
			addRefinementVariable(vi.getName());
			ref = ref + "&& ("+WILD_VAR+" == "+vi.getName()+")";
		}
		
		variable.putMetadata(REFINE_KEY, ref);//&& ("+ ref + ")");
		
		

	}
//	private <T> void getVariableMetadada(CtElement elem,CtVariableReference variable) {
//		String name = variable.getSimpleName();
//		String ref = getVariableMetadata(null, name, variable.getType());
//		elem.putMetadata(REFINE_KEY, "("+WILD_VAR+" == " + 
//				name + ") ");//&& ("+ ref + ")");
//	}

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
