

public interface StringBuilderRefinements {
    @repair.regen.specification.RefinementPredicate("int lengthS(StringBuilder s)")
    @repair.regen.specification.StateRefinement(to = "lengthS() == 0")
    public void StringBuilder();

    @repair.regen.specification.StateRefinement(from = "#i == lengthS()", to = "lengthS() == (#i + 1)")
    public java.lang.StringBuilder append(char c);
}

