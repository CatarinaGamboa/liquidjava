package regen.test.project;


// open(Car)
@repair.regen.specification.RefinementAlias("Positive(int x) { x > 0}")
@repair.regen.specification.RefinementAlias("type CarAcceptableYears(int x) { x > 1800 && x < 2050}")
@repair.regen.specification.RefinementAlias("GreaterThan(int x, int y) {x > y}")
public class Car {
    @repair.regen.specification.Refinement("CarAcceptableYears(year)")
    private int year;

    @repair.regen.specification.Refinement("Positive(_)")
    private int seats;

    public void setYear(@repair.regen.specification.Refinement("CarAcceptableYears(_)")
    int year) {
        this.year = year;
    }

    @repair.regen.specification.Refinement("CarAcceptableYears(_)")
    public int getYear() {
        return year;
    }

    @repair.regen.specification.Refinement("_ == GreaterThan(year, y)")
    public boolean isOlderThan(int y) {
        return (this.year) > y;
    }

    @repair.regen.specification.Refinement("_ == old(year) + i")
    public int addYear(int i) {
        return (year) + i;
    }
}

