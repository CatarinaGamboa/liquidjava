package liquidjava;


@java.lang.SuppressWarnings("unused")
public class CorrectReadSpecificAssignment {
    public void testAssignements() {
        @liquidjava.specification.Refinement("_ > 10")
        int a = 15;
        @liquidjava.specification.Refinement("_ > 14")
        int b = a;
        a = 12;
        @liquidjava.specification.Refinement("_ >= 15")
        int c = b;
        b = 16;
        @liquidjava.specification.Refinement("_ > 14")
        int d = c;
    }

    public void testIfs() {
        @liquidjava.specification.Refinement("_ > 10")
        int a = 15;
        if (a > 14) {
            @liquidjava.specification.Refinement("_ > 14")
            int b = a;
            a = 12;
            @liquidjava.specification.Refinement("_ < 14")
            int c = a;
        }
    }

    public static void addZ(@liquidjava.specification.Refinement("a > 0")
    int a) {
        @liquidjava.specification.Refinement("_ > 0")
        int d = a;
        if (d > 5) {
            @liquidjava.specification.Refinement("b > 5")
            int b = d;
        } else {
            @liquidjava.specification.Refinement("_ <= 5")
            int c = d;
            d = 10;
            @liquidjava.specification.Refinement("b > 9")
            int b = d;
        }
    }

    public void testWithBinOperations() {
        @liquidjava.specification.Refinement("_ > 5")
        int a = 10;
        @liquidjava.specification.Refinement("_ > 10")
        int b = a + 1;
        a = 6;
        b = a * 2;
        @liquidjava.specification.Refinement("_ > 20")
        int c = b * 2;
    }

    public static void main(java.lang.String[] args) {
    }
}

