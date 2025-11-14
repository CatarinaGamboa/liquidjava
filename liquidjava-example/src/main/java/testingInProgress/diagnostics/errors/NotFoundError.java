package testingInProgress.diagnostics.errors;

import liquidjava.specification.Refinement;

public class NotFoundError {

    public static void main(String[] args) {
        @Refinement("x > 0")
        int y = 1;
    }
}
