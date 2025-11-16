package liquidjava.processor.refinement_checker.general_checkers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import liquidjava.diagnostics.errors.CustomError;
import liquidjava.diagnostics.errors.LJError;
import liquidjava.processor.context.RefinedFunction;
import liquidjava.processor.context.RefinedVariable;
import liquidjava.processor.context.Variable;
import liquidjava.processor.context.VariableInstance;
import liquidjava.processor.refinement_checker.TypeChecker;
import liquidjava.utils.constants.Formats;
import liquidjava.utils.constants.Keys;
import liquidjava.utils.constants.Ops;
import liquidjava.utils.constants.Types;
import liquidjava.rj_language.Predicate;
import org.apache.commons.lang3.NotImplementedException;
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
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.declaration.ParentNotInitializedException;
import spoon.reflect.reference.CtVariableReference;
import spoon.support.reflect.code.CtIfImpl;

/**
 * Auxiliar class for handling the type checking of Unary and Binary operations
 *
 * @author Catarina Gamboa
 */
public class OperationsChecker {

    private TypeChecker rtc;

    public OperationsChecker(TypeChecker rtc) {
        this.rtc = rtc;
    }

    /**
     * Finds and adds refinement metadata to the binary operation
     *
     * @param <T>
     * @param operator
     */
    public <T> void getBinaryOpRefinements(CtBinaryOperator<T> operator) throws LJError {
        CtExpression<?> right = operator.getRightHandOperand();
        CtExpression<?> left = operator.getLeftHandOperand();
        Predicate oper;
        CtElement parent = operator.getParent();

        if (parent instanceof CtAnnotation)
            return; // Operations in annotations are not handled here

        if (parent instanceof CtAssignment<?, ?>
                && ((CtAssignment<?, ?>) parent).getAssigned() instanceof CtVariableWrite<?>) {
            CtVariableWrite<?> parentVar = (CtVariableWrite<?>) ((CtAssignment<?, ?>) parent).getAssigned();
            oper = getOperationRefinements(operator, parentVar, operator);

        } else {
            Predicate varLeft = getOperationRefinements(operator, left);
            Predicate varRight = getOperationRefinements(operator, right);
            oper = Predicate.createOperation(varLeft, getOperatorFromKind(operator.getKind()), varRight);
            // new Predicate(String.format("(%s %s %s)",
            // varLeft,,varRight));

        }
        String type = operator.getType().getQualifiedName();
        List<String> types = Arrays.asList(Types.IMPLEMENTED);
        if (type.contentEquals("boolean")) {
            operator.putMetadata(Keys.REFINEMENT, oper);
            if (parent instanceof CtLocalVariable<?> || parent instanceof CtUnaryOperator<?>
                    || parent instanceof CtReturn<?>)
                operator.putMetadata(Keys.REFINEMENT, Predicate.createEquals(Predicate.createVar(Keys.WILDCARD), oper));
        } else if (types.contains(type)) {
            operator.putMetadata(Keys.REFINEMENT, Predicate.createEquals(Predicate.createVar(Keys.WILDCARD), oper));
        } else if (type.equals("java.lang.String")) {
            // skip strings
        } else {
            throw new NotImplementedException("Literal type not implemented");
        }
        // TODO ADD TYPES
    }

    /**
     * Finds and adds refinement metadata to the unary operation
     *
     * @param <T>
     * @param operator
     */
    @SuppressWarnings({ "unchecked" })
    public <T> void getUnaryOpRefinements(CtUnaryOperator<T> operator) throws LJError {
        CtExpression<T> ex = (CtExpression<T>) operator.getOperand();
        String name = Formats.FRESH;
        Predicate all;
        if (ex instanceof CtVariableWrite) {
            CtVariableWrite<T> w = (CtVariableWrite<T>) ex;
            name = w.getVariable().getSimpleName();
            all = getRefinementUnaryVariableWrite(ex, operator, w, name);
            rtc.checkVariableRefinements(all, name, w.getType(), operator, w.getVariable().getDeclaration());
            return;

        } else if (ex instanceof CtVariableRead) {
            CtVariableRead<T> var = (CtVariableRead<T>) ex;
            name = var.getVariable().getSimpleName();
            // If the variable is the same, the refinements need to be changed
            try {
                CtAssignment<?, ?> assign = operator.getParent(CtAssignment.class);
                if (assign != null && assign.getAssigned() instanceof CtVariableWrite<?>) {
                    CtVariableWrite<T> w = (CtVariableWrite<T>) assign.getAssigned();
                    String parentName = w.getVariable().getSimpleName();
                    if (name.equals(parentName)) {
                        all = getRefinementUnaryVariableWrite(ex, operator, w, name);
                        operator.putMetadata(Keys.REFINEMENT, all);
                        return;
                    }
                }
            } catch (ParentNotInitializedException e) {
                throw new RuntimeException("Parent not initialized");
            }
        }

        Predicate metadata = rtc.getRefinement(ex);
        String newName;
        if (!name.equals(Formats.FRESH))
            newName = String.format(Formats.INSTANCE, name, rtc.getContext().getCounter());
        else
            newName = String.format(name, rtc.getContext().getCounter());
        Predicate newMeta = metadata.substituteVariable(Keys.WILDCARD, newName);

        Predicate unOp = getOperatorFromKind(operator.getKind(), operator);
        CtElement p = operator.getParent();
        Predicate opS = unOp.substituteVariable(Keys.WILDCARD, newName);
        if (p instanceof CtIf)
            all = opS;
        else
            all = Predicate.createEquals(Predicate.createVar(Keys.WILDCARD), opS); // TODO SEE IF () IN OPS IS NEEDED
        rtc.getContext().addInstanceToContext(newName, ex.getType(), newMeta, operator);
        operator.putMetadata(Keys.REFINEMENT, all);
    }

