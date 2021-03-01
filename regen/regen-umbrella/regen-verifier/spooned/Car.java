

// Use of private values inside
// @Refinement("_ == GreaterThan(year, y)")
// public boolean isOlderThan(int y) {
// return this.year > y;
// }
// @StateRefinement(to = "year == old(year)+i")
// @Refinement("_ == old(year) + i")
// public int addYear(int i) {
// year = year + i;
// return year;
// }
// REFINE STATE
// private boolean isOpen;
// 
// @StateRefinement(to = "!open(this)")
// public Car() {
// isOpen = false;
// }
// 
// 
// @StateRefinement(from = "!open(this)", to = "open(this)")
// public void openCar() {
// isOpen = true;
// }
// 
// @StateRefinement(from = "open(this)")
// public void passagerEnters() {
// //...
// }
@repair.regen.specification.RefinementAlias("IntBetween(int val, int lo, int hi) { lo <= val && val <= hi}")
public class Car {
    @repair.regen.specification.Refinement("IntBetween(_, 1800, 2050)")
    private int year;

    public void setYear(@repair.regen.specification.Refinement("IntBetween(_, 1900, 2000)")
    int year) {
        this.year = year;
    }

    @repair.regen.specification.Refinement("IntBetween(_, 1800, 2050)")
    public int getYear() {
        return year;
    }
}

