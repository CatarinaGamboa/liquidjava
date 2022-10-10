package liquidjava.field_updates;

import liquidjava.specification.StateRefinement;

public class CorrectFieldUpdate {
    public int n;
    @StateRefinement(from="n(this) > 0", to="n(this) == n(old(this)) + 1")
    public void shouldSucceedIfFieldIsPositive(){
        n = n + 1;
    }

    public static void main(String[] args) {

        CorrectFieldUpdate t = new CorrectFieldUpdate();
        t.n = 1;
        t.shouldSucceedIfFieldIsPositive();
    }
}
