package regen.test.project;


public class SimpleTest {
    // @Refinement("{x > 0} -> { \\v == 3 * x}")
    // private static int triplePositive(int x) {
    // return x+x+x;
    // }
    public static void main(java.lang.String[] args) {
        // @Refinement("y > 0 && y < 50")
        // int y;
        // y = 10; // okay
        // y = 100; // okay in Java, refinement type error
        // 
        // 
        int a = 3;
        @repair.regen.specification.Refinement("b < 10")
        int b = a;
    }
}

/**
 * @Refinement("y > 0 && y < 50")
 */
/**
 * int y;
 */
/**
 * y = 10; // okay
 */
/**
 * y = 100; // okay in Java, refinement type error
 */
/**
 *
 */
/**
 *
 */
/**
 * int c = y;
 */
/**
 * int a = 3;
 */
/**
 * @Refinement("b < 10")
 */
/**
 * int b = a;
 */
