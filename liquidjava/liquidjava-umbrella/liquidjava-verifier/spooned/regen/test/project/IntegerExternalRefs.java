package regen.test.project;


@liquidjava.specification.ExternalRefinementsFor("java.lang.Integer")
public interface IntegerExternalRefs {
    @liquidjava.specification.Refinement("_ == 2147483647")
    public int MAX_VALUE = 0;
}

