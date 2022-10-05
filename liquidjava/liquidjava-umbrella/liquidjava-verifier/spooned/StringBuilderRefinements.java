

public interface StringBuilderRefinements {
    @liquidjava.specification.RefinementPredicate("int lengthS(StringBuilder s)")
    @liquidjava.specification.StateRefinement(to = "lengthS() == 0")
    public void StringBuilder();

    @liquidjava.specification.StateRefinement(from = "#i == lengthS()", to = "lengthS() == (#i + 1)")
    public java.lang.StringBuilder append(char c);
}

