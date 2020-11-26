package regen.test.project;


public class SimpleTest {
    @repair.regen.specification.Refinement("{d >= 0}->{i > d}->{\\v >= d && \\v < i}")
    private static int range(int d, int i) {
        return i + 1;
    }
}

