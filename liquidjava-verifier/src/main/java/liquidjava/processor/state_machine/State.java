package liquidjava.processor.state_machine;

import liquidjava.rj_language.Predicate;

/**
 * A state of a class's typestate: one state from each of its state sets
 *
 * @param name
 *            the state names, e.g. {@code open} or {@code open and locked} for a class with two state sets
 * @param predicate
 *            the predicate that holds for {@code this} in this state
 */
public record State(String name, Predicate predicate) {
}
