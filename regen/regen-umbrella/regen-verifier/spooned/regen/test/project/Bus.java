package regen.test.project;


// @PrivateRefinement("this.year == k")//verified in visitCtReturn after
// @Refinement(" _ >= 50")
// public void setSeats(int k) {
// seats = k;
// }
public class Bus extends regen.test.project.Car {
    private int year;

    @repair.regen.specification.Refinement("_ > 20")
    private int seats;

    @java.lang.Override
    public void setYear(@repair.regen.specification.Refinement("IntBetween(_, 1000, 2010)")
    int x) {
        year = x;
    }

    @repair.regen.specification.Refinement(" _ > 20")
    @java.lang.Override
    public int getSeats() {
        return seats;
    }
}

