package liquidjava.classes;


public class Test {
    @liquidjava.specification.Refinement("_ < 10")
    public static int getYear() {
        return 8;
    }

    public static void main(java.lang.String[] args) {
        int a = 1998;
        liquidjava.classes.Car c = new liquidjava.classes.Car();
        c.setYear(a);
        @liquidjava.specification.Refinement("_ < 11")
        int j = liquidjava.classes.Test.getYear();
    }
}