    /**
     * Retrieves all the refinements for the Operation including the refinements of all operands for expressions without
     * a variable as parent
     *
     * @param operator
     * @param element
     *
     * @return String with the operation refinements
     */
    private Predicate getOperationRefinements(CtBinaryOperator<?> operator, CtExpression<?> element) throws LJError {
        return getOperationRefinements(operator, null, element);
    }

    /**
     * Retrieves all the refinements for the Operation including the refinements of all operands
     *
     * @param operator
     *            Binary Operator that started the operation
     * @param parentVar
     *            Parent of Binary Operator, usually a CtAssignment or CtLocalVariable
     * @param element
     *            CtExpression that represent an Binary Operation or one of the operands
     *
     * @return Predicate with the operation refinements
     */
    private Predicate getOperationRefinements(CtBinaryOperator<?> operator, CtVariableWrite<?> parentVar,
            CtExpression<?> element) throws LJError {
        if (element instanceof CtFieldRead<?>) {
            CtFieldRead<?> field = ((CtFieldRead<?>) element);
            if (field.getVariable().getSimpleName().equals("length")) {
                String name = String.format(Formats.FRESH, rtc.getContext().getCounter());
                rtc.getContext().addVarToContext(name, element.getType(),
                        rtc.getRefinement(element).substituteVariable(Keys.WILDCARD, name), field);
                return Predicate.createVar(name);
            }
        }

        if (element instanceof CtVariableRead<?>) {
            CtVariableRead<?> elemVar = (CtVariableRead<?>) element;
            String elemName = elemVar.getVariable().getSimpleName();
            if (elemVar instanceof CtFieldRead)
                elemName = String.format(Formats.THIS, elemName);
            Predicate elem_ref = rtc.getContext().getVariableRefinements(elemName);

            String returnName = elemName;

            CtElement parent = operator.getParent();
            // No need for specific values
            if (parent != null && !(parent instanceof CtIfImpl)) {
                elem_ref = rtc.getRefinement(elemVar);
                String newName = String.format(Formats.INSTANCE, elemName, rtc.getContext().getCounter());
                Predicate newElem_ref = elem_ref.substituteVariable(Keys.WILDCARD, newName);
                // String newElem_ref = elem_ref.replace(rtc.WILD_VAR, newName);
                RefinedVariable newVi = rtc.getContext().addVarToContext(newName, elemVar.getType(), newElem_ref,
                        elemVar);
                rtc.getContext().addSpecificVariable(newVi);
                returnName = newName;
            }

            Predicate e = elem_ref.substituteVariable(Keys.WILDCARD, elemName);
            rtc.getContext().addVarToContext(elemName, elemVar.getType(), e, elemVar);
            return Predicate.createVar(returnName);
        } else if (element instanceof CtBinaryOperator<?>) {
            CtBinaryOperator<?> binop = (CtBinaryOperator<?>) element;
            Predicate right = getOperationRefinements(operator, parentVar, binop.getRightHandOperand());
            Predicate left = getOperationRefinements(operator, parentVar, binop.getLeftHandOperand());

            return Predicate.createOperation(left, getOperatorFromKind(binop.getKind()), right);
            // Predicate(left+" "+ getOperatorFromKind(binop.getKind()) +" "+ right);

        } else if (element instanceof CtUnaryOperator<?>) {
            Predicate a = (Predicate) element.getMetadata(Keys.REFINEMENT);
            a = a.substituteVariable(Keys.WILDCARD, "");
            String s = a.toString().replace("(", "").replace(")", "").replace("==", "").replace(" ", ""); // TODO
            // IMPROVE
            return new Predicate(String.format("(%s)", s), element);

        } else if (element instanceof CtLiteral<?>) {
            CtLiteral<?> l = (CtLiteral<?>) element;
            if (l.getType().getQualifiedName().equals("java.lang.String")) {
                // skip strings
                return new Predicate();
            }
            if (l.getValue() == null)
                throw new CustomError("Null literals are not supported");

            return new Predicate(l.getValue().toString(), element);

        } else if (element instanceof CtInvocation<?>) {
            CtInvocation<?> inv = (CtInvocation<?>) element;
            CtExecutable<?> method = inv.getExecutable().getDeclaration();

            if (method == null)
                return getOperationRefinementFromExternalLib(inv, operator);

            // Get function refinements with non_used variables
            String met = ((CtClass<?>) method.getParent()).getQualifiedName(); // TODO check
            RefinedFunction fi = rtc.getContext().getFunction(method.getSimpleName(), met, inv.getArguments().size());
            Predicate innerRefs = fi.getRenamedRefinements(rtc.getContext(), inv); // TODO REVER!!
            // Substitute _ by the variable that we send
            String newName = String.format(Formats.FRESH, rtc.getContext().getCounter());

            innerRefs = innerRefs.substituteVariable(Keys.WILDCARD, newName);
            rtc.getContext().addVarToContext(newName, fi.getType(), innerRefs, inv);
            return new Predicate(newName, inv); // Return variable that represents the invocation
        }
        return rtc.getRefinement(element);
        // TODO Maybe add cases
    }

