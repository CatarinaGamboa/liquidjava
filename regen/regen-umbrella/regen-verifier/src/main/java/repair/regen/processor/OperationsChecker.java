package repair.regen.processor;

import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtUnaryOperator;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.code.CtVariableWrite;
import spoon.reflect.code.UnaryOperatorKind;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.declaration.ParentNotInitializedException;
import spoon.support.reflect.code.CtIfImpl;
import spoon.support.reflect.code.CtVariableWriteImpl;
/**
 * Auxiliar class for handling the type checking of Unary and Binary
 * operations
 * @author Catarina Gamboa
 *
 */
class OperationsChecker {

	private Context context;
	private RefinementTypeChecker rtc;

	public OperationsChecker(RefinementTypeChecker rtc) {
		this.rtc = rtc; 
		context = rtc.context;
	}

	/**
	 * Finds and adds refinement metadata to the binary operation
	 * @param <T>
	 * @param operator
	 */
	public <T> void getBinaryOpRefinements(CtBinaryOperator<T> operator) {
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
		String type = operator.getType().getQualifiedName(); 
		if (type.contentEquals("int") || type.contentEquals("long") || 
				type.contentEquals("double")) {
			operator.putMetadata(rtc.REFINE_KEY, rtc.WILD_VAR+" == " + oper);
		}else if(type.contentEquals("boolean")) {
			operator.putMetadata(rtc.REFINE_KEY, oper);
			if (parent instanceof CtLocalVariable<?> || parent instanceof CtUnaryOperator<?> ||
					parent instanceof CtReturn<?>)
				operator.putMetadata(rtc.REFINE_KEY, rtc.WILD_VAR+" == (" + oper+")");
		}
		//TODO ADD TYPES
	}

	/**
	 * Finds and adds refinement metadata to the unary operation
	 * @param <T>
	 * @param operator
	 */
	public <T> void getUnaryOpRefinements(CtUnaryOperator<T> operator) {
		CtExpression<T> ex = operator.getOperand();
		String name = rtc.FRESH, all;
		if(ex instanceof CtVariableWrite) {
			CtVariableWrite<T> w = (CtVariableWrite<T>) ex;
			name = w.getVariable().getSimpleName();
			all = getRefinementUnaryVariableWrite(ex, operator, w, name);
			rtc.checkVariableRefinements(all, name, w.getVariable().getDeclaration());
			return;

		}else if (ex instanceof CtVariableRead){
			CtVariableRead<T> var = (CtVariableRead<T>) ex;
			name = var.getVariable().getSimpleName();
			//If the variable is the same, the refinements need to be changed
			try {
				CtAssignment<?, ?> assign = operator.getParent(CtAssignment.class);
				if(assign!= null && assign.getAssigned() instanceof CtVariableWrite<?>) {
					CtVariableWrite<?> w = (CtVariableWrite<?>) assign.getAssigned();
					String parentName = w.getVariable().getSimpleName();
					if(name.equals(parentName)) {
						all = getRefinementUnaryVariableWrite(ex, operator, w, name);
						operator.putMetadata(rtc.REFINE_KEY, all);
						return;
					}
				}
			}catch(ParentNotInitializedException e) {
				System.out.println("Parent not initialized");
			}
		}

		String metadata = rtc.getRefinement(ex);
		String newName = name+"_"+context.getCounter()+"_";
		String newMeta = "("+metadata.replace(rtc.WILD_VAR, newName)+")";
		String unOp = getOperatorFromKind(operator.getKind());

		CtElement p = operator.getParent();
		String opS = unOp.replace(rtc.WILD_VAR, newName);
		if(p instanceof CtIf)
			all = unOp.replace(rtc.WILD_VAR, newName);
		else
			all ="("+rtc.WILD_VAR+" == " + opS + ")";
		System.out.println(newMeta + " && "+all);
		context.addVarToContext(newName, ex.getType(), newMeta);
		rtc.addRefinementVariable(newName);
		operator.putMetadata(rtc.REFINE_KEY, all);

	}

