package repair.regen.classes;


public class Test {
    @repair.regen.specification.Refinement("_ < 10")
    public static int getYear() {
        return 8;
    }

    public static void main(java.lang.String[] args) {
        int a = 1998;
        repair.regen.classes.Car c = new repair.regen.classes.Car();
        c.setYear(a);
        @repair.regen.specification.Refinement("_ < 11")
        int j = repair.regen.classes.Test.getYear();
    }
}

