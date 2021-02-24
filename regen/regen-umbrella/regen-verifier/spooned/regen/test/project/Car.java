package regen.test.project;


// @Refinement("_ == old(year) + i")
// public int addYear(int i) {
// return year + i;
// }
// @Refinement("_ == year")
// public int getAge(int yearNow) {
// return (yearNow+1) - year;
// }
@repair.regen.specification.RefinementAlias("Positive(int x) { x > 0}")
@repair.regen.specification.RefinementAlias("type CarAcceptableYears(int x) { x > 1800 && x < 2050}")
public class Car {
    @repair.regen.specification.Refinement("CarAcceptableYears(year)")
    private int year;

    @repair.regen.specification.Refinement("Positive(_)")
    private int seats;

    public void setYear(@repair.regen.specification.Refinement(" _ > 1900 && _ < 2000")
    int year) {
        this.year = year;
    }

    @repair.regen.specification.Refinement("CarAcceptableYears(_)")
    public int getYear() {
        return year;
    }
}

