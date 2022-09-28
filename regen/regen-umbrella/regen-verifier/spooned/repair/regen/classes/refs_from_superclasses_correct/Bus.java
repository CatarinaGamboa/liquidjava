package repair.regen.classes.refs_from_superclasses_correct;


// @PrivateRefinement("this.year == k")//verified in visitCtReturn after
// @Refinement(" _ >= 50")
// public void setSeats(int k) {
// seats = k;
// }
// 
@java.lang.SuppressWarnings("unused")
public class Bus extends repair.regen.classes.refs_from_superclasses_correct.Car {
    private int year;

    @repair.regen.specification.Refinement("_ > 20")
    private int seats;

    @java.lang.Override
    public void setYear(@repair.regen.specification.Refinement("IntBetween(x, 1000, 2100)")
    int x) {
        year = x;
    }

    @repair.regen.specification.Refinement(" _ > 20 && _ < h")
    @java.lang.Override
    public int getSeats(int h) {
        if ((seats) < h)
            return seats;
        // dummy
        else
            return 21;
        // dummy

    }
}

