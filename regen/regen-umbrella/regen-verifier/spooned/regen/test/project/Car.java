package regen.test.project;


@repair.regen.specification.RefinementAlias("type Positive(int x) { x > 0}")
public class Car {
    @repair.regen.specification.Refinement("_ > 1900 && _ < 2050")
    private int year;

    @repair.regen.specification.Refinement("Positive(_)")
    private int seats;

    public void setYear(@repair.regen.specification.Refinement("y < 0")
    int y) {
        this.year = y - 20;
    }

    @repair.regen.specification.Refinement("_ == year")
    public int getYear() {
        return year;
    }
}

