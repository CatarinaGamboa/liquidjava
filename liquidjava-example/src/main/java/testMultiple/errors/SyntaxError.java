package testMultiple.errors;

import liquidjava.specification.Refinement;

public class SyntaxError {
    
    public static void main(String[] args) {
        @Refinement("x $ 0")
        int x = -1;
    }
}