	/**
	 * Retrieves all the refinements for the Operation including the refinements of all operands
	 * for expressions without a variable as parent
	 * @param operator
	 * @param element
	 * @return String with the operation refinements
	 */
	private String getOperationRefinements(CtBinaryOperator<?> operator, 
			CtExpression<?> element) {
		return getOperationRefinements(operator, null, element);
	}

	/**
	 * Retrieves all the refinements for the Operation including the refinements of all operands
	 * @param operator Binary Operator that started the operation
	 * @param parentVar Parent of Binary Operator, usually a CtAssignment or CtLocalVariable
	 * @param element CtExpression that represent an Binary Operation or one of the operands
	 * @return String with the operation refinements
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
				elem_ref = rtc.getRefinement(elemVar);
				String newName = elemName+"_"+context.getCounter()+"_";
				String newElem_ref = elem_ref.replace(rtc.WILD_VAR, newName);
				context.addVarToContext(newName, elemVar.getType(), newElem_ref);
				rtc.addRefinementVariable(newName);
				returnName = newName;
			}

			context.addVarToContext(elemName, elemVar.getType(), elem_ref);
			rtc.addRefinementVariable(elemName);
			return returnName;
		}

		else if(element instanceof CtBinaryOperator<?>) {
			CtBinaryOperator<?> binop = (CtBinaryOperator<?>) element;
			String right = getOperationRefinements(operator, parentVar, binop.getRightHandOperand());
			String left = getOperationRefinements(operator, parentVar, binop.getLeftHandOperand());
			return left +" "+ getOperatorFromKind(binop.getKind()) +" "+ right;

		}else if (element instanceof CtUnaryOperator<?>) {
			String a = (String) element.getMetadata(rtc.REFINE_KEY);
			String b = a.replace(rtc.WILD_VAR, "").replace("(", "").replace(")", "")
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
			String newName = rtc.FRESH + context.getCounter();
			innerRefs = innerRefs.replace("\\v", newName);
			context.addVarToContext(newName, fi.getType(), innerRefs);
			rtc.addRefinementVariable(newName);
			return newName;//Return variable that represents the invocation
		}
		return rtc.getRefinement(element);
		//TODO Maybe add cases
	}
	
	/**
	 * Retrieves the refinements for the a variable write inside
	 * unary operation
	 * @param <T>
	 * @param ex
	 * @param operator
	 * @param w
	 * @param name
	 * @return String with the refinements
	 */
	private <T> String getRefinementUnaryVariableWrite(CtExpression ex, CtUnaryOperator<T> operator, CtVariableWrite w,
			String name) {
		String newName = name+"__"+context.getCounter();
		CtVariable<T> varDecl = w.getVariable().getDeclaration();

		String metadada = context.getVariableRefinements(varDecl.getSimpleName());
		rtc.addRefinementVariable(newName);
		String operation = getOperatorFromKind(operator.getKind()).replace(rtc.WILD_VAR, newName);
		String metaOper = metadada.replace(rtc.WILD_VAR, newName).replace(name, newName);
		context.addVarToContext(newName, w.getType(), metaOper);
		return rtc.WILD_VAR+" == "+operation;
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
			//TODO COMPLETE WITH MORE OPERANDS
		}
	}

	private String getOperatorFromKind(UnaryOperatorKind kind) {
		switch(kind) {
		case POSTINC:	return rtc.WILD_VAR+" + 1";
		case POSTDEC: 	return rtc.WILD_VAR+" - 1";
		case PREINC:	return rtc.WILD_VAR+" + 1";
		case PREDEC: 	return rtc.WILD_VAR+" - 1";
		//TODO COMPLETE WITH MORE OPERATIONS
		case NOT: 	return "!" + rtc.WILD_VAR;
		case POS: 	return "0 + "+ rtc.WILD_VAR;
		case NEG: 	return "-" + rtc.WILD_VAR;
		default:	return null;
		}
	}
}
