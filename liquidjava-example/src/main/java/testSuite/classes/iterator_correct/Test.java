package testSuite.classes.iterator_correct;

public class Test {

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Iterator i = new Iterator();
        boolean hn = true;
        i.hasNext(true);
        int x = i.next(hn);
    }
}
