package liquidjava.api.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import liquidjava.api.CommandLineLauncher;
import liquidjava.diagnostics.Diagnostics;
import liquidjava.diagnostics.errors.LJError;
import liquidjava.diagnostics.errors.StateRefinementError;
import liquidjava.diagnostics.hints.Hint;
import liquidjava.diagnostics.hints.Hint.Kind;

class TestStateHints {

    private static final String TEST_SUITE = "../liquidjava-example/src/main/java/testSuite/";

    @Test
    void missingCallIsSuggestedWithTheCallsAllowedNow() {
        assertHints("ErrorStateHintCallSequence.java",
                new Hint(Kind.CALL_SEQUENCE, "Call f.open() before this call to move f from created to open"),
                new Hint(Kind.AVAILABLE_CALLS, "In state created, f can call: open()"));
    }

    @Test
    void missingCallsAreSuggestedInOrder() {
        assertHints("ErrorStateHintTwoCalls.java",
                new Hint(Kind.CALL_SEQUENCE,
                        "Call r.prepare(), then r.start() before this call to move r from idle to running"),
                new Hint(Kind.AVAILABLE_CALLS, "In state idle, r can call: prepare()"));
    }

    @Test
    void statesOfMultipleStateSetsAreCombined() {
        assertHints("ErrorStateHintMultipleSets.java",
                new Hint(Kind.CALL_SEQUENCE,
                        "Call d.unlock() before this call to move d from closed and locked to closed and unlocked"),
                new Hint(Kind.AVAILABLE_CALLS, "In state closed and locked, d can call: unlock()"));
    }

    @Test
    void callAllowedByDifferentRefinementsInEachStateIsNotSuggested() {
        assertHints("ErrorStateHintSeveralRefinements.java");
    }

    @Test
    void receiverThatIsNotAVariableGetsNoHints() {
        assertHints("ErrorStateHintMethodCallReceiver.java");
    }

    @Test
    void classWithoutStateSetKeepsTheGeneralHint() {
        assertHints("ErrorUnconstrainedStateRefinement.java", Hint.general(
                "No state information is known for this object. Make sure its state is initialized and available at this point."));
    }

    private static void assertHints(String test, Hint... expected) {
        CommandLineLauncher.launch(TEST_SUITE + test);
        List<LJError> errors = Diagnostics.getInstance().getErrors().stream().toList();
        assertEquals(1, errors.size(), "Expected exactly one error from " + test);
        StateRefinementError error = assertInstanceOf(StateRefinementError.class, errors.get(0));
        assertEquals(List.of(expected), error.getHints());
    }
}
