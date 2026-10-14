package testSuite;

import liquidjava.specification.Refinement;

public class ErrorLengthOfArrayExpression {

    static int nonNegative(@Refinement("_ >= 0") int x) {
        return x;
    }

    static int rowSize(int[][] grid, int i) {
        return nonNegative(grid[i].length - 10); // Expect: Refinement Error
    }
}
