

public class MyList {
    int[] arr = new int[20];

    @liquidjava.specification.Refinement("lengthA(_) == 0")
    public java.util.ArrayList<java.lang.Integer> createList() {
        return new java.util.ArrayList<java.lang.Integer>();
    }

    @liquidjava.specification.Refinement("lengthA(_) == (1 + lengthA(xs))")
    public java.util.ArrayList<java.lang.Integer> append(java.util.ArrayList xs, int k) {
        return null;
    }
}

