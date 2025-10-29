package testSuite.classes.index_out_of_bounds_correct;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        ArrayList<Integer> l = new ArrayList<>();
        l.add(1);
        l.get(0);
    }
}