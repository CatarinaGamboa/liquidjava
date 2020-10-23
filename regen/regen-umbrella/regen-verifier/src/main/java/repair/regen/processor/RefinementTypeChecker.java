package repair.regen.processor;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

import org.hamcrest.core.IsInstanceOf;

import repair.regen.language.Variable;
import repair.regen.smt.SMTEvaluator;
import repair.regen.smt.TypeCheckError;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtExpression;
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
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.factory.TypeFactory;
import spoon.reflect.path.CtRole;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.CtScanner;
import spoon.reflect.visitor.Filter;
import spoon.reflect.visitor.chain.CtFunction;
import spoon.support.comparator.CtLineElementComparator;
import spoon.support.reflect.code.CtVariableWriteImpl;

public class RefinementTypeChecker extends CtScanner {
	// This class should do the following:

	// 1. Keep track of the context variable types
	// 2. Do type checking and inference
	private final String REFINE_KEY = "refinement";
	private final String REFINE_RETURN_KEY = "refinement_return";
	private final String REFINE_PARAMS_KEY = "refinement_params";
	private final String RETURN_VAR = "return_var";
	private int counter = 0;
	private Stack<Map<String, CtTypeReference<?>>> ctx = new Stack<>();

	public RefinementTypeChecker() {
		ctx.add(new HashMap<>()); // default context
	}


	private Map<String, CtTypeReference<?>> getContext() {
		Map<String, CtTypeReference<?>> f = new HashMap<>();
		for (Map<String, CtTypeReference<?>> frame : ctx) {
			for (String s : frame.keySet()) {
				f.put(s, frame.get(s));
			}
		}
		return f;
	}

	private void addToContext(String s, CtTypeReference<?> t) {
		if( !ctx.peek().containsKey(s))
			ctx.peek().put(s,  t);
	}

	
//--------------------- Visitors -----------------------------------

	/**
	 * Visitor for binary operations
	 * Adds metadata to the binary operations from the operands
	 */
	@Override
	public <T> void visitCtBinaryOperator(CtBinaryOperator<T> operator) {
		super.visitCtBinaryOperator(operator);
		StringBuilder sb = new StringBuilder(); 
		CtExpression<?> right = operator.getRightHandOperand();
		CtExpression<?> left = operator.getLeftHandOperand();
		String oper = operator.toString();
		CtElement parent = operator.getParent();
		
		if(parent instanceof CtAssignment<?, ?>) {
			CtVariableWriteImpl<?> parentVar = (CtVariableWriteImpl<?>)((CtAssignment) parent)
					.getAssigned();
			oper = getOperationRefinements(operator, parentVar, operator, sb);
		}else {// if( parent instanceof CtLocalVariable<?> || parent instanceof CtReturn<?>) {
			
			String varRight = getOperationRefinements(operator, right, sb);
			String varLeft = getOperationRefinements(operator, left, sb);
			oper =  varLeft +" "+ getOperatorFromKind(operator.getKind()) +" "+ varRight;
		
		}
		if (operator.getType().getQualifiedName().contentEquals("int")) {
			operator.putMetadata(REFINE_KEY, "\\v == " + oper+ sb.toString());
		}
	}

	@Override
	public <T> void visitCtVariableRead(CtVariableRead<T> variableRead) {
		super.visitCtVariableRead(variableRead);

		CtVariable<T> varDecl = variableRead.getVariable().getDeclaration();
		getVariableMetadada(variableRead, varDecl);
	}

	@Override
	public <T> void visitCtUnaryOperator(CtUnaryOperator<T> operator) {
		super.visitCtUnaryOperator(operator);
		System.out.println("Entrou no unary:" + operator);
		CtExpression ex = operator.getOperand();
		if(ex instanceof CtVariableWrite) {//++, --
			CtVariableWrite w = (CtVariableWrite) ex;
			String name = w.getVariable().getSimpleName();
			String newName = "VV_"+counter++;
			addToContext(newName, w.getType());
			getVariableMetadada(ex, w.getVariable().getDeclaration());
			String metadada = ((String) ex.getMetadata(REFINE_KEY));
			String binOperation = getOperatorFromKind(operator.getKind()).replace("\\v", newName);
			String metaOper = metadada.replace("\\v", newName).replace(name, newName);
			String all = metaOper + " && " + "\\v == "+binOperation;
			checkVariableRefinements(all, name, w.getVariable().getDeclaration());
			//checkSMT(all, binOperation, element);
			
			
			//a++;
			// a = VV_0 + 1 && VV_0 > 0 && a > 0 
			
		}
	}

	@Override
	public <T> void visitCtLiteral(CtLiteral<T> lit) {
		if (lit.getType().getQualifiedName().contentEquals("int")) {
			lit.putMetadata(REFINE_KEY, "\\v == " + lit.getValue());
		}
	}	

