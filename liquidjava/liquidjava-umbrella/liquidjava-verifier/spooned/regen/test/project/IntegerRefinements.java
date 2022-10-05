package regen.test.project;


@liquidjava.specification.ExternalRefinementsFor("java.lang.Integer")
public interface IntegerRefinements {
    @liquidjava.specification.Refinement("_ == 2147483647")
    static int MAX_VALUE = 0;
}

