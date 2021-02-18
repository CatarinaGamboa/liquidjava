package regen.test.project;


@repair.regen.specification.ExternalRefinementsFor("java.lang.Integer")
public interface IntegerRefinements {
    @repair.regen.specification.Refinement("_ == 2147483647")
    static int MAX_VALUE = 0;
}