	@SuppressWarnings("unchecked")
	@Override
	public <T> void visitCtLocalVariable(CtLocalVariable<T> localVariable) {
		super.visitCtLocalVariable(localVariable);
		//only declaration, no assignment
		if(localVariable.getAssignment() == null) return;

		String refinementFound = (String) localVariable.getAssignment().getMetadata(REFINE_KEY);
		
		CtExpression a = localVariable.getAssignment();
		if (refinementFound == null) {
			refinementFound = "true";
		}
		checkVariableRefinements(refinementFound, localVariable.getSimpleName(), localVariable);

	}

	@Override
	public <T,A extends T> void visitCtAssignment(CtAssignment<T,A> assignement) {
		super.visitCtAssignment(assignement);
		CtExpression<T> ex =  assignement.getAssigned();

		if (ex instanceof CtVariableWriteImpl) {
			CtVariable<T> varDecl = (CtVariable<T>) ((CtVariableAccess<?>) ex)
					.getVariable()
					.getDeclaration();
			getVariableMetadada(ex, varDecl);

			String refinementFound = (String) assignement.getAssignment().getMetadata(REFINE_KEY);
			if (refinementFound == null) {
				refinementFound = "true";
			}
			checkVariableRefinements(refinementFound, varDecl.getSimpleName(), varDecl);
		}
	}
	
	public <R> void visitCtInvocation(CtInvocation<R> invocation) {
		super.visitCtInvocation(invocation);
		CtExecutable<?> method = invocation.getExecutable().getDeclaration();
		if(method != null) {
			String methodRef = (String)method.getMetadata(REFINE_RETURN_KEY);
			if(methodRef != null) {
				//Checking Parameters
				List<CtParameter<?>> params = method.getParameters();
				List<CtExpression<?>> exps = invocation.getArguments();
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < params.size(); i++) {
					CtParameter<?> param = params.get(i);
					CtExpression<?> exp = exps.get(i);
					String name = param.getSimpleName(),
							refPar = (String)param.getMetadata(REFINE_KEY),
							refInv = ((String)exp.getMetadata(REFINE_KEY)).replace("\\v", name),
							correctRef = sb.length()==0? refInv : sb.toString() + " && ("+refInv+")";
					if(exp instanceof CtVariableRead<?>)
						addToContext(((CtVariableRead) exp).getVariable().getSimpleName(), 
								((CtVariableRead) exp).getType());
				
					checkSMT(correctRef, refPar, (CtVariable<?>)param);
					//refPar = correctRef+ " && "+refPar;//TODO CONFIRM IF THIS MAKES SENSE
					
					sb.append(sb.length() == 0 ? refPar:" && "+refPar);
				}
				//Checking Return
				String s = sb.length() == 0? methodRef:  sb.append(" && "+methodRef).toString();
				invocation.putMetadata(REFINE_KEY, s);
			}

		}
	}
	
	@Override
	public <R> void visitCtReturn(CtReturn<R> ret) {
		super.visitCtReturn(ret);
		System.out.println(ret);
		if(ret.getReturnedExpression() != null) {
			CtMethod method = ret.getParent(CtMethod.class);
			String returnVarName = "RET_"+counter++; 
			String retRef = "("+((String)ret.getReturnedExpression().getMetadata(REFINE_KEY))
					.replace("\\v", returnVarName)+")";
			String expectedType = ((String) method.getMetadata(REFINE_RETURN_KEY)).replace("\\v", returnVarName);
			String paramsRef = (String)method.getMetadata(REFINE_PARAMS_KEY);
			String correctRef = paramsRef.length() > 0? paramsRef+" && "+retRef : retRef;
			addToContext(returnVarName, method.getType());
			
			checkSMT(correctRef, expectedType, ret);
		}
	}

	public <R> void visitCtMethod(CtMethod<R> method) {
		//super.visitCtMethod(method);
		for(CtAnnotation<? extends Annotation> ann :method.getAnnotations()) {
			if( !ann.getActualAnnotation().annotationType().getCanonicalName()
					.contentEquals("repair.regen.specification.Refinement"))
				break;
			CtLiteral<String> s = (CtLiteral<String>) ann.getAllValues().get("value");
			String methodRef = s.getValue();
			List<CtParameter<?>> params = method.getParameters();
			String[] r = methodRef.split("->");
			StringBuilder sb = new StringBuilder();
			
			for (int i = 0; i < params.size(); i++) {
				CtParameter<?> param = params.get(i);
				String name = param.getSimpleName();
				String metRef = r[i].replace("{", "(").replace("}", ")").replace("\\v", name);
				param.putMetadata(REFINE_KEY, metRef);
				sb.append(sb.length() == 0? metRef : " && "+metRef);
				
				addToContext(name, param.getType());
			}
			String retRef = r[r.length-1].replace("{", "(").replace("}", ")");
			
			method.putMetadata(REFINE_RETURN_KEY, retRef);
			method.putMetadata(REFINE_PARAMS_KEY, sb.toString());
			method.putMetadata(REFINE_KEY, sb.append(" && "+ retRef).toString());
			
		}
		super.visitCtMethod(method);
	}

