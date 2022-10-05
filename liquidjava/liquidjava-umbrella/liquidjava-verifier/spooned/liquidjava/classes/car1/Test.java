package liquidjava.classes.car1;


@java.lang.SuppressWarnings("unused")
public class Test {
    @liquidjava.specification.Refinement("_ < 10")
    public static int getYear() {
        return 8;
    }

    public static void main(java.lang.String[] args) {
        int a = 1998;
        liquidjava.classes.car1.Car c = new liquidjava.classes.car1.Car();
        c.setYear(a);
        @liquidjava.specification.Refinement("_ < 11")
        int j = liquidjava.classes.car1.Test.getYear();
    }
}

