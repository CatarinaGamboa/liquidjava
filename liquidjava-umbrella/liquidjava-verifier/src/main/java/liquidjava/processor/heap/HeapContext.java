package liquidjava.processor.heap;

import liquidjava.processor.context.Variable;
import liquidjava.rj_language.Predicate;
import liquidjava.rj_language.ast.*;
import liquidjava.rj_language.visitors.ExpressionVisitor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Context to keep information about the heap. For a simple code:
 * 
 * <pre>
 * {
 *     &#64;code
 *     String x = allocateNewString();
 * }
 * </pre>
 * 
 * where {@code allocateNewString()} is annotated with precondition `emp` and postcondition `ret -> s` the checking code
 * should be
 * 
 * <pre>
 * {@code
 * var alTr = HeapContext.Transition.createAllocation(ret)
 * hctx.applyFrameRule(alTr);
 * }
 * </pre>
 *
 *
 */
public class HeapContext {

    Map<String, Pointee> heap;

    public static HeapContext fromPredicate(Predicate p) throws Exception {
        HeapContext res = new HeapContext();

        p.getExpression().accept(new ExpressionVisitor() {
            @Override
            public void visitAliasInvocation(AliasInvocation ai) throws Exception {

            }

            @Override
            public void visitBinaryExpression(BinaryExpression be) throws Exception {

                if (Objects.equals(be.getOperator(), "|*")) {
                    be.getFirstOperand().accept(this);

                    be.getSecondOperand().accept(this);

                } else if (Objects.equals(be.getOperator(), "|->")) {

                } else {
                    throw new Exception("Heap refinement should contain only `|*` and `|->` connections");
                }
            }

            @Override
            public void visitFunctionInvocation(FunctionInvocation fi) throws Exception {

            }

            @Override
            public void visitGroupExpression(GroupExpression ge) throws Exception {

            }

            @Override
            public void visitITE(Ite ite) throws Exception {

            }

            @Override
            public void visitLiteralBoolean(LiteralBoolean lb) {

            }

            @Override
            public void visitLiteralInt(LiteralInt li) {

            }

            @Override
            public void visitLiteralReal(LiteralReal lr) throws Exception {

            }

            @Override
            public void visitLiteralString(LiteralString ls) {

            }

            @Override
            public void visitUnaryExpression(UnaryExpression ue) throws Exception {

            }

            @Override
            public void visitVar(Var v) throws Exception {

            }

            @Override
            public void visitUnit(SepUnit unit) throws Exception {

            }

            @Override
            public void visitSepEmp(SepEmp sepEmp) throws Exception {

            }
        });

        return res;
    }

    /**
     * Merges one this with another HeapContext with another
     *
     * @param extension
     *            heaplet to extend with
     * 
     * @return this
     */

    private HeapContext extend(HeapContext extension) {
        return this;
    }

    private HeapContext() {
        heap = new HashMap<>();
    }

    public static HeapContext empty() {
        return new HeapContext();
    }

    public Expression asExpression() {
        return null;
    }

    /**
     * Extends heaplet with pre- and postcondition
     *
     * <pre>
     * {@code
     * hctx.applyFrameRule(P, Q)
     * }
     * </pre>
     *
     * <pre>
     * {@code
     * P {Code} Q
     * ---------------
     * hctx * P {Code} hctx * Q
     * }
     * </pre>
     *
     * @param precondition
     *            heaplet for precondition
     * @param postcondition
     *            heaplet for postcondition
     * 
     * @return heaplet extended with provided pair
     */
    public HeapContext applyFrameRule(HeapContext precondition, HeapContext postcondition) {
        return this;
    }

    public HeapContext applyFrameRule(Transition transition) {
        return applyFrameRule(transition.getPre(), transition.getPost());
    }

    static public class Transition {
        HeapContext precondition;
        HeapContext postcondition;

        private Transition() {
            precondition = HeapContext.empty();
            postcondition = HeapContext.empty();
        }

        public Transition add(Transition t) {
            precondition.extend(t.precondition);
            postcondition.extend(t.postcondition);
            return this;
        }

        public HeapContext getPre() {
            return precondition;
        }

        public HeapContext getPost() {
            return postcondition;
        }

        /**
         * <pre>
         * {@code
         * sep.emp
         * --------------
         * sep.emp
         * }
         * </pre>
         * 
         * @return heaplet pair of empty heaps
         */
        static public Transition id() {
            return new Transition();
        }

        /**
         * Generates a precondition and postcondition for mutation
         * 
         * <pre>
         * {@code
         * v -> generatedValue
         * --------------
         * sep.emp
         * }
         * </pre>
         * 
         * @param v
         *            consumed value
         * 
         * @return heaplet pair for precondition and postcondition
         */
        static public Transition consumption(Variable v) {
            return null;
        }

        /**
         * Generates a precondition and postcondition for mutation
         * 
         * <pre>
         * {@code
         * v -> generatedValue1
         * --------------
         * v -> generatedValue2
         * }
         * </pre>
         * 
         * @param v
         *            mutated value
         * 
         * @return heaplet pair for precondition and postcondition
         */
        static public Transition mutation(Variable v) {
            return null;
        }

        /**
         * Generates a precondition and postcondition for allocation
         * 
         * <pre>
         * {@code
         * sep.emp
         * --------------
         * v -> generatedValue
         * }
         * </pre>
         *
         * @param v
         *            created variable
         * 
         * @return heaplet pair for precondition and postcondition
         */
        static public Transition allocation(Variable v) {
            return null;
        }
    }
}