//------------------------------- Auxiliary Methods ----------------------------------------

	private <T> void checkVariableRefinements(String refinementFound, String simpleName, CtVariable<T> variable) {
		String correctRefinement = refinementFound.replace("\\v", simpleName);
		//variable.putMetadata(REFINE_KEY, correctRefinement);
		Optional<String> expectedType = variable.getAnnotations().stream()
				.filter(
						ann -> ann.getActualAnnotation().annotationType().getCanonicalName()
						.contentEquals("repair.regen.specification.Refinement")
						).map(
								ann -> (CtLiteral<String>) ann.getAllValues().get("value")
								).map(
										str -> str.getValue().replace("\\v", simpleName)
										).findAny();

		expectedType.ifPresent((et) -> {
			checkSMT(correctRefinement, et, variable);

		});

	}

	private <T> void checkSMT(String correctRefinement, String expectedType, CtElement element) {
		System.out.println("SMT subtyping:" + correctRefinement + " <: " + expectedType);
		System.out.println("-----------------------------------------------");

		if (element instanceof CtVariable<?>) {
			CtVariable<?> v = (CtVariable<?>) element;
			addToContext(v.getSimpleName(), v.getType());
		}
		try {
			//System.out.println(ctx);
			new SMTEvaluator().verifySubtype(correctRefinement, expectedType, getContext());
		} catch (TypeCheckError e) {
			printError(element, expectedType, correctRefinement);

		}
		element.putMetadata(REFINE_KEY, expectedType);		
	}


	private <T> void getVariableMetadada(CtElement variable, CtVariable<T> varDecl) {
		String refinementFound = (String) varDecl.getMetadata(REFINE_KEY);
		if (refinementFound == null) {
			refinementFound = "true";
			addToContext(varDecl.getSimpleName(), varDecl.getType());
		}
		variable.putMetadata(REFINE_KEY, "(\\v == " + 
				varDecl.getSimpleName() + 
				") && ("+ refinementFound + ")");

	}	

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
		//TODO case NE: 	return "!=";
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
		case POSTINC:	return "\\v + 1";
		case POSTDEC: 	return "\\v - 1";
		case PREINC:	return "\\v + 1";
		case PREDEC: 	return "\\v - 1";
		//TODO FILL WITH CORRECT
		case NOT: 	return "*";
		case POS: 	return "/";
		case NEG: 	return "0 - \\v";
		default:	return null;
		}
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
			String elem_ref = (String) element.getMetadata(REFINE_KEY);

			//same name as caller k = k +...
			CtElement parent = operator.getParent();
			if(parent instanceof CtAssignment) {
				CtExpression<?> parent_var = ((CtAssignment) parent).getAssigned();
				if(parent_var instanceof CtVariableWriteImpl) {
					String parentName = parentVar.getVariable().getSimpleName();
					if(parentName.equals(elemName)) {
						elemName = "VV_"+counter++;//+parentName;
						addToContext(elemName, parentVar.getType());
						elem_ref = elem_ref.replaceAll(parentName, elemName);

					}
				}
			}
			addToContext(elemName, elemVar.getType());
			sb.append(" && "+elem_ref.replace("\\v", elemName));
			return elemName;
		}
		else if(element instanceof CtBinaryOperator<?>) {
			CtBinaryOperator<?> binop = (CtBinaryOperator<?>) element;
			String right = getOperationRefinements(operator, parentVar, binop.getRightHandOperand(), sb);
			String left = getOperationRefinements(operator, parentVar, binop.getLeftHandOperand(), sb);
			return left +" "+ getOperatorFromKind(binop.getKind()) +" "+ right;
		}else if (element instanceof CtLiteral<?>) {
			CtLiteral<?> l = (CtLiteral<?>) element;
			return l.getValue().toString();
		}

		//TODO Add cases

		return null;
	}
	private String getOperationRefinements(CtBinaryOperator<?> operator, 
			CtExpression<?> element, StringBuilder sb) {
		return getOperationRefinements(operator, null, element, sb);
	}



	/**
	 * Prints the error message
	 * @param <T>
	 * @param var
	 * @param et
	 * @param correctRefinement
	 */
	private <T> void printError(CtElement var, String et, String correctRefinement) {
		System.out.println("______________________________________________________");
		System.err.println("Failed to check refinement at: ");
		System.out.println();
		System.out.println(var);
		System.out.println();
		System.out.println("Type expected:" + et);
		System.out.println("Refinement found:" + correctRefinement);
		System.out.println("Location: " + var.getPosition());
		System.out.println("______________________________________________________");
		System.exit(1);
	}

}
