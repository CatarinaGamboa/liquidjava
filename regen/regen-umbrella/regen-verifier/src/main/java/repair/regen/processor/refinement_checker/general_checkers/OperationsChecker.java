package repair.regen.processor.refinement_checker.general_checkers;

import java.util.Arrays;
import java.util.List;

import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.EqualsPredicate;
import repair.regen.processor.constraints.OperationPredicate;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.constraints.VariablePredicate;
import repair.regen.processor.context.RefinedFunction;
import repair.regen.processor.context.RefinedVariable;
import repair.regen.processor.refinement_checker.TypeChecker;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFieldRead;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtUnaryOperator;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.code.CtVariableWrite;
import spoon.reflect.code.UnaryOperatorKind;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.declaration.ParentNotInitializedException;
import spoon.support.reflect.code.CtIfImpl;
/**
 * Auxiliar class for handling the type checking of Unary and Binary
 * operations
 * @author Catarina Gamboa
 *
 */
public class OperationsChecker {

	private TypeChecker rtc;

	public OperationsChecker(TypeChecker rtc) {
		this.rtc = rtc; 
	}

	/**
	 * Finds and adds refinement metadata to the binary operation
	 * @param <T>
	 * @param operator
	 */
	public <T> void getBinaryOpRefinements(CtBinaryOperator<T> operator) {
		CtExpression<?> right = operator.getRightHandOperand();
		CtExpression<?> left = operator.getLeftHandOperand();
		Constraint oper;
		CtElement parent = operator.getParent();
		if(parent instanceof CtAssignment<?, ?>  &&
			((CtAssignment) parent).getAssigned() instanceof CtVariableWrite<?>) {
				CtVariableWrite<?> parentVar = (CtVariableWrite<?>)((CtAssignment) parent)
						.getAssigned();
				oper = getOperationRefinements(operator, parentVar, operator);
			
		}else {
			Constraint varRight = getOperationRefinements(operator, right);
			Constraint varLeft = getOperationRefinements(operator, left);
			oper = new OperationPredicate(varLeft,  getOperatorFromKind(operator.getKind()), varRight);
//					new Predicate(String.format("(%s %s %s)", 
//					varLeft,,varRight));

		}
		String type = operator.getType().getQualifiedName(); 
		List<String> types = Arrays.asList(rtc.implementedTypes);
		if(type.contentEquals("boolean")) {
			operator.putMetadata(rtc.REFINE_KEY, oper);
			if (parent instanceof CtLocalVariable<?> || parent instanceof CtUnaryOperator<?> ||
					parent instanceof CtReturn<?>)
				operator.putMetadata(rtc.REFINE_KEY, new EqualsPredicate(new VariablePredicate(rtc.WILD_VAR), oper));
		}else if (types.contains(type)) {
			operator.putMetadata(rtc.REFINE_KEY, new EqualsPredicate(new VariablePredicate(rtc.WILD_VAR), oper));
		}else {
			System.out.println("Literal type not implemented");
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
		String name = rtc.freshFormat;
		Constraint all;
		if(ex instanceof CtVariableWrite) {
			CtVariableWrite<T> w = (CtVariableWrite<T>) ex;
			name = w.getVariable().getSimpleName();
			all = getRefinementUnaryVariableWrite(ex, operator, w, name);
			rtc.checkVariableRefinements(all, name,  w.getType(), operator, w.getVariable().getDeclaration());
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

		Constraint metadata = rtc.getRefinement(ex);
		String newName;
		if(!name.equals(rtc.freshFormat))
			newName = String.format(rtc.instanceFormat, name, rtc.getContext().getCounter());
		else
			newName = String.format(name, rtc.getContext().getCounter());
		Constraint newMeta = metadata.substituteVariable(rtc.WILD_VAR, newName);
		
		Constraint unOp = getOperatorFromKind(operator.getKind(), operator);
		CtElement p = operator.getParent();
		Constraint opS = unOp.substituteVariable(rtc.WILD_VAR, newName);
	
		if(p instanceof CtIf)
			all = opS;
		else
			all = new EqualsPredicate(new VariablePredicate(rtc.WILD_VAR), opS);//TODO SEE IF () IN OPS IS NEEDED
		
		rtc.getContext().addInstanceToContext(newName, ex.getType(), newMeta, operator);
		operator.putMetadata(rtc.REFINE_KEY, all);

	}

	/**
	 * Retrieves all the refinements for the Operation including the refinements of all operands
	 * for expressions without a variable as parent
	 * @param operator
	 * @param element
	 * @return String with the operation refinements
	 */
	private Constraint getOperationRefinements(CtBinaryOperator<?> operator, 
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
	private Constraint getOperationRefinements(CtBinaryOperator<?> operator, CtVariableWrite<?> parentVar, 
			CtExpression<?> element) {
		if(element instanceof CtFieldRead<?>) {
			CtFieldRead<?> field = ((CtFieldRead<?>)element);
			if(field.getVariable().getSimpleName().equals("length")) {
				String name = String.format(rtc.freshFormat, rtc.getContext().getCounter());
				rtc.getContext().addVarToContext(name, element.getType(), 
						rtc.getRefinement(element).substituteVariable(rtc.WILD_VAR, name), field);
				return new VariablePredicate(name);
			}
		}

		if(element instanceof CtVariableRead<?>) {
			CtVariableRead<?> elemVar = (CtVariableRead<?>) element;
			String elemName = elemVar.getVariable().getSimpleName();
			if(elemVar instanceof CtFieldRead)
				elemName = String.format(rtc.thisFormat, elemName);
			Constraint elem_ref = rtc.getContext().getVariableRefinements(elemName);

			String returnName = elemName;

			CtElement parent = operator.getParent();
			//No need for specific values
			if(parent != null && !(parent instanceof CtIfImpl)) {
				elem_ref = rtc.getRefinement(elemVar);
				String newName = String.format(rtc.instanceFormat, elemName, rtc.getContext().getCounter());
				Constraint newElem_ref = elem_ref.substituteVariable(rtc.WILD_VAR, newName);
				//String newElem_ref = elem_ref.replace(rtc.WILD_VAR, newName);
				RefinedVariable newVi = rtc.getContext().addVarToContext(newName, elemVar.getType(), 
						newElem_ref, elemVar);
				rtc.getContext().addSpecificVariable(newVi);
				returnName = newName;
			}
			
			Constraint e = elem_ref.substituteVariable(rtc.WILD_VAR, elemName);
			RefinedVariable rv = rtc.getContext().addVarToContext(elemName, elemVar.getType(), e, elemVar);
			return new VariablePredicate(returnName);
		}

		else if(element instanceof CtBinaryOperator<?>) {
			CtBinaryOperator<?> binop = (CtBinaryOperator<?>) element;
			Constraint right = getOperationRefinements(operator, parentVar, binop.getRightHandOperand());
			Constraint left = getOperationRefinements(operator, parentVar, binop.getLeftHandOperand());
			
			return new OperationPredicate(left, getOperatorFromKind(binop.getKind()), right);
//					Predicate(left+" "+ getOperatorFromKind(binop.getKind()) +" "+ right);

		}else if (element instanceof CtUnaryOperator<?>) {
			Constraint a = (Constraint) element.getMetadata(rtc.REFINE_KEY);
			a = a.substituteVariable(rtc.WILD_VAR, "");
			String s = a.toString().replace("(", "").replace(")", "")
					.replace("==", "").replace(" ", "");//TODO IMPROVE
			return new Predicate(String.format("(%s)",s), element, rtc.getErrorEmitter());
			
		}else if (element instanceof CtLiteral<?>) {
			CtLiteral<?> l = (CtLiteral<?>) element;
			return new Predicate(l.getValue().toString(), element, rtc.getErrorEmitter());

		}else if(element instanceof CtInvocation<?>) {
			CtInvocation<?> inv = (CtInvocation<?>) element;
			CtExecutable<?> method = inv.getExecutable().getDeclaration();
			//Get function refinements with non_used variables
			String met = ((CtClass)method.getParent()).getQualifiedName();//TODO check
			RefinedFunction fi = rtc.getContext().getFunction(method.getSimpleName(), met);
			Constraint innerRefs = fi.getRenamedRefinements(rtc.getContext(), inv);//TODO REVER!!
			//Substitute _ by the variable that we send
			String newName = String.format(rtc.freshFormat, rtc.getContext().getCounter());
			
			innerRefs = innerRefs.substituteVariable(rtc.WILD_VAR, newName);
			RefinedVariable rv = rtc.getContext().addVarToContext(newName, fi.getType(), innerRefs, inv);
			return new Predicate(newName, inv, rtc.getErrorEmitter());//Return variable that represents the invocation
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
	private <T> Constraint getRefinementUnaryVariableWrite(CtExpression ex, CtUnaryOperator<T> operator, CtVariableWrite w,
			String name) {
		String newName = String.format(rtc.instanceFormat, name, rtc.getContext().getCounter());
		CtVariable<T> varDecl = w.getVariable().getDeclaration();

		Constraint metadada = rtc.getContext().getVariableRefinements(varDecl.getSimpleName());
		metadada = metadada.substituteVariable(rtc.WILD_VAR, newName);
		metadada = metadada.substituteVariable(name, newName);
		
		Constraint c = getOperatorFromKind(operator.getKind(), ex).substituteVariable(rtc.WILD_VAR, newName);
		
		RefinedVariable rv = rtc.getContext().addVarToContext(newName, w.getType(), metadada, w);
		return new EqualsPredicate(new VariablePredicate(rtc.WILD_VAR), c);
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

	private Constraint getOperatorFromKind(UnaryOperatorKind kind, CtElement elem) {
		String ret = null;
		switch(kind) {
		case POSTINC:	ret = rtc.WILD_VAR+" + 1";break;
		case POSTDEC: 	ret = rtc.WILD_VAR+" - 1";break;
		case PREINC:	ret = rtc.WILD_VAR+" + 1";break;
		case PREDEC: 	ret = rtc.WILD_VAR+" - 1";break;
		//TODO COMPLETE WITH MORE OPERATIONS
		case NOT: 	ret = "!" + rtc.WILD_VAR;break;
		case POS: 	ret = "0 + "+ rtc.WILD_VAR;break;
		case NEG: 	ret = "-" + rtc.WILD_VAR;
		}
		return new Predicate(ret, elem, rtc.getErrorEmitter());
	}
}
