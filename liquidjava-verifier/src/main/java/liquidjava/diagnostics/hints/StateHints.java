package liquidjava.diagnostics.hints;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import liquidjava.diagnostics.hints.Hint.Kind;
import liquidjava.processor.context.RefinedFunction;
import liquidjava.processor.context.Variable;
import liquidjava.processor.state_machine.State;
import liquidjava.processor.state_machine.StateMachine;
import liquidjava.utils.Utils;
import spoon.reflect.reference.CtTypeReference;

/**
 * Hints for a call made in a state that does not allow it, read from the class's {@link StateMachine}. Each hint only
 * states what the machine proves, so a suggested sequence of calls always makes the failing call verify.
 */
public final class StateHints {

    private static final int MAX_SEQUENCE_LENGTH = 3;
    private static final int MAX_SEQUENCES = 3;
    private static final int MAX_LISTED_CALLS = 5;

    private final StateMachine machine;
    private final String receiver;

    private StateHints(StateMachine machine, String receiver) {
        this.machine = machine;
        this.receiver = receiver;
    }

    /**
     * @param call
     *            the method that cannot be called
     * @param current
     *            the states the receiver can be in before the call
     * @param receiver
     *            the receiver as written in the code
     */
    public static List<Hint> forCall(StateMachine machine, RefinedFunction call, Set<State> current, String receiver) {
        if (!machine.models(call) || current.isEmpty() || machine.allows(current, call))
            return List.of(); // the machine cannot explain why the call failed
        StateHints hints = new StateHints(machine, receiver);
        return Stream.concat(hints.howToReach(current, call).stream(), hints.allowedCalls(current).stream()).toList();
    }

    private List<Hint> howToReach(Set<State> current, RefinedFunction call) {
        Optional<List<List<RefinedFunction>>> search = machine.shortestPaths(current, call, MAX_SEQUENCES);
        if (search.isEmpty() || search.get().isEmpty() || search.get().get(0).size() > MAX_SEQUENCE_LENGTH)
            return List.of();
        return search.get().stream().map(sequence -> callSequence(current, sequence)).toList();
    }

    private Hint callSequence(Set<State> current, List<RefinedFunction> sequence) {
        Set<State> reached = current;
        for (RefinedFunction method : sequence)
            reached = machine.after(reached, method);
        String calls = sequence.stream().map(this::call).collect(Collectors.joining(", then "));
        return new Hint(Kind.CALL_SEQUENCE, String.format("Call %s before this call to move %s from %s to %s", calls,
                receiver, names(current), names(reached)));
    }

    private List<Hint> allowedCalls(Set<State> current) {
        List<RefinedFunction> allowed = machine.allowedIn(current);
        if (allowed.isEmpty())
            return List.of();
        List<String> signatures = allowed.stream().map(StateHints::signature).toList();
        String others = machine.complete() ? "" : " (and possibly others)";
        return List.of(new Hint(Kind.AVAILABLE_CALLS,
                String.format("In state %s, %s can call: %s%s", names(current), receiver, list(signatures), others)));
    }

    private String call(RefinedFunction method) {
        return receiver + "." + signature(method);
    }

    private static String signature(RefinedFunction method) {
        return Utils.getSimpleName(method.getName()) + parameters(method);
    }

    private static String parameters(RefinedFunction function) {
        return function.getArguments().stream().map(Variable::getType).map(CtTypeReference::getSimpleName)
                .collect(Collectors.joining(", ", "(", ")"));
    }

    private static String names(Collection<State> states) {
        return states.stream().map(State::name).collect(Collectors.joining(" or "));
    }

    private static String list(List<String> items) {
        if (items.size() <= MAX_LISTED_CALLS)
            return String.join(", ", items);
        return String.join(", ", items.subList(0, MAX_LISTED_CALLS)) + ", ...";
    }
}
