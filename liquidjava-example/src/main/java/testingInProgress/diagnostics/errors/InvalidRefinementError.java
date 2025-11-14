package testingInProgress.diagnostics.errors;

import liquidjava.specification.Refinement;

public class InvalidRefinementError {
    public static void main(String[] args) {
        @Refinement("_ + 1")
        int x = 5;
    }
}
