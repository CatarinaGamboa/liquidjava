package liquidjava.processor.heap;

import liquidjava.processor.context.Variable;
import liquidjava.rj_language.Predicate;
import liquidjava.rj_language.ast.*;
import liquidjava.rj_language.parsing.ParsingException;
import liquidjava.rj_language.visitors.ExpressionVisitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static liquidjava.utils.Utils.WILD_VAR;

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

    private HeapContext(HashMap<Pointer, Pointee> heap) {
        this.heap = heap;
    }

    private HeapContext addPtoPredicate(Predicate pointer) {
        this.heap.put(new Pointer(pointer), new Pointee(new Predicate(new SepUnit())));
        return this;
    }

    public HeapContext clone() {
        HashMap<Pointer, Pointee> cloned_heap = new HashMap<>();
        heap.forEach((key, value) -> cloned_heap.put(new Pointer(key.p.clone()), new Pointee(value.p.clone())));
        return new HeapContext(cloned_heap);
    }

    /**
     * @return k1 |-> v1 * k2 |-> v2 * ....
     */
    public Predicate toSepConjunctions() {
        return heap.entrySet().stream().map(e -> Predicate.createPto(e.getKey().p, e.getValue().p))
                .reduce(Predicate.emptyHeap(), Predicate::createSepConjunction);
    }

    /**
     * @return (k1 |-> v1 * true) && (k2 |-> v2 * true) && ....
     */
    public List<Predicate> toSeparateHeaplets() {
        return heap.entrySet().stream().map(e -> Predicate.createPto(e.getKey().p, e.getValue().p))
                .collect(Collectors.toList());
    }

    public String toString() {
        if (heap.isEmpty()) {
            return "[]";
        }
        return "["
                + heap.entrySet().stream().map(e -> e.getKey() + "|->" + e.getValue()).collect(Collectors.joining(","))
                + "]";

    }

    static class Pointer {
        Predicate p;

        public Pointer(Predicate p) {
            this.p = p;
        }

        public String toString() {
            return p.toString();
        }
    }

    static class Pointee {
        Predicate p;

        public Pointee(Predicate p) {
            this.p = p;
        }

        public String toString() {
            return p.toString();
        }
    }

    Map<Pointer, Pointee> heap;

    public static HeapContext fromPredicate(Predicate p) throws ParsingException {
        HeapContext res = new HeapContext();

        try {
            p.getExpression().accept(new ExpressionVisitor() {
                @Override
                public void visitAliasInvocation(AliasInvocation ai) throws Exception {
                    throw new Exception("Can't have top level alias invocation: " + ai);
                }

                @Override
                public void visitBinaryExpression(BinaryExpression be) throws Exception {

                    if (Objects.equals(be.getOperator(), "|*")) {
                        be.getFirstOperand().accept(this);
                        be.getSecondOperand().accept(this);

                    } else if (Objects.equals(be.getOperator(), "|->")) {
                        Pointer pointer = new Pointer(new Predicate(be.getFirstOperand()));
                        Pointee pointee = new Pointee(new Predicate(be.getSecondOperand()));
                        res.heap.put(pointer, pointee);
                    } else {
                        throw new Exception("Heap refinement should contain only `|*` and `|->` connections");
                    }
                }

                @Override
                public void visitFunctionInvocation(FunctionInvocation fi) throws Exception {
                    throw new Exception("Can't have top level function invocation: " + fi);
                }

                @Override
                public void visitGroupExpression(GroupExpression ge) throws Exception {
                    ge.getExpression().accept(this);
                }

                @Override
                public void visitITE(Ite ite) throws Exception {
                    throw new Exception("Can't have top level ite: " + ite);
                }

                @Override
                public void visitLiteralBoolean(LiteralBoolean lb) throws Exception {
                    throw new Exception("Can't have top level boolean literal: " + lb);
                }

                @Override
                public void visitLiteralInt(LiteralInt li) throws Exception {
                    throw new Exception("Can't have top level int literal: " + li);
                }

                @Override
                public void visitLiteralReal(LiteralReal lr) throws Exception {
                    throw new Exception("Can't have top level real literal: " + lr);
                }

                @Override
                public void visitLiteralString(LiteralString ls) throws Exception {
                    throw new Exception("Can't have top level string literal: " + ls);
                }

                @Override
                public void visitUnaryExpression(UnaryExpression ue) throws Exception {
                    throw new Exception("Can't have top level unary expression literal: " + ue);
                }

                @Override
                public void visitVar(Var v) throws Exception {
                    throw new Exception("Can't have top level variable: " + v);
                }

                @Override
                public void visitUnit(SepUnit unit) throws Exception {
                    return;
                }

                @Override
                public void visitSepEmp(SepEmp sepEmp) throws Exception {
                    return;
                }
            });
        } catch (Exception e) {
            throw new ParsingException(e);
        }

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

    public void enterContext() {

    }

    public void exitContext() {

    }

    static public class Transition {
        HeapContext precondition;
        HeapContext postcondition;

        private Transition(HeapContext precondition, HeapContext postcondition) {
            this.precondition = precondition;
            this.postcondition = postcondition;
        }

        public Transition clone() {
            return new Transition(precondition.clone(), postcondition.clone());
        }

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

        public Transition substituteWildVar(String to) {
            return this.substituteInPlace(WILD_VAR, to);
        }

        public Transition substituteThis(String to) {
            return this.substituteInPlace("this", to);
        }

        public Transition substituteFromMap(Map<String, String> map) {
            map.forEach(this::substituteInPlace);
            return this;
        }

        public Transition substituteInPlace(String from, String to) {

            System.out.println("Substituting: " + from + " -> " + to);
            System.out.println("Heap transition: " + precondition + " > " + postcondition);
            postcondition.heap.forEach((key, value) -> {
                key.p.substituteInPlace(from, to);
                value.p.substituteInPlace(from, to);
            });
            precondition.heap.forEach((key, value) -> {
                key.p.substituteInPlace(from, to);
                value.p.substituteInPlace(from, to);
            });

            System.out.println("Substituted heap transition: " + precondition + " > " + postcondition);
            return this;
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

        /**
         * Creates heap transition from pre- and postcondition.
         * 
         * @param pre
         *            precondition
         * @param post
         *            postcondition
         * 
         * @return triple representation of {@code pre {C} post}
         */
        static public Transition fromPrePostCond(HeapContext pre, HeapContext post) {
            Transition res = new Transition();
            res.precondition = pre;
            res.postcondition = post;
            return res;
        }

        static public Transition simpleConstructorTransition() {
            return fromPrePostCond(HeapContext.empty(), HeapContext.empty().addPtoPredicate(Predicate.createVar("_")));
        }

        public boolean isId() {
            return postcondition.heap.isEmpty() && precondition.heap.isEmpty();
        }
    }
}
