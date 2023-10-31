package liquidjava.field_updates;


public class IncorrectFieldUpdate {
    public int n;

    @liquidjava.specification.StateRefinement(from = "n(this) > 0", to = "n(this) == n(old(this))")
    public void shouldFailIfFieldIsNegative() {
    }

    public static void main(java.lang.String[] args) {
        liquidjava.field_updates.IncorrectFieldUpdate t = new liquidjava.field_updates.IncorrectFieldUpdate();
        t.n = -1;
        t.shouldFailIfFieldIsNegative();
    }
}

