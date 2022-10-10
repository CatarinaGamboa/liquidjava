package liquidjava.field_updates;


public class CorrectFieldUpdate {
    public int n = 0;

    @liquidjava.specification.StateRefinement(from = "n(this) > 0", to = "n(this) == n(old(this))")
    public void shouldSucceedIfFieldIsPositive() {
    }

    public static void main(java.lang.String[] args) {
        liquidjava.field_updates.CorrectFieldUpdate t = new liquidjava.field_updates.CorrectFieldUpdate();
        t.n = 1;
        t.shouldSucceedIfFieldIsPositive();
    }
}

