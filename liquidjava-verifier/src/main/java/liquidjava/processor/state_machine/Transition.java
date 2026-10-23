package liquidjava.processor.state_machine;

import java.util.Set;

import liquidjava.processor.context.ObjectState;
import liquidjava.processor.context.RefinedFunction;

/**
 * A call that is allowed in state {@code from} and leaves the object in one of the states {@code to}
 *
 * @param from
 *            the state the call is made in
 * @param method
 *            the method called
 * @param change
 *            the state refinement of {@code method} that applies in {@code from}
 * @param to
 *            the states the object can be in after the call (more than one when the state after the call is not
 *            determined by the state before it)
 */
public record Transition(State from, RefinedFunction method, ObjectState change, Set<State> to) {
}
