package testSuite;

import liquidjava.specification.Refinement;

public class CorrectLengthOfArrayExpression {

    int[] values = new int[3];

    int[] values() {
        return values;
    }

    static double sum(double[][] grid) {
        double total = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                total += grid[i][j];
            }
        }
        return total;
    }

    static int sizes(CorrectLengthOfArrayExpression o) {
        return o.values().length + o.values.length;
    }

    int ownSize() {
        return values.length + this.values.length;
    }

    static int firstRow(@Refinement("length(grid) > 0") int[][] grid) {
        @Refinement("_ > 0")
        int rows = grid.length;
        return rows + grid[0].length;
    }
}
