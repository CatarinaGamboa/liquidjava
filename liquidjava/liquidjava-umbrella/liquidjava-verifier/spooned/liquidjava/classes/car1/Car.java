package liquidjava.classes.car1;


// @Refinement("_ == GreaterThan(year, y)")
// public boolean isOlderThan(int y) {
// return this.year > y;
// }
@liquidjava.specification.RefinementAlias("Positive(int x) { x > 0}")
@liquidjava.specification.RefinementAlias("type CarAcceptableYears(int x) { x > 1800 && x < 2050}")
@liquidjava.specification.RefinementAlias("GreaterThan(int x, int y) {x > y}")
public class Car {
    @liquidjava.specification.Refinement("CarAcceptableYears(year)")
    private int year;

    @liquidjava.specification.Refinement("Positive(_)")
    private int seats;

    public void setYear(@liquidjava.specification.Refinement("CarAcceptableYears(_)")
    int year) {
        this.year = year;
    }

    @liquidjava.specification.Refinement("CarAcceptableYears(_)")
    public int getYear() {
        return year;
    }
}

