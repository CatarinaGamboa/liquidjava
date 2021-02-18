package regen.test.project;


@repair.regen.specification.ExternalRefinementsFor("java.lang.Integer")
public interface IntegerExternalRefs {
    @repair.regen.specification.Refinement("_ == 2147483647")
    public int MAX_VALUE = 0;
}

