package repair.regen.classes.car1;


public class Test {
    @repair.regen.specification.Refinement("_ < 10")
    public static int getYear() {
        return 8;
    }

    public static void main(java.lang.String[] args) {
        int a = 1998;
        repair.regen.classes.car1.Car c = new repair.regen.classes.car1.Car();
        c.setYear(a);
        @repair.regen.specification.Refinement("_ < 11")
        int j = repair.regen.classes.car1.Test.getYear();
    }
}

