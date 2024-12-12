package testingInProgress;

import liquidjava.specification.RefinementPredicate;
import liquidjava.specification.StateRefinement;

public interface StringBuilderRefinements {
    @RefinementPredicate("int lengthS(StringBuilder s)")
    @StateRefinement(to = "lengthS() == 0")
    public void StringBuilder();

    @StateRefinement(from = "#i == lengthS()", to = "lengthS() == (#i + 1)")
    public StringBuilder append(char c);
}