    private Predicate getOperationRefinementFromExternalLib(CtInvocation<?> inv, CtBinaryOperator<?> operator)
            throws LJError {

        CtExpression<?> t = inv.getTarget();
        if (t instanceof CtVariableRead) {
            CtVariableReference<?> v = ((CtVariableRead<?>) t).getVariable();
            String c = v.getType().toString();
            String simpleName = inv.getExecutable().getSimpleName();

            // Find Function in Context
            int i = c.indexOf("<");
            String typeNotParametrized = (i > 0) ? c.substring(0, i) : c;
            String methodInClassName = typeNotParametrized + "." + simpleName;
            RefinedFunction fi = rtc.getContext().getFunction(methodInClassName, typeNotParametrized,
                    inv.getArguments().size());
            Predicate innerRefs = fi.getRenamedRefinements(rtc.getContext(), inv); // TODO REVER!!

            // Substitute _ by the variable that we send
            String newName = String.format(Formats.FRESH, rtc.getContext().getCounter());
            innerRefs = innerRefs.substituteVariable(Keys.WILDCARD, newName);
            // change this for the current instance
            RefinedVariable r = rtc.getContext().getVariableByName(v.getSimpleName());
            if (r instanceof Variable) {
                Optional<VariableInstance> ovi = ((Variable) r).getLastInstance();
                if (ovi.isPresent())
                    innerRefs = innerRefs.substituteVariable(Keys.THIS, ovi.get().getName());
            }

            rtc.getContext().addVarToContext(newName, fi.getType(), innerRefs, inv);
            return new Predicate(newName, inv); // Return variable that represents the invocation
        }
        return new Predicate();
    }

    /**
     * Retrieves the refinements for the a variable write inside unary operation
     *
     * @param <T>
     * @param ex
     * @param operator
     * @param w
     * @param name
     *
     * @return String with the refinements
     */
    private <T> Predicate getRefinementUnaryVariableWrite(CtExpression<T> ex, CtUnaryOperator<T> operator,
            CtVariableWrite<T> w, String name) throws LJError {
        String newName = String.format(Formats.INSTANCE, name, rtc.getContext().getCounter());
        CtVariable<T> varDecl = w.getVariable().getDeclaration();

        Predicate metadada = rtc.getContext().getVariableRefinements(varDecl.getSimpleName());
        metadada = metadada.substituteVariable(Keys.WILDCARD, newName);
        metadada = metadada.substituteVariable(name, newName);

        Predicate c = getOperatorFromKind(operator.getKind(), ex).substituteVariable(Keys.WILDCARD, newName);

        rtc.getContext().addVarToContext(newName, w.getType(), metadada, w);
        return Predicate.createEquals(Predicate.createVar(Keys.WILDCARD), c);
    }

    // ############################### Operations Auxiliaries
    // ##########################################

    /**
     * Get the String value of the operator from the enum
     *
     * @param kind
     *
     * @return
     */
    private String getOperatorFromKind(BinaryOperatorKind kind) {
        return switch (kind) {
        case PLUS -> Ops.PLUS;
        case MINUS -> Ops.MINUS;
        case MUL -> Ops.MUL;
        case DIV -> Ops.DIV;
        case MOD -> Ops.MOD;
        case AND -> Ops.AND;
        case OR -> Ops.OR;
        case EQ -> Ops.EQ;
        case NE -> Ops.NEQ;
        case GE -> Ops.GE;
        case GT -> Ops.GT;
        case LE -> Ops.LE;
        case LT -> Ops.LT;
        default -> null;
        };
    }

    private Predicate getOperatorFromKind(UnaryOperatorKind kind, CtElement elem) throws LJError {
        String ret = switch (kind) {
        case POSTINC -> Keys.WILDCARD + " + 1";
        case POSTDEC -> Keys.WILDCARD + " - 1";
        case PREINC -> Keys.WILDCARD + " + 1";
        case PREDEC -> Keys.WILDCARD + " - 1";
        case COMPL -> "(32 & " + Keys.WILDCARD + ")";
        case NOT -> "!" + Keys.WILDCARD;
        case POS -> "0 + " + Keys.WILDCARD;
        case NEG -> "-" + Keys.WILDCARD;
        default -> throw new CustomError(kind + "operation not supported");
        };
        return new Predicate(ret, elem);
    };
}
